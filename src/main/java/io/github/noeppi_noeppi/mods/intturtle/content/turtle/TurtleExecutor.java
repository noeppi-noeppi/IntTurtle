package io.github.noeppi_noeppi.mods.intturtle.content.turtle;

import com.mojang.serialization.Codec;
import io.github.noeppi_noeppi.libx.util.ServerMessages;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.engine.CraftCodeInterface;
import io.github.noeppi_noeppi.mods.intturtle.engine.CraftCodeVM;
import io.github.noeppi_noeppi.mods.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.mods.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCalls;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class TurtleExecutor implements CraftCodeInterface {

    private final Turtle turtle;
    private final int instructionsPerTick;
    private final DynamicObjects dot;
    private CraftCodeVM vm;
    
    private final Codec<CraftCodeVM> vmCodec;
    
    private int blockingTicks = 0;
    private boolean skipThisTick = false;
    
    private StringBuilder currentChatline = new StringBuilder();
    
    public TurtleExecutor(Turtle turtle, int instructionsPerTick) {
        this.turtle = turtle;
        this.instructionsPerTick = instructionsPerTick;
        this.dot = new DynamicObjects(this.turtle);
        this.vm = new CraftCodeVM(this, new Memory(new long[]{ 99 }));
        this.vmCodec = CraftCodeVM.codec(this);
    }
    
    public void tick(Consumer<String> statusSetter) {
        if (this.blockingTicks > 0) {
            this.blockingTicks -= 1;
        }
        this.skipThisTick = false;
        codeLoop: if (!vm.exited()) {
            for (int i = 0; i < this.instructionsPerTick; i++) {
                try {
                    this.vm.step();
                    if (this.skipThisTick) {
                        this.skipThisTick = false;
                        break;
                    }
                } catch (IntCodeException e) {
                    statusSetter.accept("Failed at " + e.at + ": " + e.getMessage());
                    vm.forceExit();
                    break codeLoop;
                }
            }
            if (vm.exited()) {
                statusSetter.accept("Exited");
            }
        }
    }
    
    // May not modify the array
    public void start(long[] memory) {
        this.dot.reset();
        this.vm = new CraftCodeVM(this, new Memory(memory));
    }
    
    public CompoundTag save() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("VM", this.vmCodec.encodeStart(NbtOps.INSTANCE, this.vm).getOrThrow(false, err -> {}));
        nbt.put("Dot", this.dot.save());
        nbt.putInt("Blocking", this.blockingTicks);
        nbt.putString("Chat", this.currentChatline.toString());
        return nbt;
    }
    
    public void load(CompoundTag nbt) {
        this.vm = nbt.contains("VM") ? this.vmCodec.decode(NbtOps.INSTANCE, nbt.get("VM")).getOrThrow(false, err -> {}).getFirst() : new CraftCodeVM(this, new Memory(new long[]{ 99 }));
        this.dot.load(nbt.getCompound("Dot"));
        this.blockingTicks = nbt.getInt("Blocking");
        this.currentChatline = new StringBuilder().append(nbt.getString("Chat"));
    }
    
    @Nullable
    private ServerLevel level() {
        Level level = this.turtle.getLevel();
        if (level != null && !level.isClientSide &&level instanceof ServerLevel serverLevel) return serverLevel;
        return null;
    }

    @Override
    public boolean canRead() throws IntCodeException {
        return true;
    }

    @Override
    public long read() throws IntCodeException {
        return '\n';
    }

    @Override
    public void write(long value) throws IntCodeException {
        if (value == '\n') {
            String line = this.currentChatline.toString();
            ServerLevel level = level();
            if (level != null) {
                ServerMessages.broadcast(level, new TextComponent("<IntTurtle> " + line));
            }
            this.currentChatline = new StringBuilder();
        } else if (value < 0 || value > Integer.MAX_VALUE) {
            throw new IntCodeException("Invalid code point for output");
        } else if (value >= ' ' && value != 0x7F) {
            this.currentChatline.appendCodePoint((int) value);
        }
    }

    @Override
    public boolean canInvoke(long callId, Memory memory) throws IntCodeException {
        if (SystemCalls.getSysCall(callId).ticksBlocking() > 0 && this.blockingTicks > 0) {
            this.skipThisTick = true;
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void invoke(long callId, Memory memory) throws IntCodeException {
        SystemCall call = SystemCalls.getSysCall(callId);
        ServerLevel level = level();
        if (level != null) {
            this.blockingTicks += call.ticksBlocking();
            call.invoke(this.turtle, level, memory, this.dot);
        }
    }
}

package io.github.noeppi_noeppi.mods.intturtle.syscall.nbt;

import io.github.noeppi_noeppi.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerLevel;

public class ScTagToInt implements SystemCall {

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        Tag tag = dot.get(Tag.class, new CompoundTag());
        long value = 0;
        if (tag instanceof CompoundTag nbt) {
            value = nbt.size();
        } else if (tag instanceof CollectionTag<?> nbt) {
            value = nbt.size();
        } else if (tag instanceof NumericTag nbt) {
            value = nbt.getAsLong();
        }
        memory.set(0, value);
    }
}

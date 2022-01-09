package io.github.noeppi_noeppi.mods.intturtle.engine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class CraftCodeVM {

    public static final int MAJOR = 1;
    public static final int MINOR = 0;
    public static final String VERSION = "CraftCode v" + MAJOR + "." + MINOR;

    public static Codec<CraftCodeVM> codec(CraftCodeInterface iface) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("inst").orElse(0).forGetter(vm -> vm.instruction),
                Codec.INT.fieldOf("rel").orElse(0).forGetter(vm -> vm.relative),
                Codec.BOOL.fieldOf("ex").orElse(false).forGetter(vm -> vm.exited),
                Memory.CODEC.fieldOf("mem").orElse(new Memory()).forGetter(vm -> vm.memory)
            ).apply(instance, (inst, rel, ex, mem) -> new CraftCodeVM(iface, inst, rel, ex, mem))
        );
    }
    
    private final CraftCodeInterface iface;
    
    private int instruction;
    private int relative;
    private boolean exited;
    private final Memory memory;

    public CraftCodeVM(CraftCodeInterface iface, Memory memory) {
        this.iface = iface;
        this.instruction = 0;
        this.relative = 0;
        this.exited = false;
        this.memory = memory;
    }
    
    private CraftCodeVM(CraftCodeInterface iface, int instruction, int relative, boolean exited, Memory memory) {
        this.iface = iface;
        this.instruction = instruction;
        this.relative = relative;
        this.exited = exited;
        this.memory = memory;
    }

    public boolean exited() {
        return exited;
    }
    
    public void forceExit() {
        exited = true;
    }

    public void step() throws IntCodeException {
        if (this.exited) return;
        try {
            long opcode = this.memory.get(this.instruction);
            if (opcode < 0) throw new IntCodeException("Negative instruction: " + opcode, this.instruction);
            int oldInst = this.instruction;
            int advance = switch ((int) (opcode % 100)) {
                case 1 -> { write(opcode, 3, read(opcode, 1) + read(opcode, 2)); yield 4; }
                case 2 -> { write(opcode, 3, read(opcode, 1) * read(opcode, 2)); yield 4; }
                case 3 -> { if (this.iface.canRead()) { write(opcode, 1, this.iface.read()); yield 2; } else { yield 0; } }
                case 4 -> { this.iface.write(this.read(opcode, 1)); yield 2; }
                case 5 -> { if (read(opcode, 1) != 0) { this.instruction = memVal(read(opcode, 2), "Jump out of memory"); } yield 3; }
                case 6 -> { if (read(opcode, 1) == 0) { this.instruction = memVal(read(opcode, 2), "Jump out of memory"); } yield 3; }
                case 7 -> { write(opcode, 3, (read(opcode, 1) < read(opcode, 2)) ? 1 : 0); yield 4; }
                case 8 -> { write(opcode, 3, (read(opcode, 1) == read(opcode, 2)) ? 1 : 0); yield 4; }
                case 9 -> { this.relative += intVal(read(opcode, 1), "Relative overflow"); yield 2; }
                case 10 -> { long call = read(opcode, 1); if (this.iface.canInvoke(call, this.memory)) { this.iface.invoke(call, this.memory); yield 2; } else { yield 0; } }
                case 99 -> { this.exited = true; yield 0; }
                default -> throw new IntCodeException("Unknown instruction: " + opcode, this.instruction);
            };
            if (this.instruction == oldInst) {
                this.instruction += advance;
            }
        } catch (IntCodeException e) {
            if (e.at != Long.MIN_VALUE) throw e;
            throw new IntCodeException(e, this.instruction);
        }
    }
    
    private long read(long opcode, int pos) throws IntCodeException {
        long param = this.memory.get(this.instruction + pos);
        int mode = (int) ((opcode / Math.pow(10, pos + 1)) % 10);
        return switch (mode) {
            case 0 -> this.memory.get(param);
            case 1 -> param;
            case 2 -> this.memory.get(this.relative + param);
            default -> throw new IntCodeException("Invalid parameter mode for reading: " + mode, this.instruction);
        };
    }
    
    private void write(long opcode, int pos, long value) throws IntCodeException {
        long param = this.memory.get(this.instruction + pos);
        int mode = (int) ((opcode / Math.pow(10, pos + 1)) % 10);
        switch (mode) {
            case 0 -> this.memory.set(param, value);
            case 1 -> throw new IntCodeException("Direct mode can't be used for writing", this.instruction);
            case 2 -> this.memory.set(this.relative + param, value);
            default -> throw new IntCodeException("Invalid parameter mode for writing: " + mode, this.instruction);
        }
    }
    
    @SuppressWarnings("SameParameterValue")
    private int memVal(long value, String err) throws IntCodeException {
        if (value < 0 || value > Integer.MAX_VALUE) {
            throw new IntCodeException(err, value);
        } else {
            return (int) value;
        }
    }
    
    @SuppressWarnings("SameParameterValue")
    private int intVal(long value, String err) throws IntCodeException {
        if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
            throw new IntCodeException(err, value);
        } else {
            return (int) value;
        }
    }
}

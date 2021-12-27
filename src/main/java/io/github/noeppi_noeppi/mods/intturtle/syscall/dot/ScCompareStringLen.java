package io.github.noeppi_noeppi.mods.intturtle.syscall.dot;

import io.github.noeppi_noeppi.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.server.level.ServerLevel;

import java.util.function.Function;

public class ScCompareStringLen implements SystemCall {
    
    private final Function<DynamicObjects, String> getter;
    private final int memoryAddress;

    public ScCompareStringLen(Function<DynamicObjects, String> getter, int memoryAddress) {
        this.getter = getter;
        this.memoryAddress = memoryAddress;
    }

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        String str = getter.apply(dot);
        if (str == null) {
            memory.set(memoryAddress, 0);
        } else {
            StringBuilder sb = new StringBuilder();
            long target = memory.get(0);
            int len = (int) memory.get(target);
            for (long i = 0; i < len; i++) {
                sb.appendCodePoint((int) memory.get(target + i + 1));
            }
            memory.set(memoryAddress, str.equals(sb.toString()) ? len : 0);
        }
    }
}

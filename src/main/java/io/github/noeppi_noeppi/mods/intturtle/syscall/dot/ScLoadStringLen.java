package io.github.noeppi_noeppi.mods.intturtle.syscall.dot;

import io.github.noeppi_noeppi.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.server.level.ServerLevel;

public class ScLoadStringLen implements SystemCall {

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        String str = dot.get(String.class, "");
        long target = memory.get(0);
        int[] values = str.codePoints().toArray();
        memory.set(target, values.length);
        for (int i = 0; i < values.length; i++) {
            memory.set(target + i + 1, values[i]);
        }
        memory.set(target + values.length, 0);
        memory.set(0, values.length);
    }
}

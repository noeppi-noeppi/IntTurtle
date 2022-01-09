package io.github.noeppi_noeppi.mods.intturtle.syscall.dot;

import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.mods.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.server.level.ServerLevel;

public class ScStoreStringLen implements SystemCall {

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        StringBuilder sb = new StringBuilder();
        long target = memory.get(0);
        int len = (int) memory.get(target);
        for (long i = 0; i < len; i++) {
            sb.appendCodePoint((int) memory.get(target + i + 1));
        }
        dot.set(sb.toString());
    }
}

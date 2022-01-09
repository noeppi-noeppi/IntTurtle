package io.github.noeppi_noeppi.mods.intturtle.syscall.base;

import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.engine.CraftCodeVM;
import io.github.noeppi_noeppi.mods.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.mods.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCalls;
import net.minecraft.server.level.ServerLevel;

public class ScVersion implements SystemCall {

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        String str = CraftCodeVM.VERSION + " " + SystemCalls.VERSION;
        dot.set(str);
        memory.set(0, str.codePoints().count());
        memory.set(1, CraftCodeVM.MAJOR);
        memory.set(2, CraftCodeVM.MINOR);
        memory.set(3, SystemCalls.API);
    }
}

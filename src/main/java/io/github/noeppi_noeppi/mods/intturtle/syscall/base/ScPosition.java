package io.github.noeppi_noeppi.mods.intturtle.syscall.base;

import io.github.noeppi_noeppi.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.server.level.ServerLevel;

public class ScPosition implements SystemCall {

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        memory.set(0, turtle.facing().get2DDataValue());
        memory.set(1, turtle.getBlockPos().getX());
        memory.set(2, turtle.getBlockPos().getY());
        memory.set(3, turtle.getBlockPos().getZ());
    }
}

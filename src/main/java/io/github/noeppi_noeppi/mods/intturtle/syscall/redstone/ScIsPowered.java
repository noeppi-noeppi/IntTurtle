package io.github.noeppi_noeppi.mods.intturtle.syscall.redstone;

import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.mods.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.server.level.ServerLevel;

public class ScIsPowered implements SystemCall {

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        memory.set(1, level.hasNeighborSignal(turtle.getBlockPos()) ? 1 : 0);
    }
}

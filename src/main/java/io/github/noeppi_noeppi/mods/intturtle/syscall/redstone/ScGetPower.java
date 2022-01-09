package io.github.noeppi_noeppi.mods.intturtle.syscall.redstone;

import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.mods.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import io.github.noeppi_noeppi.mods.intturtle.util.IntCodeEnums;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

public class ScGetPower implements SystemCall {

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        Direction dir = IntCodeEnums.getDirection((int) memory.get(0));
        memory.set(1, level.getSignal(turtle.getBlockPos().relative(dir), dir.getOpposite()));
    }
}

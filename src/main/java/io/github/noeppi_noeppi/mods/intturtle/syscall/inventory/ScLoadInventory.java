package io.github.noeppi_noeppi.mods.intturtle.syscall.inventory;

import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.dot.objects.DynamicItemHandler;
import io.github.noeppi_noeppi.mods.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.mods.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import io.github.noeppi_noeppi.mods.intturtle.util.MovingDirection;
import net.minecraft.server.level.ServerLevel;

public class ScLoadInventory implements SystemCall {

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        MovingDirection dir = MovingDirection.targetFromMemory(memory.get(0));
        dot.set(new DynamicItemHandler(turtle.targetPos(dir), turtle.facing().getOpposite()));
    }

    @Override
    public int ticksBlocking() {
        return 1;
    }
}

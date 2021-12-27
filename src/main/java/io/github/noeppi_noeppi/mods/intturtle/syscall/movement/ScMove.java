package io.github.noeppi_noeppi.mods.intturtle.syscall.movement;

import io.github.noeppi_noeppi.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import io.github.noeppi_noeppi.mods.intturtle.util.MovingDirection;
import net.minecraft.server.level.ServerLevel;

public class ScMove implements SystemCall {

    public static final int MOVE_DURATION = 12;
    
    private final MovingDirection dir;

    public ScMove(MovingDirection dir) {
        this.dir = dir;
    }

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        memory.set(1, turtle.move(this.dir) ? 1 : 0);
    }

    @Override
    public int ticksBlocking() {
        return MOVE_DURATION;
    }
}

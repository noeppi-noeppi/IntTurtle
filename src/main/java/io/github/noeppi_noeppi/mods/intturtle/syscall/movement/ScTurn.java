package io.github.noeppi_noeppi.mods.intturtle.syscall.movement;

import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.mods.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.server.level.ServerLevel;

public class ScTurn implements SystemCall {

    public static final int TURN_DURATION = 8;
    
    private final boolean right;

    public ScTurn(boolean right) {
        this.right = right;
    }

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        if (right) {
            turtle.turnRight();
        } else {
            turtle.turnLeft();
        }
        memory.set(1, 1);
    }

    @Override
    public int ticksBlocking() {
        return TURN_DURATION;
    }
}

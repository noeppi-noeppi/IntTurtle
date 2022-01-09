package io.github.noeppi_noeppi.mods.intturtle.syscall;

import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.mods.intturtle.engine.Memory;
import net.minecraft.server.level.ServerLevel;

public interface SystemCall {
    
    void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException;
    
    default int ticksBlocking() {
        return 0;
    }
}

package io.github.noeppi_noeppi.mods.intturtle.syscall;

import io.github.noeppi_noeppi.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import net.minecraft.server.level.ServerLevel;

public interface SystemCall {
    
    void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot);
    
    default int ticksBlocking() {
        return 0;
    }
}

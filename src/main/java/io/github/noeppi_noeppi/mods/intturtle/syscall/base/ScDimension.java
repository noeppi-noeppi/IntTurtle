package io.github.noeppi_noeppi.mods.intturtle.syscall.base;

import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.mods.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class ScDimension implements SystemCall {

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        ResourceKey<Level> dim = level.dimension();
        memory.set(0, getDimensionId(dim));
        String rl = dim.location().toString();
        dot.set(rl);
    }
    
    private static int getDimensionId(ResourceKey<Level> dim) {
        if (Level.OVERWORLD.equals(dim)) {
            return 0;
        } else if (Level.NETHER.equals(dim)) {
            return -1;
        } else if (Level.END.equals(dim)) {
            return 1;
        } else {
            return 2;
        }
    }
}

package io.github.noeppi_noeppi.mods.intturtle.syscall.dot;

import io.github.noeppi_noeppi.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObject;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.server.level.ServerLevel;

import java.util.function.Function;

public class ScAllocate implements SystemCall {

    private final Function<DynamicObjects, DynamicObject<?, ?>> source;
    private final long memOut;

    public ScAllocate(Function<DynamicObjects, DynamicObject<?, ?>> source, long memOut) {
        this.source = source;
        this.memOut = memOut;
    }

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        memory.set(this.memOut, dot.allocate(this.source.apply(dot)));
    }
}

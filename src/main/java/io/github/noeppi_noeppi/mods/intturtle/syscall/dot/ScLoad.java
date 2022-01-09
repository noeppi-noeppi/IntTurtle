package io.github.noeppi_noeppi.mods.intturtle.syscall.dot;

import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObject;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.mods.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.server.level.ServerLevel;

import java.util.function.BiConsumer;

public class ScLoad implements SystemCall {

    private final long memIn;
    private final BiConsumer<DynamicObjects, DynamicObject<?, ?>> target;

    public ScLoad(long memIn, BiConsumer<DynamicObjects, DynamicObject<?, ?>> target) {
        this.memIn = memIn;
        this.target = target;
    }


    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        this.target.accept(dot, dot.get((int) memory.get(this.memIn)));
    }
}

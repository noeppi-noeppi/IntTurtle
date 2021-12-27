package io.github.noeppi_noeppi.mods.intturtle.syscall.dot;

import io.github.noeppi_noeppi.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObject;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.server.level.ServerLevel;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class ScMov implements SystemCall {

    private final Function<DynamicObjects, DynamicObject<?, ?>> source;
    private final BiConsumer<DynamicObjects, DynamicObject<?, ?>> target;

    public ScMov(Function<DynamicObjects, DynamicObject<?, ?>> source, BiConsumer<DynamicObjects, DynamicObject<?, ?>> target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        this.target.accept(dot, this.source.apply(dot));
    }
}

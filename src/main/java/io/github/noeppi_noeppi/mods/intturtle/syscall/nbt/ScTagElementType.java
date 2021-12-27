package io.github.noeppi_noeppi.mods.intturtle.syscall.nbt;

import io.github.noeppi_noeppi.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerLevel;

public class ScTagElementType implements SystemCall {

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        Tag tag = dot.get(Tag.class, new CompoundTag());
        long value = Tag.TAG_END;
        if (tag instanceof CollectionTag<?> nbt) {
            value = nbt.getElementType();
        }
        memory.set(0, value);
    }
}

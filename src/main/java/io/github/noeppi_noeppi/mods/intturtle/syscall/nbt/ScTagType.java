package io.github.noeppi_noeppi.mods.intturtle.syscall.nbt;

import io.github.noeppi_noeppi.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;

public class ScTagType implements SystemCall {
    
    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        Tag tag = dot.get(Tag.class, new CompoundTag());
        memory.set(0, tag.getId());
    }
}

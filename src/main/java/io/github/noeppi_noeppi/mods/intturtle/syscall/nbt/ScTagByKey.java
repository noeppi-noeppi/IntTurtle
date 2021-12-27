package io.github.noeppi_noeppi.mods.intturtle.syscall.nbt;

import io.github.noeppi_noeppi.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObject;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.dot.objects.DynamicNBT;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;

import java.util.Objects;

public class ScTagByKey implements SystemCall {

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        Tag tag = dot.get(Tag.class, new CompoundTag());
        String key = dot.getA(String.class, "");
        if (tag instanceof CompoundTag nbt) {
            if (nbt.contains(key)) {
                dot.set(new DynamicNBT(Objects.requireNonNull(nbt.get(key))));
                memory.set(0, 1);
            } else {
                dot.set(DynamicObject.EMPTY);
                memory.set(0, 0);
            }
        } else {
            memory.set(0, 0);
        }
    }
}

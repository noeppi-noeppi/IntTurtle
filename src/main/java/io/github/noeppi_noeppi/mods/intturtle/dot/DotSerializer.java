package io.github.noeppi_noeppi.mods.intturtle.dot;

import net.minecraft.nbt.Tag;

public interface DotSerializer<S, T extends DynamicObject<S, ?>> {
    
    Tag save(T obj);
    T load(Tag nbt);
}

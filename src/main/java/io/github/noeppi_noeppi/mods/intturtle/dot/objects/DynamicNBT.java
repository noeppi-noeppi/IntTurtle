package io.github.noeppi_noeppi.mods.intturtle.dot.objects;

import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DotSerializer;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObject;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;

public class DynamicNBT extends DynamicObject<Tag, Tag> {

    public DynamicNBT(Tag value) {
        super(value.copy(), Tag.class);
    }

    @Override
    public boolean valid(Turtle turtle, ServerLevel level) {
        return true;
    }

    @Nullable
    @Override
    public Tag get(Turtle turtle, ServerLevel level) {
        return this.value.copy();
    }
    
    public enum Serializer implements DotSerializer<Tag, DynamicNBT> {
        INSTANCE;
        
        @Override
        public Tag save(DynamicNBT obj) {
            return obj.value.copy();
        }

        @Override
        public DynamicNBT load(Tag nbt) {
            return new DynamicNBT(nbt.copy());
        }
    }
}

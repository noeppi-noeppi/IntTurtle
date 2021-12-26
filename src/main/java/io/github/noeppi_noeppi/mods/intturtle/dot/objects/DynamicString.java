package io.github.noeppi_noeppi.mods.intturtle.dot.objects;

import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DotSerializer;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObject;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;

public class DynamicString extends DynamicObject<String, String> {

    protected DynamicString(String value) {
        super(value, String.class);
    }

    @Override
    public boolean valid(Turtle turtle, ServerLevel level) {
        return true;
    }

    @Nullable
    @Override
    public String get(Turtle turtle, ServerLevel level) {
        return this.value;
    }
    
    public enum Serializer implements DotSerializer<String, DynamicString> {
        INSTANCE;
        
        @Override
        public Tag save(DynamicString obj) {
            return StringTag.valueOf(obj.value);
        }

        @Override
        public DynamicString load(Tag nbt) {
            return new DynamicString(nbt.getAsString());
        }
    }
}

package io.github.noeppi_noeppi.mods.intturtle.dot;

import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;

import javax.annotation.Nullable;

public abstract class DynamicObject<S, T> {
    
    public static final DynamicObject<Unit, Unit> EMPTY = new DynamicObject<>(Unit.INSTANCE, Unit.class) {

        @Override
        public boolean valid(Turtle turtle, ServerLevel level) {
            return false;
        }

        @Override
        public Unit get(Turtle turtle, ServerLevel level) {
            return Unit.INSTANCE;
        }
    };
    
    protected final S value;
    protected final Class<T> targetClass;

    protected DynamicObject(S value, Class<T> targetClass) {
        this.value = value;
        this.targetClass = targetClass;
    }
    
    public abstract boolean valid(Turtle turtle, ServerLevel level);
    
    @Nullable
    public abstract T get(Turtle turtle, ServerLevel level);
}

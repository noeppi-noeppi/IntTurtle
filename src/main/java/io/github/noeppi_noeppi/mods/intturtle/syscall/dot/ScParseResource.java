package io.github.noeppi_noeppi.mods.intturtle.syscall.dot;

import io.github.noeppi_noeppi.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

public class ScParseResource implements SystemCall {

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        String str = dot.get(String.class);
        ResourceLocation rl = str == null ? null : ResourceLocation.tryParse(str);
        if (rl == null) {
            dot.setA("minecraft");
            dot.setB("missigno");
            memory.set(0, 0);
        } else {
            dot.setA(rl.getNamespace());
            dot.setB(rl.getPath());
            memory.set(0, 1);
        }
    }
}

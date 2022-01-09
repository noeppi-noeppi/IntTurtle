package io.github.noeppi_noeppi.mods.intturtle.syscall.nbt;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.mods.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public class ScCountPath implements SystemCall {
    
    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        Tag tag = dot.getA(Tag.class, new CompoundTag());
        String pathStr = dot.getB(String.class);
        try {
            NbtPathArgument.NbtPath path = NbtPathArgument.nbtPath().parse(new StringReader(pathStr));
            List<Tag> results = path.get(tag);
            memory.set(0, results.size());
        } catch (CommandSyntaxException e) {
            memory.set(0, 0);
        }
    }
}

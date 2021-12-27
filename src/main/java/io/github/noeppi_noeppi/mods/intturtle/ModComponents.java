package io.github.noeppi_noeppi.mods.intturtle;

import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import io.github.noeppi_noeppi.mods.intturtle.content.source.SourceCodeItem;
import io.github.noeppi_noeppi.mods.intturtle.content.turtle.TurtleBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@RegisterClass
public class ModComponents {
    
    public static final Item sourceCode = new SourceCodeItem(IntTurtle.getInstance(), new Item.Properties());
    public static final Block testTurtle = new TurtleBlock(IntTurtle.getInstance(), 1);
}

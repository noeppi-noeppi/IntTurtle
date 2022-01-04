package io.github.noeppi_noeppi.mods.intturtle;

import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import io.github.noeppi_noeppi.mods.intturtle.content.source.SourceCodeItem;
import io.github.noeppi_noeppi.mods.intturtle.content.turtle.TurtleBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@RegisterClass
public class ModComponents {
    
    public static final Item sourceCode = new SourceCodeItem(IntTurtle.getInstance(), new Item.Properties());
    public static final Item basicCircuit = new SourceCodeItem(IntTurtle.getInstance(), new Item.Properties().stacksTo(16));
    public static final Item advancedCircuit = new SourceCodeItem(IntTurtle.getInstance(), new Item.Properties().stacksTo(16));
    
    public static final Block basicTurtle = new TurtleBlock(IntTurtle.getInstance(), 4);
    public static final Block advancedTurtle = new TurtleBlock(IntTurtle.getInstance(), 64);
}

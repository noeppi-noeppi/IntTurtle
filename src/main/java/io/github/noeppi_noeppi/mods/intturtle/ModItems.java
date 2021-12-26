package io.github.noeppi_noeppi.mods.intturtle;

import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import io.github.noeppi_noeppi.mods.intturtle.content.source.SourceCodeItem;
import net.minecraft.world.item.Item;

@RegisterClass
public class ModItems {
    
    public static final Item sourceCode = new SourceCodeItem(IntTurtle.getInstance(), new Item.Properties());
}
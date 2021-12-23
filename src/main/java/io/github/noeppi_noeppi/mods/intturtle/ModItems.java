package io.github.noeppi_noeppi.mods.intturtle;

import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import io.github.noeppi_noeppi.mods.intturtle.content.source.ItemSourceCode;
import net.minecraft.world.item.Item;

@RegisterClass
public class ModItems {
    
    public static final Item sourceCode = new ItemSourceCode(IntTurtle.getInstance(), new Item.Properties());
}

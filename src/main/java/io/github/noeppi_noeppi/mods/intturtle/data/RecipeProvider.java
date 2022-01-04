package io.github.noeppi_noeppi.mods.intturtle.data;

import io.github.noeppi_noeppi.libx.annotation.data.Datagen;
import io.github.noeppi_noeppi.libx.data.provider.recipe.RecipeProviderBase;
import io.github.noeppi_noeppi.libx.data.provider.recipe.SmithingExtension;
import io.github.noeppi_noeppi.libx.data.provider.recipe.crafting.CraftingExtension;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.mods.intturtle.ModComponents;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

@Datagen
public class RecipeProvider extends RecipeProviderBase implements CraftingExtension, SmithingExtension {

    public RecipeProvider(ModX mod, DataGenerator generator) {
        super(mod, generator);
    }

    @Override
    protected void setup() {
        this.shapeless(ModComponents.sourceCode, Items.PAPER, Tags.Items.DYES_BLUE);
        
        this.shaped(ModComponents.basicCircuit, " r ", "tqt", "ggg", 'r', Tags.Items.DUSTS_REDSTONE, 't', Items.REDSTONE_TORCH, 'q', Tags.Items.GEMS_QUARTZ, 'g', Tags.Items.INGOTS_GOLD);
        this.smithing(ModComponents.basicCircuit, Tags.Items.INGOTS_NETHERITE, ModComponents.advancedCircuit);
        
        this.shaped(ModComponents.basicTurtle, "iii", "ici", "bbb", 'i', Tags.Items.INGOTS_IRON, 'c', ModComponents.basicCircuit, 'b', Blocks.POLISHED_ANDESITE);
        this.shaped(ModComponents.advancedTurtle, "iii", "ici", "bbb", 'i', Tags.Items.INGOTS_IRON, 'c', ModComponents.advancedCircuit, 'b', Blocks.POLISHED_BLACKSTONE);
    }
}

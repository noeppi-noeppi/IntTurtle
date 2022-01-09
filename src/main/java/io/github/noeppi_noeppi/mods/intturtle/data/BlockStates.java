package io.github.noeppi_noeppi.mods.intturtle.data;

import io.github.noeppi_noeppi.libx.annotation.data.Datagen;
import io.github.noeppi_noeppi.libx.data.provider.BlockStateProviderBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.mods.intturtle.IntTurtle;
import io.github.noeppi_noeppi.mods.intturtle.content.turtle.TurtleBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

@Datagen
public class BlockStates extends BlockStateProviderBase {
    
    public static final ResourceLocation TURTLE_PARENT = IntTurtle.getInstance().resource("block/int_turtle");

    public BlockStates(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper) {
        super(mod, generator, fileHelper);
    }

    @Override
    protected void setup() {
        
    }

    @Override
    protected void defaultState(ResourceLocation id, Block block, Supplier<ModelFile> model) {
        if (block instanceof TurtleBlock) {
            this.simpleBlock(block, model.get());
        } else {
            super.defaultState(id, block, model);
        }
    }

    @Override
    protected ModelFile defaultModel(ResourceLocation id, Block block) {
        if (block instanceof TurtleBlock) {
            return this.models().withExistingParent(id.getPath(), TURTLE_PARENT)
                    .texture("turtle", new ResourceLocation(id.getNamespace(), "block/" + id.getPath()));
        } else {
            return super.defaultModel(id, block);
        }
    }
}

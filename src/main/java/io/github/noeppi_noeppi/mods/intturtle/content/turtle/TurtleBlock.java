package io.github.noeppi_noeppi.mods.intturtle.content.turtle;

import io.github.noeppi_noeppi.libx.base.tile.BlockBE;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.mods.intturtle.ModItems;
import io.github.noeppi_noeppi.mods.intturtle.content.source.SourceCodeItem;
import net.minecraft.Util;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class TurtleBlock extends BlockBE<Turtle> {

    private final int instructionsPerTick;

    public TurtleBlock(ModX mod, int instructionsPerTick) {
        super(mod, Turtle.class, Properties.copy(Blocks.IRON_BLOCK));
        this.instructionsPerTick = instructionsPerTick;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerClient(ResourceLocation id, Consumer<Runnable> defer) {
        BlockEntityRenderers.register(this.getBlockEntityType(), mgr -> new TurtleRenderer());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        BlockEntity be = super.newBlockEntity(pos, state);
        if (be instanceof Turtle turtle) {
            turtle.setInstructionsPerTick(this.instructionsPerTick);
        }
        return be;
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (stack.getItem() == ModItems.sourceCode) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof Turtle turtle && SourceCodeItem.hasMemory(stack)) {
                    turtle.startProgram(SourceCodeItem.getMemory(stack));
                }
            } else {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof Turtle turtle && turtle.getStatus() != null) {
                    player.sendMessage(new TranslatableComponent("intturtle.turtle.system_message", turtle.getStatus()), Util.NIL_UUID);
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}

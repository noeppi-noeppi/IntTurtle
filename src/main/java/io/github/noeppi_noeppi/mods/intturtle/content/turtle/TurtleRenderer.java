package io.github.noeppi_noeppi.mods.intturtle.content.turtle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import io.github.noeppi_noeppi.mods.intturtle.syscall.movement.ScMove;
import io.github.noeppi_noeppi.mods.intturtle.syscall.movement.ScTurn;
import io.github.noeppi_noeppi.mods.intturtle.util.MovingDirection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.data.EmptyModelData;

import javax.annotation.Nonnull;

public class TurtleRenderer implements BlockEntityRenderer<Turtle> {
    
    @Override
    public void render(@Nonnull Turtle turtle, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        poseStack.pushPose();
        Direction facing = turtle.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        float rot = facing.toYRot() + 180;
        Direction lastDir = turtle.getLastDir();
        if (lastDir != null && facing != lastDir) {
            float oldRot = lastDir.toYRot() + 180;
            rot = Mth.rotLerp((turtle.getTurningTicks() + partialTicks) / ((float) ScTurn.TURN_DURATION), oldRot, rot);
        }
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-rot));
        poseStack.translate(-0.5, -0.5, -0.5);
        if (turtle.getMoveDir() != MovingDirection.NONE) {
            double value = 1 - ((turtle.getMovingTicks() + partialTicks) / ((double) ScMove.MOVE_DURATION));
            poseStack.translate(0, value * turtle.getMoveDir().factorY, value * turtle.getMoveDir().factorZ);
        }

        BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(turtle.getBlockState());
        
        VertexConsumer vertex = buffer.getBuffer(RenderType.solid());
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
                poseStack.last(), vertex, turtle.getBlockState(), model,
                1, 1, 1, light, OverlayTexture.NO_OVERLAY,
                EmptyModelData.INSTANCE
        );

        ItemStack stack1 = turtle.getInventory().getStackInSlot(0);
        if (!stack1.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.95, 0.45, 0.5);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(-90));
            poseStack.scale(0.9f, 0.9f, 0.9f);
            Minecraft.getInstance().getItemRenderer().renderStatic(stack1, ItemTransforms.TransformType.FIXED, light, overlay, poseStack, buffer, 0);
            poseStack.popPose();
        }
        
        ItemStack stack2 = turtle.getInventory().getStackInSlot(1);
        if (!stack2.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.05, 0.45, 0.5);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(-90));
            poseStack.scale(0.9f, 0.9f, 0.9f);
            Minecraft.getInstance().getItemRenderer().renderStatic(stack2, ItemTransforms.TransformType.FIXED, light, overlay, poseStack, buffer, 0);
            poseStack.popPose();
        }
        
        poseStack.popPose();
    }
}

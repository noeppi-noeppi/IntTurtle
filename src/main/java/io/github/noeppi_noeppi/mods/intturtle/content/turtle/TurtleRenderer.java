package io.github.noeppi_noeppi.mods.intturtle.content.turtle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import io.github.noeppi_noeppi.libx.annotation.model.Model;
import io.github.noeppi_noeppi.mods.intturtle.util.MovingDirection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.data.EmptyModelData;

import javax.annotation.Nonnull;

public class TurtleRenderer implements BlockEntityRenderer<Turtle> {

    @Model("block/test_turtle")
    public static BakedModel model = null;
    
    @Override
    public void render(@Nonnull Turtle turtle, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        poseStack.pushPose();
        Direction facing = turtle.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        float rot = facing.toYRot() + 180;
        Direction lastDir = turtle.getLastDir();
        if (lastDir != null && facing != lastDir) {
            float oldRot = lastDir.toYRot() + 180;
            rot = Mth.rotLerp((turtle.getTurningTicks() + partialTicks) / ((float) Turtle.TURN_DURATION), oldRot, rot);
        }
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-rot));
        poseStack.translate(-0.5, -0.5, -0.5);
        if (turtle.getMoveDir() != MovingDirection.NONE) {
            double value = 1 - ((turtle.getMovingTicks() + partialTicks) / ((double) Turtle.MOVE_DURATION));
            poseStack.translate(0, value * turtle.getMoveDir().factorY, value * turtle.getMoveDir().factorZ);
        }

        VertexConsumer vertex = buffer.getBuffer(RenderType.solid());
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
                poseStack.last(), vertex, turtle.getBlockState(), model,
                1, 1, 1, light, OverlayTexture.NO_OVERLAY,
                EmptyModelData.INSTANCE
        );
        
        poseStack.popPose();
    }
}

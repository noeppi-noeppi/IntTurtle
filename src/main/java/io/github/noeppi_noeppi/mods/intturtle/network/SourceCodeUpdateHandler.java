package io.github.noeppi_noeppi.mods.intturtle.network;

import io.github.noeppi_noeppi.mods.intturtle.ModComponents;
import io.github.noeppi_noeppi.mods.intturtle.content.source.SourceCodeItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SourceCodeUpdateHandler {

    public static void handle(SourceCodeUpdateSerializer.Message msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
                if (!stack.isEmpty() && stack.getItem() == ModComponents.sourceCode) {
                    SourceCodeItem.setMemory(stack, msg.memory());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

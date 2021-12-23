package io.github.noeppi_noeppi.mods.intturtle.network;

import io.github.noeppi_noeppi.mods.intturtle.content.source.SourceCodeScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenSourceCodeScreenHandler {

    public static void handle(OpenSourceCodeScreenSerializer.Message msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> Minecraft.getInstance().setScreen(new SourceCodeScreen(msg.title(), msg.memory())));
        ctx.get().setPacketHandled(true);
    }
}

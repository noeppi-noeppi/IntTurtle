package io.github.noeppi_noeppi.mods.intturtle.network;

import io.github.noeppi_noeppi.libx.network.PacketSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public class OpenSourceCodeScreenSerializer implements PacketSerializer<OpenSourceCodeScreenSerializer.Message> {

    @Override
    public Class<Message> messageClass() {
        return Message.class;
    }

    @Override
    public void encode(Message msg, FriendlyByteBuf buffer) {
        buffer.writeComponent(msg.title());
        buffer.writeLongArray(msg.memory());
    }

    @Override
    public Message decode(FriendlyByteBuf buffer) {
        return new Message(buffer.readComponent(), buffer.readLongArray());
    }

    public record Message(Component title, long[] memory) {}
}

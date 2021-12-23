package io.github.noeppi_noeppi.mods.intturtle.network;

import io.github.noeppi_noeppi.libx.network.PacketSerializer;
import net.minecraft.network.FriendlyByteBuf;

public class SourceCodeUpdateSerializer implements PacketSerializer<SourceCodeUpdateSerializer.Message> {

    @Override
    public Class<Message> messageClass() {
        return Message.class;
    }

    @Override
    public void encode(Message msg, FriendlyByteBuf buffer) {
        buffer.writeLongArray(msg.memory());
    }

    @Override
    public Message decode(FriendlyByteBuf buffer) {
        return new Message(buffer.readLongArray());
    }

    public record Message(long[] memory) {}
}

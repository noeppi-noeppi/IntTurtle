package io.github.noeppi_noeppi.mods.intturtle.network;

import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.network.NetworkX;
import net.minecraftforge.network.NetworkDirection;

public class IntTurtleNetwork extends NetworkX {

    public IntTurtleNetwork(ModX mod) {
        super(mod);
    }

    @Override
    protected Protocol getProtocol() {
        return Protocol.of("1");
    }

    @Override
    protected void registerPackets() {
        this.register(new OpenSourceCodeScreenSerializer(), () -> OpenSourceCodeScreenHandler::handle, NetworkDirection.PLAY_TO_CLIENT);
        this.register(new SourceCodeUpdateSerializer(), () -> SourceCodeUpdateHandler::handle, NetworkDirection.PLAY_TO_SERVER);
    }
}

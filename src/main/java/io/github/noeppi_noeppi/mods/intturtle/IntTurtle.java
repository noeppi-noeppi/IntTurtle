package io.github.noeppi_noeppi.mods.intturtle;

import io.github.noeppi_noeppi.libx.mod.registration.ModXRegistration;
import io.github.noeppi_noeppi.libx.mod.registration.RegistrationBuilder;
import io.github.noeppi_noeppi.mods.intturtle.network.IntTurtleNetwork;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nonnull;

@Mod("intturtle")
public final class IntTurtle extends ModXRegistration {
    
    private static IntTurtle instance;
    private static IntTurtleNetwork network;

    public IntTurtle() {
        super(CreativeModeTab.TAB_MISC);
        
        instance = this;
        network = new IntTurtleNetwork(this);
    }
    
    @Nonnull
    public static IntTurtle getInstance() {
        return instance;
    }

    @Nonnull
    public static IntTurtleNetwork getNetwork() {
        return network;
    }

    @Override
    protected void initRegistration(RegistrationBuilder builder) {
        builder.setVersion(1);
    }

    @Override
    protected void setup(FMLCommonSetupEvent event) {
        
    }

    @Override
    protected void clientSetup(FMLClientSetupEvent event) {
        
    }
}

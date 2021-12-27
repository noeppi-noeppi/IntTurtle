package io.github.noeppi_noeppi.mods.intturtle;

import io.github.noeppi_noeppi.libx.mod.registration.ModXRegistration;
import io.github.noeppi_noeppi.libx.mod.registration.RegistrationBuilder;
import io.github.noeppi_noeppi.mods.intturtle.dot.DotManager;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.dot.objects.DynamicItemHandler;
import io.github.noeppi_noeppi.mods.intturtle.dot.objects.DynamicString;
import io.github.noeppi_noeppi.mods.intturtle.network.IntTurtleNetwork;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCalls;
import io.github.noeppi_noeppi.mods.intturtle.syscall.base.ScDimension;
import io.github.noeppi_noeppi.mods.intturtle.syscall.base.ScPosition;
import io.github.noeppi_noeppi.mods.intturtle.syscall.base.ScVersion;
import io.github.noeppi_noeppi.mods.intturtle.syscall.dot.*;
import io.github.noeppi_noeppi.mods.intturtle.syscall.inventory.*;
import io.github.noeppi_noeppi.mods.intturtle.syscall.movement.ScMove;
import io.github.noeppi_noeppi.mods.intturtle.syscall.movement.ScTurn;
import io.github.noeppi_noeppi.mods.intturtle.util.MovingDirection;
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
        DotManager.register(this.resource("string"), DynamicString.class, DynamicString.Serializer.INSTANCE);
        DotManager.register(this.resource("item_handler"), DynamicItemHandler.class, DynamicItemHandler.Serializer.INSTANCE);
        
        // Base
        SystemCalls.register(0, new ScVersion());
        SystemCalls.register(1, new ScPosition());
        SystemCalls.register(2, new ScDimension());
        
        // Dot
        SystemCalls.register(100, new ScMov(DynamicObjects::get, DynamicObjects::setA));
        SystemCalls.register(101, new ScMov(DynamicObjects::get, DynamicObjects::setB));
        SystemCalls.register(102, new ScMov(DynamicObjects::getA, DynamicObjects::set));
        SystemCalls.register(103, new ScMov(DynamicObjects::getB, DynamicObjects::set));
        SystemCalls.register(104, new ScSwap());
        SystemCalls.register(105, new ScAllocate(DynamicObjects::get, 0));
        SystemCalls.register(106, new ScAllocate(DynamicObjects::getA, 1));
        SystemCalls.register(107, new ScAllocate(DynamicObjects::getB, 2));
        SystemCalls.register(108, new ScFree());
        SystemCalls.register(109, new ScLoadStringNull());
        SystemCalls.register(110, new ScLoadStringLen());
        SystemCalls.register(111, new ScStoreStringNull());
        SystemCalls.register(112, new ScStoreStringLen());
        SystemCalls.register(113, new ScCompareStringNull(dot -> dot.get(String.class), 0));
        SystemCalls.register(114, new ScCompareStringLen(dot -> dot.get(String.class), 0));
        SystemCalls.register(115, new ScCompareStringNull(dot -> dot.getA(String.class), 1));
        SystemCalls.register(116, new ScCompareStringLen(dot -> dot.getA(String.class), 1));
        SystemCalls.register(117, new ScCompareStringNull(dot -> dot.getB(String.class), 2));
        SystemCalls.register(118, new ScCompareStringLen(dot -> dot.getB(String.class), 2));
        SystemCalls.register(119, new ScParseResource());
        
        // Movement
        SystemCalls.register(200, new ScMove(MovingDirection.FORWARD));
        SystemCalls.register(201, new ScMove(MovingDirection.UP));
        SystemCalls.register(202, new ScMove(MovingDirection.DOWN));
        SystemCalls.register(203, new ScTurn(false));
        SystemCalls.register(204, new ScTurn(true));
        
        // Inventory
        SystemCalls.register(300, new ScStackSwap());
        SystemCalls.register(301, new ScStackUp());
        SystemCalls.register(302, new ScLoadInventory());
        SystemCalls.register(303, new ScExtract());
        SystemCalls.register(304, new ScInsert());
        SystemCalls.register(305, new ScItemInfo());
    }

    @Override
    protected void clientSetup(FMLClientSetupEvent event) {
        //
    }
}

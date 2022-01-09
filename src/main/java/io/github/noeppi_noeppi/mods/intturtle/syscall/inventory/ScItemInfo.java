package io.github.noeppi_noeppi.mods.intturtle.syscall.inventory;

import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.mods.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.Objects;

public class ScItemInfo implements SystemCall {

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        boolean offHand = memory.get(0) != 0;
        ItemStack stack = turtle.getInventory().getStackInSlot(offHand ? 1 : 0);
        dot.set(Objects.requireNonNull(stack.getItem().getRegistryName()).toString());
        memory.set(0, ((ForgeRegistry<Item>) ForgeRegistries.ITEMS).getID(stack.getItem()));
        memory.set(1, stack.getCount());
        memory.set(2, stack.getMaxStackSize());
        memory.set(3, stack.hasTag() ? 1 : 0);
    }
}

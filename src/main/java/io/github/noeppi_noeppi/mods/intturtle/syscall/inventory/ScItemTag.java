package io.github.noeppi_noeppi.mods.intturtle.syscall.inventory;

import io.github.noeppi_noeppi.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.dot.objects.DynamicNBT;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

public class ScItemTag implements SystemCall {

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        boolean offHand = memory.get(0) != 0;
        ItemStack stack = turtle.getInventory().getStackInSlot(offHand ? 1 : 0);
        dot.set(new DynamicNBT(stack.hasTag() ? stack.getOrCreateTag() : new CompoundTag()));
        memory.set(0, stack.hasTag() ? 1 : 0);
    }
}

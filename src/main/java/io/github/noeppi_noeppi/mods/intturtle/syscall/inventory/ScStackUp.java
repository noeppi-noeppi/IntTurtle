package io.github.noeppi_noeppi.mods.intturtle.syscall.inventory;

import io.github.noeppi_noeppi.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

public class ScStackUp implements SystemCall {

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        ItemStack stack1 = turtle.getInventory().getStackInSlot(0).copy();
        ItemStack stack2 = turtle.getInventory().getStackInSlot(1).copy();
        if (ItemStack.isSameItemSameTags(stack1, stack2)) {
            int amount = Math.max(0, Math.min(stack2.getCount(), stack1.getMaxStackSize() - stack1.getCount()));
            stack1.grow(amount);
            stack2.shrink(amount);
            turtle.getInventory().setStackInSlot(0, stack1);
            turtle.getInventory().setStackInSlot(1, stack2);
            memory.set(0, amount);
        } else {
            memory.set(0, 0);
        }
        memory.set(1, stack1.isEmpty() ? 0 : stack1.getCount());
        memory.set(2, stack2.isEmpty() ? 0 : stack2.getCount());
    }
}

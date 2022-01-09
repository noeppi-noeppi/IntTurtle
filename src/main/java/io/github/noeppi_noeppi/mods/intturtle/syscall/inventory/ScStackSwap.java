package io.github.noeppi_noeppi.mods.intturtle.syscall.inventory;

import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.mods.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

public class ScStackSwap implements SystemCall {

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        ItemStack stack2 = turtle.getInventory().getStackInSlot(0).copy();
        ItemStack stack1 = turtle.getInventory().getStackInSlot(1).copy();
        turtle.getInventory().setStackInSlot(0, stack1);
        turtle.getInventory().setStackInSlot(1, stack2);
        memory.set(1, stack1.isEmpty() ? 0 : stack1.getCount());
        memory.set(2, stack2.isEmpty() ? 0 : stack2.getCount());
    }
}

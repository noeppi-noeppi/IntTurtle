package io.github.noeppi_noeppi.mods.intturtle.syscall.inventory;

import io.github.noeppi_noeppi.intturtle.engine.IntCodeException;
import io.github.noeppi_noeppi.intturtle.engine.Memory;
import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObjects;
import io.github.noeppi_noeppi.mods.intturtle.dot.objects.DynamicItemHandler;
import io.github.noeppi_noeppi.mods.intturtle.syscall.SystemCall;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ScInsert implements SystemCall {

    @Override
    public void invoke(Turtle turtle, ServerLevel level, Memory memory, DynamicObjects dot) throws IntCodeException {
        IItemHandler handler = dot.get(IItemHandler.class, DynamicItemHandler.EMPTY);
        int slot = (int) memory.get(1);
        int max = (int) memory.get(2);
        if (max <= 0) max = Integer.MAX_VALUE - 1;

        if (slot >= handler.getSlots()) {
            memory.set(1, 0);
            memory.set(2, turtle.getInventory().getStackInSlot(0).getCount());
            return;
        }
        
        ItemStack stackToInsert = turtle.getInventory().getStackInSlot(0).copy();
        int actualCount = stackToInsert.getCount();
        stackToInsert.setCount(Math.min(stackToInsert.getCount(), max));
        int initialCount = stackToInsert.getCount();
        int amountToGrowRemainder = actualCount - initialCount;
        if (slot < 0) {
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack remainder = handler.insertItem(i, stackToInsert, false);
                if (remainder.getCount() < initialCount) {
                    remainder.grow(amountToGrowRemainder);
                    turtle.getInventory().setStackInSlot(0, remainder);
                    memory.set(1, initialCount - remainder.getCount());
                    memory.set(2, remainder.getCount());
                    return;
                }
            }
            memory.set(1, 0);
            memory.set(2, turtle.getInventory().getStackInSlot(0).getCount());
        } else {
            ItemStack remainder = handler.insertItem(slot, stackToInsert, false).copy();
            remainder.grow(amountToGrowRemainder);
            turtle.getInventory().setStackInSlot(0, remainder);
            memory.set(1, initialCount - remainder.getCount());
            memory.set(2, remainder.getCount());
        }
    }

    @Override
    public int ticksBlocking() {
        return 1;
    }
}

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

public class ScExtract implements SystemCall {

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
        
        ItemStack extracted = ItemStack.EMPTY;
        int slotToUse = 0;
        if (slot < 0) {
            for (int i = 0; i < handler.getSlots(); i++) {
                extracted = handler.extractItem(i, max, true);
                if (!extracted.isEmpty()) {
                    slotToUse = i;
                    break;
                }
            }
        } else {
            extracted = handler.extractItem(slot, max, true);
            slotToUse = slot;
        }
        
        int amountToExtract = 0;
        ItemStack currentStack = turtle.getInventory().getStackInSlot(0);
        if (!extracted.isEmpty()) {
            if (currentStack.isEmpty()) {
                amountToExtract = extracted.getCount();
            } else if (ItemStack.isSameItemSameTags(extracted, currentStack)) {
                amountToExtract = Math.min(extracted.getCount(), currentStack.getMaxStackSize() - currentStack.getCount());
            }
        }
        
        if (amountToExtract > 0) {
            ItemStack actualExtracted = handler.extractItem(slotToUse, amountToExtract, false);
            if (currentStack.isEmpty()) {
                turtle.getInventory().setStackInSlot(0, actualExtracted.copy());
            } else {
                ItemStack copy = currentStack.copy();
                copy.grow(actualExtracted.getCount());
                turtle.getInventory().setStackInSlot(0, copy);
            }
            memory.set(1, actualExtracted.getCount());
            memory.set(2, currentStack.getCount());
        } else {
            memory.set(1, 0);
            memory.set(2, currentStack.getCount());
        }
    }
}

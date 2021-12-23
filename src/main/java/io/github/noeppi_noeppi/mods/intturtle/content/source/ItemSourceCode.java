package io.github.noeppi_noeppi.mods.intturtle.content.source;

import io.github.noeppi_noeppi.libx.base.ItemBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.mods.intturtle.IntTurtle;
import io.github.noeppi_noeppi.mods.intturtle.network.OpenSourceCodeScreenSerializer;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nonnull;

public class ItemSourceCode extends ItemBase {

    public ItemSourceCode(ModX mod, Properties properties) {
        super(mod, properties.stacksTo(1));
    }
    
    public static boolean hasMemory(ItemStack stack) {
        return stack.hasTag() && stack.getOrCreateTag().contains("IntCodeMemory", Tag.TAG_LONG_ARRAY);
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) return super.use(level, player, hand);
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!level.isClientSide && player instanceof ServerPlayer sp) {
            IntTurtle.getNetwork().channel.send(PacketDistributor.PLAYER.with(() -> sp), new OpenSourceCodeScreenSerializer.Message(stack.getHoverName(), getMemory(stack)));
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    // May not modify the returned array
    public static long[] getMemory(ItemStack stack) {
        if (!hasMemory(stack)) {
            return new long[]{};
        } else {
            return stack.getOrCreateTag().getLongArray("IntCodeMemory");
        }
    }
    
    public static void setMemory(ItemStack stack, long[] memory) {
        long[] memCopy = new long[memory.length];
        System.arraycopy(memory, 0, memCopy, 0, memory.length);
        stack.getOrCreateTag().putLongArray("IntCodeMemory", memCopy);
    }
}

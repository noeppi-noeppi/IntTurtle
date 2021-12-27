package io.github.noeppi_noeppi.mods.intturtle.dot.objects;

import io.github.noeppi_noeppi.libx.inventory.BaseItemStackHandler;
import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import io.github.noeppi_noeppi.mods.intturtle.dot.DotSerializer;
import io.github.noeppi_noeppi.mods.intturtle.dot.DynamicObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class DynamicItemHandler extends DynamicObject<DynamicItemHandler.WrappedItemHandler, IItemHandler> {

    public static final IItemHandler EMPTY = BaseItemStackHandler.builder(0).build();
    
    public DynamicItemHandler(BlockPos pos, Direction dir) {
        super(new WrappedItemHandler(pos.immutable(), dir), IItemHandler.class);
    }

    @Override
    public boolean valid(Turtle turtle, ServerLevel level) {
        return turtle.canReach(this.value.pos(), this.value.dir());
    }

    @Nullable
    @Override
    public IItemHandler get(Turtle turtle, ServerLevel level) {
        BlockEntity be = level.getBlockEntity(this.value.pos);
        if (be == null) return EMPTY;
        LazyOptional<IItemHandler> handler = be.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        return handler.orElse(EMPTY);
    }

    public record WrappedItemHandler(BlockPos pos, Direction dir) {}

    public enum Serializer implements DotSerializer<WrappedItemHandler, DynamicItemHandler> {
        INSTANCE;

        @Override
        public Tag save(DynamicItemHandler obj) {
            CompoundTag nbt = new CompoundTag();
            nbt.put("Pos", NbtUtils.writeBlockPos(obj.value.pos()));
            nbt.putInt("Side", obj.value.dir().get3DDataValue());
            return nbt;
        }

        @Override
        public DynamicItemHandler load(Tag tag) {
            CompoundTag nbt = tag instanceof CompoundTag c ? c : new CompoundTag();
            return new DynamicItemHandler(NbtUtils.readBlockPos(nbt.getCompound("Pos")), Direction.from3DDataValue(nbt.getInt("Side")));
        }
    }
}

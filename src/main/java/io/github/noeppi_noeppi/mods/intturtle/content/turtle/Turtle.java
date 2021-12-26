package io.github.noeppi_noeppi.mods.intturtle.content.turtle;

import io.github.noeppi_noeppi.libx.base.tile.BlockEntityBase;
import io.github.noeppi_noeppi.libx.base.tile.TickableBlock;
import io.github.noeppi_noeppi.mods.intturtle.util.MovingDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Turtle extends BlockEntityBase implements TickableBlock {

    public static final int TURN_DURATION = 4;
    public static final int MOVE_DURATION = 8;
    
    @Nullable
    private TurtleExecutor executor = null;
    
    @Nullable
    private String status = null;
    
    // Client properties
    @Nullable
    private Direction lastDir;
    private int turningTicks = 0;
    private MovingDirection moveDir = MovingDirection.NONE;
    private int movingTicks = 0;
    
    private Direction cachedFacing = null;
    
    @Nullable
    private BlockPos scheduledMove = null;
    
    public Turtle(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void setInstructionsPerTick(int instructionsPerTick) {
        this.executor = new TurtleExecutor(this, instructionsPerTick);
    }

    public void startProgram(long[] memory) {
        if (this.executor != null) {
            this.status = null;
            this.executor.start(memory);
            this.setChanged();
        }
    }
    
    @Override
    public void tick() {
        this.cachedFacing = null;
        if (this.lastDir == null) {
            this.lastDir = facing();
            this.setChanged();
            this.setDispatchable();
        }
        if (this.lastDir != facing()) {
            this.turningTicks += 1;
            if (this.turningTicks >= TURN_DURATION) {
                this.lastDir = facing();
                this.turningTicks = 0;
            }
            this.setChanged();
            this.setDispatchable();
        }
        if (this.moveDir != MovingDirection.NONE) {
            this.movingTicks += 1;
            if (this.movingTicks >= MOVE_DURATION) {
                this.moveDir = MovingDirection.NONE;
                this.movingTicks = 0;
            }
            this.setChanged();
            this.setDispatchable();
        }
        if (this.level != null && !this.level.isClientSide && this.executor != null) {
            this.scheduledMove = null;
            this.executor.tick(str -> this.status = str);
            if (this.scheduledMove != null) {
                BlockState state = this.getBlockState();
                CompoundTag data = this.save(new CompoundTag());
                data.putInt("x", this.scheduledMove.getX());
                data.putInt("y", this.scheduledMove.getY());
                data.putInt("z", this.scheduledMove.getZ());
                this.level.setBlock(this.worldPosition, Blocks.AIR.defaultBlockState(), 3);
                this.level.setBlock(this.scheduledMove, state, 3);
                BlockEntity be = this.level.getBlockEntity(this.scheduledMove);
                if (be != null) {
                    be.load(data);
                    be.setChanged();
                    if (be instanceof BlockEntityBase base) base.setDispatchable();
                }
                return;
            }
            this.setChanged();
        }
    }
    
    public Direction facing() {
        if (this.cachedFacing == null) {
            this.cachedFacing = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        }
        return this.cachedFacing;
    }
    
    public void turnLeft() {
        this.turnTo(facing(), facing().getClockWise());
    }
    
    public void turnRight() {
        this.turnTo(facing(), facing().getCounterClockWise());
    }
    
    private void turnTo(Direction facing, Direction newDir) {
        if (this.level != null && !this.level.isClientSide) {
            this.lastDir = facing;
            this.level.setBlock(this.worldPosition, this.getBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, newDir), 3);
            this.cachedFacing = null;
            this.setChanged();
            this.setDispatchable();
        }
    }
    
    public boolean move(MovingDirection dir) {
        if (dir == MovingDirection.NONE) return false;
        if (this.level != null && !this.level.isClientSide) {
            BlockPos target = dir.target(this.worldPosition, this.facing());
            if (!this.level.isInWorldBounds(target)) return false;
            BlockState currentState = this.level.getBlockState(target);
            if (!currentState.isAir()) return false;
            this.moveDir = dir;
            this.movingTicks = 0;
            this.scheduledMove = target.immutable();
            this.setChanged();
            this.setDispatchable();
            return true;
        } else {
            return false;
        }
    }

    private void addClientValues(CompoundTag nbt) {
        if (this.lastDir != null) nbt.putInt("LastDir", this.lastDir.get2DDataValue());
        nbt.putInt("TurningTicks", this.turningTicks);
        nbt.putInt("MoveDir", this.lastDir.ordinal());
        nbt.putInt("MovingTicks", this.movingTicks);
    }
    
    private void loadClientValues(CompoundTag nbt) {
        this.lastDir = nbt.contains("LastDir") ? Direction.from2DDataValue(nbt.getInt("LastDir")) : null;
        this.turningTicks = nbt.getInt("TurningTicks");
        this.moveDir = MovingDirection.values()[nbt.getInt("MoveDir")];
        this.movingTicks = nbt.getInt("MovingTicks");
    }
    
    @Nonnull
    @Override
    public CompoundTag save(@Nonnull CompoundTag nbt) {
        if (this.executor != null) nbt.put("Executor", this.executor.save());
        nbt.putString("Status", this.status == null ? "" : this.status);
        this.addClientValues(nbt);
        return super.save(nbt);
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        if (this.executor != null) this.executor.load(nbt.getCompound("Executor"));
        String statusStr = nbt.getString("Status");
        this.status = statusStr.isEmpty() ? null : this.status;
        this.loadClientValues(nbt);
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        if (this.level != null && !this.level.isClientSide) {
            this.addClientValues(nbt);
        }
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag nbt) {
        super.handleUpdateTag(nbt);
        if (this.level != null && this.level.isClientSide) {
            this.loadClientValues(nbt);
        }
    }

    // Getters

    @Nullable
    public String getStatus() {
        return status;
    }

    @Nullable
    public Direction getLastDir() {
        return lastDir;
    }

    public int getTurningTicks() {
        return turningTicks;
    }

    public MovingDirection getMoveDir() {
        return moveDir;
    }

    public int getMovingTicks() {
        return movingTicks;
    }
}

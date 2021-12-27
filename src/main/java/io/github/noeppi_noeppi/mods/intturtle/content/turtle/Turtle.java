package io.github.noeppi_noeppi.mods.intturtle.content.turtle;

import io.github.noeppi_noeppi.libx.base.tile.BlockEntityBase;
import io.github.noeppi_noeppi.libx.base.tile.TickableBlock;
import io.github.noeppi_noeppi.libx.inventory.BaseItemStackHandler;
import io.github.noeppi_noeppi.mods.intturtle.syscall.movement.ScMove;
import io.github.noeppi_noeppi.mods.intturtle.syscall.movement.ScTurn;
import io.github.noeppi_noeppi.mods.intturtle.util.MovingDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Turtle extends BlockEntityBase implements TickableBlock {
    
    private final TurtleExecutor executor;
    
    private final BaseItemStackHandler inventory = BaseItemStackHandler.builder(2)
            .contentsChanged(() -> {
                this.setChanged();
                this.setDispatchable();
            }).build();
    
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
        int instructionsPerTick = 1;
        if (state.getBlock() instanceof TurtleBlock turtleBlock) instructionsPerTick = turtleBlock.instructionsPerTick;
        this.executor = new TurtleExecutor(this, instructionsPerTick);
    }

    public void startProgram(long[] memory) {
        this.status = null;
        this.executor.start(memory);
        this.setChanged();
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
            if (this.turningTicks >= ScTurn.TURN_DURATION) {
                this.lastDir = facing();
                this.turningTicks = 0;
            }
            this.setChanged();
            this.setDispatchable();
        }
        if (this.moveDir != MovingDirection.NONE) {
            this.movingTicks += 1;
            if (this.movingTicks >= ScMove.MOVE_DURATION) {
                this.moveDir = MovingDirection.NONE;
                this.movingTicks = 0;
            }
            this.setChanged();
            this.setDispatchable();
        }
        if (this.level != null && !this.level.isClientSide) {
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
    
    public BlockPos targetPos(MovingDirection dir) {
        return dir.target(this.worldPosition, this.facing());
    }
    
    public boolean canReach(BlockPos pos) {
        if (this.worldPosition.equals(pos)) {
            return true;
        } else if (this.worldPosition.getX() == pos.getX() && this.worldPosition.getZ() == pos.getZ()) {
            return Math.abs(this.worldPosition.getY() - pos.getY()) <= 1;
        } else {
            return this.worldPosition.relative(this.facing()).equals(pos);
        }
    }
    
    public boolean canReach(BlockPos pos, Direction face) {
        if (this.worldPosition.equals(pos)) {
            return true;
        } else if (this.worldPosition.getX() == pos.getX() && this.worldPosition.getZ() == pos.getZ()) {
            if (this.worldPosition.getY() + 1 == pos.getY()) {
                return face == Direction.DOWN;
            } else if (this.worldPosition.getY() - 1 == pos.getY()) {
                return face == Direction.UP;
            } else {
                return false;
            }
        } else {
            return this.worldPosition.relative(this.facing()).equals(pos) && face == this.facing().getOpposite();
        }
    }

    private void addClientValues(CompoundTag nbt) {
        if (this.lastDir != null) nbt.putInt("LastDir", this.lastDir.get2DDataValue());
        nbt.putInt("TurningTicks", this.turningTicks);
        nbt.putInt("MoveDir", this.moveDir.ordinal());
        nbt.putInt("MovingTicks", this.movingTicks);
        nbt.put("Inventory", this.inventory.serializeNBT());
    }
    
    private void loadClientValues(CompoundTag nbt) {
        this.lastDir = nbt.contains("LastDir") ? Direction.from2DDataValue(nbt.getInt("LastDir")) : null;
        this.turningTicks = nbt.getInt("TurningTicks");
        this.moveDir = MovingDirection.values()[nbt.getInt("MoveDir")];
        this.movingTicks = nbt.getInt("MovingTicks");
        this.inventory.deserializeNBT(nbt.getCompound("Inventory"));
    }
    
    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("Executor", this.executor.save());
        nbt.putString("Status", this.status == null ? "" : this.status);
        this.addClientValues(nbt);
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.executor.load(nbt.getCompound("Executor"));
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

    public BaseItemStackHandler getInventory() {
        return inventory;
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

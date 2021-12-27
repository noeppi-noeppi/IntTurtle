package io.github.noeppi_noeppi.mods.intturtle.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public enum MovingDirection {
    
    NONE(0, 0),
    FORWARD(0, 1),
    UP(1, 0),
    DOWN(-1, 0);
    
    public final double factorY;
    public final double factorZ;

    MovingDirection(double factorY, double factorZ) {
        this.factorY = factorY;
        this.factorZ = factorZ;
    }
    
    public BlockPos target(BlockPos pos, Direction facing) {
        return switch (this) {
            case NONE -> pos.immutable();
            case FORWARD -> pos.immutable().relative(facing);
            case UP -> pos.immutable().above();
            case DOWN -> pos.immutable().below();
        };
    }
    
    public static MovingDirection targetFromMemory(long value) {
        if (value == 1) {
            return MovingDirection.UP;
        } else if (value == 2) {
            return MovingDirection.DOWN;
        } else {
            return MovingDirection.FORWARD;
        }
    }
}

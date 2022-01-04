package io.github.noeppi_noeppi.mods.intturtle.util;

import net.minecraft.core.Direction;

public class IntCodeEnums {
    
    public static Direction getDirection(int id) {
        if (id < 4) {
            return Direction.from2DDataValue(id);
        } else {
            return id == 4 ? Direction.UP : Direction.DOWN;
        }
    }
}

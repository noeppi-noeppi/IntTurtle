package io.github.noeppi_noeppi.mods.intturtle.util;

import java.util.Locale;

public class Util {
    
    public static String capitalize(String str) {
        if (str.isEmpty()) {
            return "";
        } else {
            return str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1);
        }
    }
}

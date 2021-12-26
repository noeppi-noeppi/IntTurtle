package io.github.noeppi_noeppi.mods.intturtle.syscall;

import io.github.noeppi_noeppi.intturtle.engine.IntCodeException;

import java.util.HashMap;
import java.util.Map;

public class SystemCalls {
    
    private static final Map<Integer, SystemCall> sysCalls = new HashMap<>();

    public static synchronized void register(int id, SystemCall call) {
        if (sysCalls.containsKey(id)) throw new IllegalStateException("Dynamic Object type registered twice: " + id);
        sysCalls.put(id, call);
    }
    
    public static SystemCall getSysCall(long id) throws IntCodeException {
        if (id >= 0 && id <= Integer.MAX_VALUE && sysCalls.containsKey((int) id)) {
            return sysCalls.get((int) id);
        } else {
            throw new IntCodeException("Unknown system call: " + id);
        }
    }
}

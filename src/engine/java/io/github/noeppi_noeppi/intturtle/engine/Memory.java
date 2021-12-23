package io.github.noeppi_noeppi.intturtle.engine;

import com.mojang.serialization.Codec;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.LongStream;

public class Memory {
    
    private static final int BLOCK_SIZE = 64;
    
    public static final Codec<Memory> CODEC = Codec.LONG_STREAM.xmap(Memory::new, Memory::storageStream);
    
    private final ArrayList<Long> memory;
    
    @Nullable
    private long[] storage = null;
    
    public Memory() {
        this.memory = new ArrayList<>();
        
    }
    
    public Memory(long[] data) {
        this.memory = new ArrayList<>(data.length);
        for (long l : data) this.memory.add(l);
    }
    
    private Memory(LongStream data) {
        this.storage = data.toArray();
        this.memory = new ArrayList<>(this.storage.length);
        for (long l : this.storage) this.memory.add(l);
    }
    
    public long get(int idx) throws IntCodeException {
        if (idx < 0) throw new IntCodeException("negative memory read", idx);
        if (idx >= memory.size()) return 0;
        return memory.get(idx);
    }

    public long get(long idx) throws IntCodeException {
        if (idx < 0) throw new IntCodeException("negative memory read", idx);
        if (idx > Integer.MAX_VALUE) throw new IntCodeException("memory overflow", idx);
        if (idx >= memory.size()) return 0;
        return memory.get((int) idx);
    }
    
    public void set(int idx, long value) throws IntCodeException {
        if (idx < 0) throw new IntCodeException("negative memory write", idx);
        growIfNeeded(idx);
        this.memory.set(idx, value);
        this.storage = null;
    }

    public void set(long idx, long value) throws IntCodeException {
        if (idx < 0) throw new IntCodeException("negative memory write", idx);
        if (idx > Integer.MAX_VALUE) throw new IntCodeException("memory overflow", idx);
        growIfNeeded((int) idx);
        this.memory.set((int) idx, value);
        this.storage = null;
    }
    
    private void growIfNeeded(int idx) {
        while (memory.size() <= idx) {
            memory.ensureCapacity(memory.size() + BLOCK_SIZE);
            for (int i = 0; i < BLOCK_SIZE; i++) memory.add(0l);
        }
    }
    
    private LongStream storageStream() {
        if (this.storage == null) {
            int limit = this.memory.size();
            while (limit > 0 && this.memory.get(limit - 1) == 0l) {
                limit -= 1;
            }
            this.storage = this.memory.stream().mapToLong(l -> l).limit(limit).toArray();
        }
        return Arrays.stream(this.storage);
    }
}

package io.github.noeppi_noeppi.mods.intturtle.dot;

import io.github.noeppi_noeppi.mods.intturtle.content.turtle.Turtle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class DynamicObjects {
    
    private final Turtle turtle;
    
    private DynamicObject<?, ?> registerX = DynamicObject.EMPTY;
    private DynamicObject<?, ?> registerA = DynamicObject.EMPTY;
    private DynamicObject<?, ?> registerB = DynamicObject.EMPTY;
    private final Map<Integer, DynamicObject<?, ?>> storageMap = new HashMap<>();

    public DynamicObjects(Turtle turtle) {
        this.turtle = turtle;
    }

    @Nullable
    public <T> T get(Class<T> cls) {
        return this.get(this.registerX, cls);
    }
    
    @Nullable
    public <T> T getA(Class<T> cls) {
        return this.get(this.registerA, cls);
    }

    @Nullable
    public <T> T getB(Class<T> cls) {
        return this.get(this.registerB, cls);
    }
    
    @Nullable
    public <T> T get(int id, Class<T> cls) {
        return this.get(this.storageMap.get(id), cls);
    }
    
    @Nullable
    private <T> T get(@Nullable DynamicObject<?, ?> obj, Class<T> cls) {
        if (obj == null || obj == DynamicObject.EMPTY) return null;
        Level level = this.turtle.getLevel();
        if (cls.isAssignableFrom(obj.targetClass) && level instanceof ServerLevel serverLevel && obj.valid(turtle, serverLevel)) {
            //noinspection unchecked
            return (T) obj.get(turtle, serverLevel);
        }
        return null;
    }
    
    public void set(DynamicObject<?, ?> obj) {
        this.registerX = obj;
    }

    public void setA(DynamicObject<?, ?> obj) {
        this.registerA = obj;
    }

    public void setB(DynamicObject<?, ?> obj) {
        this.registerB = obj;
    }
    
    public long allocate(DynamicObject<?, ?> obj) {
        int id = 0;
        while (this.storageMap.containsKey(id)) id += 1;
        this.storageMap.put(id, obj);
        return id;
    }
    
    public void free(long id) {
        if (id >= Integer.MIN_VALUE && id <= Integer.MAX_VALUE) {
            this.storageMap.remove((int) id);
        }
    }
    
    public void reset() {
        this.registerX = DynamicObject.EMPTY;
        this.registerA = DynamicObject.EMPTY;
        this.registerB = DynamicObject.EMPTY;
        this.storageMap.clear();
    }
    
    public CompoundTag save() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("X", DotManager.save(this.registerX));
        nbt.put("A", DotManager.save(this.registerA));
        nbt.put("B", DotManager.save(this.registerB));
        
        CompoundTag mapTag = new CompoundTag();
        for (Map.Entry<Integer, DynamicObject<?, ?>> entry : this.storageMap.entrySet()) {
            mapTag.put(Integer.toString(entry.getKey()), DotManager.save(entry.getValue()));
        }
        nbt.put("M", mapTag);
        
        return nbt;
    }
    
    public void load(CompoundTag nbt) {
        this.registerX = DotManager.load(nbt.getCompound("X"));
        this.registerA = DotManager.load(nbt.getCompound("A"));
        this.registerB = DotManager.load(nbt.getCompound("B"));
        
        this.storageMap.clear();
        CompoundTag mapTag = nbt.getCompound("M");
        for (String key : mapTag.getAllKeys()) {
            try {
                this.storageMap.put(Integer.parseInt(key), DotManager.load(mapTag.getCompound(key)));
            } catch (NumberFormatException e) {
                //
            }
        }
    }
}

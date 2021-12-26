package io.github.noeppi_noeppi.mods.intturtle.dot;

import io.github.noeppi_noeppi.mods.intturtle.IntTurtle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class DotManager {
    
    private static final ResourceLocation EMPTY_ID = new ResourceLocation("minecraft", "empty");
    
    private static final Map<ResourceLocation, DotSerializer<?, ?>> idMap = new HashMap<>();
    private static final Map<Class<? extends DynamicObject<?, ?>>, DotSerializer<?, ?>> clsMap = new HashMap<>();
    private static final Map<Class<? extends DynamicObject<?, ?>>, ResourceLocation> clsIdMap = new HashMap<>();

    public static synchronized <S, T extends DynamicObject<S, ?>> void register(ResourceLocation id, Class<T> dotCls, DotSerializer<S, T> serializer) {
        if (idMap.containsKey(id) || clsMap.containsKey(dotCls) || clsIdMap.containsKey(dotCls)) throw new IllegalStateException("Dynamic Object type registered twice: " + id + " of " + dotCls);
        idMap.put(id, serializer);
        clsMap.put(dotCls, serializer);
        clsIdMap.put(dotCls, id);
    }
    
    public static CompoundTag save(DynamicObject<?, ?> obj) {
        if (obj == DynamicObject.EMPTY) {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("Id", EMPTY_ID.toString());
            return nbt;
        } else if (clsMap.containsKey(obj.getClass())) {
            //noinspection unchecked
            DotSerializer<Object, DynamicObject<Object, ?>> serializer = (DotSerializer<Object, DynamicObject<Object, ?>>) clsMap.get(obj.getClass());
            CompoundTag nbt = new CompoundTag();
            nbt.putString("Id", clsIdMap.get(obj.getClass()).toString());
            //noinspection unchecked
            nbt.put("Value", serializer.save((DynamicObject<Object, ?>) obj));
            return nbt;
        } else {
            IntTurtle.getInstance().logger.warn("Can't save dynamic object: Unknown type: " + obj.getClass());
            return new CompoundTag();
        }
    }
    
    public static DynamicObject<?, ?> load(CompoundTag nbt) {
        ResourceLocation id = nbt.contains("Id") ? ResourceLocation.tryParse(nbt.getString("Id")) : null;
        if (id == null || EMPTY_ID.equals(id) || !idMap.containsKey(id)) {
            if (!EMPTY_ID.equals(id)) IntTurtle.getInstance().logger.warn("Can't load dynamic object: Unknown id: " + id);
            return DynamicObject.EMPTY;
        } else {
            //noinspection unchecked
            DotSerializer<Object, DynamicObject<Object, ?>> serializer = (DotSerializer<Object, DynamicObject<Object, ?>>) idMap.get(id);
            Tag data = nbt.get("Value");
            if (data == null) {
                if (!EMPTY_ID.equals(id)) IntTurtle.getInstance().logger.warn("Can't load dynamic object: No data: " + id);
                return DynamicObject.EMPTY;
            } else {
                return serializer.load(data);
            }
        }
    }
}

package net.shadowking21.baublemounts.components;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.shadowking21.baublemounts.BaubleMounts;

public class MountComponents {

    public CompoundTag compoundTag;

    public static final DeferredRegister<DataComponentType<?>> COMPONENT_TYPES
            = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, BaubleMounts.MODID);
    public static DeferredHolder<DataComponentType<?>, DataComponentType<MountRecord>> MOUNT_COMPONENTS
            = COMPONENT_TYPES.register("mount_record", () -> new DataComponentType.Builder<MountRecord>()
            .persistent(ComponentsCodec.MOUNT_CODEC)
            .networkSynchronized(ComponentsCodec.BASIC_STREAM_CODEC)
            .build());
}

package net.shadowking21.baublemounts.components;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.shadowking21.baublemounts.BaubleMounts;

import java.util.Objects;
import java.util.UUID;

public class MountComponents {
    public static final DeferredRegister.DataComponents REGISTRAR = DeferredRegister.createDataComponents(BaubleMounts.MODID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MountRecord>> MOUNT_COMPONENTS = REGISTRAR.registerComponentType(
            "basic",
            builder -> builder
                    .networkSynchronized(ComponentsCodec.BASIC_STREAM_CODEC)
    );

}

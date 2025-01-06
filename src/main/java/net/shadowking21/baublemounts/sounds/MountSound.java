package net.shadowking21.baublemounts.sounds;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.shadowking21.baublemounts.BaubleMounts;

public class MountSound {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, BaubleMounts.MODID);
    public static final DeferredHolder<SoundEvent, SoundEvent> MOUNT_SUMMON = SOUND_EVENTS.register(
            "mount_summon",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BaubleMounts.MODID, "mount_summon"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> MOUNT_UNSUMMON = SOUND_EVENTS.register(
            "mount_unsummon",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BaubleMounts.MODID, "mount_unsummon"))
    );
}

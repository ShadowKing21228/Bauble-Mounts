package net.shadowking21.baublemounts.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.shadowking21.baublemounts.Baublemounts;

import static net.shadowking21.baublemounts.Baublemounts.MOD_ID;

public class SoundRegistry {

    static DeferredRegister<SoundEvent> sounds = DeferredRegister.create(MOD_ID, Registries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> mountSummon = sounds.register(new ResourceLocation(MOD_ID, "mount_summon"), () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MOD_ID, "mount_summon")));

    public static final RegistrySupplier<SoundEvent> mountUnsummon = sounds.register(new ResourceLocation(MOD_ID, "mount_unsummon"), () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MOD_ID, "mount_unsummon")));

    public static void init() {
        sounds.register();
    }
}

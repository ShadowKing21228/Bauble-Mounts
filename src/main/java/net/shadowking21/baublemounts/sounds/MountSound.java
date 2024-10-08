package net.shadowking21.baublemounts.sounds;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shadowking21.baublemounts.BaubleMounts;

public class MountSound {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BaubleMounts.MODID);
    public static SoundEvent MOUNT_SUMMON = registerSound("mount_summon");
    public static SoundEvent MOUNT_UNSUMMON = registerSound("mount_unsummon");


    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }


    public static RegistryObject<SoundEvent> registrySoundEvents(SoundEvent soundEvent) {
        return SOUND_EVENTS.register(soundEvent.getLocation().getPath(), () -> soundEvent);
    }

    public static SoundEvent registerSound(String soundName) {
        ResourceLocation location = new ResourceLocation(BaubleMounts.MODID, soundName);
        SoundEvent event = new SoundEvent(location);
        registrySoundEvents(event);
        return event;
    }


}


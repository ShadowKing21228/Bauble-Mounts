package net.shadowking21.baublemounts.sounds;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shadowking21.baublemounts.BaubleMounts;

public class MountSound {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BaubleMounts.MODID);
    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BaubleMounts.MODID, name)));
    }
    public static void register(IEventBus eventBus) {SOUND_EVENTS.register(eventBus);}
    public static final RegistryObject<SoundEvent> MOUNT_SUMMON = registerSoundEvents("mount_summon");
    public static final RegistryObject<SoundEvent> MOUNT_UNSUMMON = registerSoundEvents("mount_unsummon");


}

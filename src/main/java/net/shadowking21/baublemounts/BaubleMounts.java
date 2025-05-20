package net.shadowking21.baublemounts;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.shadowking21.baublemounts.client.TooltipHandler;
import net.shadowking21.baublemounts.components.MountComponents;
import net.shadowking21.baublemounts.events.Events;
import net.shadowking21.baublemounts.items.MountBauble;
import net.shadowking21.baublemounts.items.MountBaubleBroken;
import net.shadowking21.baublemounts.network.SendSpawnEntityC2S;
import net.shadowking21.baublemounts.sounds.MountSound;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import java.util.Locale;

@Mod(BaubleMounts.MODID)
public class BaubleMounts {
    public static final String MODID = "baublemounts";
    private static final Logger LOGGER = LogUtils.getLogger();
    public BaubleMounts(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::setupPackets);
        MountBauble.register(modEventBus);
        MountBaubleBroken.register(modEventBus);
        NeoForge.EVENT_BUS.register(new Events());
        MountSound.SOUND_EVENTS.register(modEventBus);
        MountComponents.COMPONENT_TYPES.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, BMConfig.CONFIG_SPEC);
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> ConfigurationScreen::new);
        if (FMLEnvironment.dist.isClient()) {
            new ClientModEvents().init();
        }
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(MountBauble.BAUBLECOMMON);
            //event.accept(MountBauble.BAUBLEBROKEN);
        }
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public static class ClientModEvents {
        public void init() {
            NeoForge.EVENT_BUS.addListener(this::keyMountSummon);
        }

        public static final KeyMapping MOUNT_SUMMON_KEY = new KeyMapping(
                "key.baublemounts.mountsummon",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "key.categories.baublemounts"
        );

        @SubscribeEvent
        public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
            event.register(MOUNT_SUMMON_KEY);
        }

        public void keyMountSummon(InputEvent.Key event) {
            if (MOUNT_SUMMON_KEY.consumeClick()) {
                PacketDistributor.sendToServer(new SendSpawnEntityC2S.PlayersMountSummonData(true));
            }
        }
    }

    public static ResourceLocation prefix(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name.toLowerCase(Locale.ROOT));
    }
    public void setupPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID).versioned("1.0.0").optional();
        registrar.playToServer(SendSpawnEntityC2S.PlayersMountSummonData.TYPE, SendSpawnEntityC2S.PlayersMountSummonData.STREAM_CODEC, SendSpawnEntityC2S.PlayersMountSummonData::handle);
    }
}

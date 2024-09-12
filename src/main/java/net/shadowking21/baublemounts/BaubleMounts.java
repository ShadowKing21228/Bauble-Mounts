package net.shadowking21.baublemounts;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.shadowking21.baublemounts.events.Events;
import net.shadowking21.baublemounts.items.MountBauble;
import net.shadowking21.baublemounts.items.MountBaubleBroken;
import net.shadowking21.baublemounts.network.ModNetwork;
import net.shadowking21.baublemounts.network.SendSpawnEntityC2S;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.SlotTypeMessage;

import static net.shadowking21.baublemounts.BaubleMounts.ClientModEvents.MOUNT_SUMMON_KEY;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BaubleMounts.MODID)
public class BaubleMounts {

    public static final String MODID = "baublemounts";
    public BaubleMounts() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MountBauble.register(modEventBus);
        MountBaubleBroken.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(new Events());
        ModNetwork.init();
        if (FMLEnvironment.dist.isClient())
        {
            new ClientModEvents().init();
        }
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::sendImc);
    }


    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientProxy {
        @SubscribeEvent
        public static void onClientSetup(final FMLClientSetupEvent event) {
            registerKeys();
        }

        public static KeyMapping registerKeybinding(KeyMapping key) {
            ClientRegistry.registerKeyBinding(key);
            return key;
        }

        public static void registerKeys() {
            registerKeybinding(MOUNT_SUMMON_KEY);
        }

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        public void init() {
            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
            MinecraftForge.EVENT_BUS.addListener(this::keyMountSummon);
        }
        public static final KeyMapping MOUNT_SUMMON_KEY = new KeyMapping(
                "key.baublemounts.mountsummon",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "key.categories.baublemounts"
        );
        @OnlyIn(Dist.CLIENT)
        public void keyMountSummon(InputEvent.KeyInputEvent event)
        {
            if (MOUNT_SUMMON_KEY.consumeClick())
            {
                new SendSpawnEntityC2S().sendToServer();
            }
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    public void sendImc(InterModEnqueueEvent evt) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () ->
                (new SlotTypeMessage.Builder("mountbauble")).priority(10).icon(new ResourceLocation("curios", "item/empty_curio_slot")
        ).build());
    }
}

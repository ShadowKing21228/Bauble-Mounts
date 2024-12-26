package net.shadowking21.baublemounts;

import com.google.common.reflect.Reflection;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.shadowking21.baublemounts.client.TooltipHandler;
import net.shadowking21.baublemounts.components.MountComponents;
import net.shadowking21.baublemounts.components.MountRecord;
import net.shadowking21.baublemounts.events.Events;
import net.shadowking21.baublemounts.items.MountBauble;
import net.shadowking21.baublemounts.network.SendSpawnEntityC2S;
import net.shadowking21.baublemounts.sounds.MountSound;
import net.shadowking21.baublemounts.utils.Utils;
import org.joml.Vector2ic;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Mod(BaubleMounts.MODID)
public class BaubleMounts {
    public static final String MODID = "baublemounts";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static IEventBus ModEventBus;
    public BaubleMounts(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::setupPackets);
        MountBauble.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(new Events());
        MountSound.SOUND_EVENTS.register(modEventBus);
        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(this::onRegisterTooltip);
            new ClientModEvents().init();
        }
        MountComponents.COMPONENT_TYPES.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, BMConfig.CONFIG_SPEC);
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> ConfigurationScreen::new);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(MountBauble.BAUBLECOMMON);
            event.accept(MountBauble.BAUBLEBROKEN);
        }
    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
        @OnlyIn(Dist.CLIENT)
        public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
            event.register(MOUNT_SUMMON_KEY);
        }

        @OnlyIn(Dist.CLIENT)
        public void keyMountSummon(InputEvent.Key event) {
            if (MOUNT_SUMMON_KEY.consumeClick()) {
                PacketDistributor.sendToServer(new SendSpawnEntityC2S.PlayersMountSummonData(true));
            }
        }
    }
    @OnlyIn(Dist.CLIENT)
    public void onRegisterTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
        TooltipHandler.register(event);
    }
    @OnlyIn(Dist.CLIENT)
    public static TooltipHandler.TooltipRender tooltipRender = new TooltipHandler.TooltipRender(null);

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onRenderTooltip(RenderTooltipEvent.Pre event) {
        ItemStack itemStack = event.getItemStack();
        Optional<Entity> optionalEntity;
        Entity entity = null;
        if (itemStack.getItem() == MountBauble.BAUBLECOMMON.get() || itemStack.getItem() == MountBauble.BAUBLEBROKEN.get()) {
            if (Utils.hasMountComponents(itemStack)) {
                CompoundTag compoundTag = itemStack.get(MountComponents.MOUNT_COMPONENTS.get()).compoundTag();
                assert Minecraft.getInstance().player != null;
                if (!compoundTag.isEmpty()) {
                    optionalEntity = EntityType.create(compoundTag, Minecraft.getInstance().player.level());
                    entity = optionalEntity.get();
                    if (BuiltInRegistries.ENTITY_TYPE.getKey(optionalEntity.get().getType()).getNamespace().equals("iceandfire") || BuiltInRegistries.ENTITY_TYPE.getKey(optionalEntity.get().getType()).getNamespace().equals("dragonmounts"))
                        entity = null;
                }
            }
            TooltipHandler.BaubleMountsTooltipComponent tooltipComponent = new TooltipHandler.BaubleMountsTooltipComponent(itemStack, entity);
            tooltipRender.component = tooltipComponent;
            GuiGraphics guiGraphics = event.getGraphics();
            Vector2ic vector2ic = DefaultTooltipPositioner.INSTANCE.positionTooltip(guiGraphics.guiWidth(), guiGraphics.guiHeight(), event.getX(), event.getY(), 0, 10);
            tooltipRender.renderImage(event.getFont(), vector2ic.x(), vector2ic.y(), guiGraphics);
            event.setCanceled(true);
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

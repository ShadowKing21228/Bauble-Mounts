package net.shadowking21.baublemounts;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import net.shadowking21.baublemounts.client.TooltipHandler;
import net.shadowking21.baublemounts.events.Events;
import net.shadowking21.baublemounts.items.MountBauble;
import net.shadowking21.baublemounts.items.MountBaubleBroken;
import net.shadowking21.baublemounts.network.ModNetwork;
import net.shadowking21.baublemounts.network.SendSpawnEntityC2S;
import net.shadowking21.baublemounts.sounds.MountSound;
import org.joml.Vector2ic;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BaubleMounts.MODID)
public class BaubleMounts {

    public static final String MODID = "baublemounts";
    public BaubleMounts() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onRegisterTooltip);
        MountBauble.register(modEventBus);
        MountBaubleBroken.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        MinecraftForge.EVENT_BUS.register(new Events());
        ModNetwork.init();
        MountSound.register(modEventBus);
        if (FMLEnvironment.dist.isClient())
        {
            new ClientModEvents().init();
        }
    }
    private void addCreative (BuildCreativeModeTabContentsEvent event ) {
        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(MountBauble.BAUBLECOMMON);
            event.accept(MountBaubleBroken.BAUBLEBROKEN);
        }
    }
    private void commonSetup(final FMLCommonSetupEvent event) {
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

        @SubscribeEvent
        @OnlyIn(Dist.CLIENT)
        public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
            event.register(MOUNT_SUMMON_KEY);
        }

        @OnlyIn(Dist.CLIENT)
        public void keyMountSummon(InputEvent.Key event)
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
    public void onRegisterTooltip (RegisterClientTooltipComponentFactoriesEvent event)
    {
        TooltipHandler.register(event);
    }
    public static TooltipHandler.TooltipRender tooltipRender = new TooltipHandler.TooltipRender(null);
    @SubscribeEvent
    public void onRenderTooltip (RenderTooltipEvent.Pre event)
    {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.getItem() == MountBauble.BAUBLECOMMON.get() || itemStack.getItem() == MountBaubleBroken.BAUBLEBROKEN.get())
        {
            Entity entity = null;
            if (itemStack.hasTag() && itemStack.getTag().contains("Mount") && !itemStack.getTag().getCompound("Mount").isEmpty())
            {
                CompoundTag compoundTag = itemStack.getTag().getCompound("Mount");
                Optional<Entity> d1 = EntityType.create(compoundTag, Minecraft.getInstance().player.level());
                if(d1.isPresent()) {
                    entity = d1.get();
                    if (ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).getNamespace().equals("iceandfire") || ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).getNamespace().equals("dragonmounts"))
                        entity = null;
                }
            }

            TooltipHandler.BaubleMountsTooltipComponent tooltipComponent = new TooltipHandler.BaubleMountsTooltipComponent(itemStack, entity);
            GuiGraphics guiGraphics = event.getGraphics();

            Vector2ic vector2ic = DefaultTooltipPositioner.INSTANCE.positionTooltip(guiGraphics.guiWidth(), guiGraphics.guiHeight(), event.getX(), event.getY(), 0, 10);
            tooltipRender.component = tooltipComponent;
            tooltipRender.renderImage(event.getFont(), vector2ic.x(), vector2ic.y(), guiGraphics);
            event.setCanceled(true);
        }
    }
}

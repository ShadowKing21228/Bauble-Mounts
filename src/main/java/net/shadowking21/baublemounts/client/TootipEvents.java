package net.shadowking21.baublemounts.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.shadowking21.baublemounts.BaubleMounts;
import net.shadowking21.baublemounts.components.MountComponents;
import net.shadowking21.baublemounts.items.MountBauble;
import net.shadowking21.baublemounts.utils.Utils;
import org.joml.Vector2ic;

import java.util.Optional;

import static net.shadowking21.baublemounts.client.TooltipHandler.getTooltipRender;

@EventBusSubscriber(modid = BaubleMounts.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class TootipEvents {
    @SubscribeEvent
    public static void onRenderTooltip(RenderTooltipEvent.Pre event) {
        ItemStack itemStack = event.getItemStack();
        Optional<Entity> optionalEntity;
        Entity entity = null;
        if (itemStack.getItem() == MountBauble.BAUBLECOMMON.get() /*|| itemStack.getItem() == MountBauble.BAUBLEBROKEN.get()*/) {
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
            getTooltipRender().component = tooltipComponent;
            GuiGraphics guiGraphics = event.getGraphics();
            Vector2ic vector2ic = DefaultTooltipPositioner.INSTANCE.positionTooltip(guiGraphics.guiWidth(), guiGraphics.guiHeight(), event.getX(), event.getY(), 0, 10);
            getTooltipRender().renderImage(event.getFont(), vector2ic.x(), vector2ic.y(), guiGraphics);
            event.setCanceled(true);
        }
    }
}

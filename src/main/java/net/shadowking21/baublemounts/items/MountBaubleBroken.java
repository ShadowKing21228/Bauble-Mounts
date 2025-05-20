package net.shadowking21.baublemounts.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.shadowking21.baublemounts.BaubleMounts;
import net.shadowking21.baublemounts.components.MountComponents;
import net.shadowking21.baublemounts.components.MountRecord;
import net.shadowking21.baublemounts.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class MountBaubleBroken {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(BaubleMounts.MODID);

    public static final DeferredItem<Item> BAUBLEBROKEN = MountBauble.ITEMS.register("mount_bauble_broken", () -> new Item(new Item.Properties().stacksTo(1).component(MountComponents.MOUNT_COMPONENTS, MountRecord.DEFAULT)){
        @Override
        public void appendHoverText(ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add(Component.translatable("tooltip.baublemounts.shiftdescbroken"));
            } else {
                tooltipComponents.add(Component.translatable("tooltip.baublemounts.desc"));
            }
            if (Utils.hasMountComponents(stack) && context.level() != null)
            {
                Optional<Entity> entityMount = EntityType.create(stack.get(MountComponents.MOUNT_COMPONENTS.get()).compoundTag(), context.level());
                if (entityMount.isPresent()) {
                    String d = entityMount.get().getDisplayName().getString();
                    if (d.contains("[")) d = d.replace("[", " ");
                    if (d.contains("]")) d = d.replace("]", " ");
                    tooltipComponents.add(Component.translatable("tooltip.baublemounts.getname", d));
                }
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}

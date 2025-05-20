package net.shadowking21.baublemounts.events;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.shadowking21.baublemounts.BMConfig;
import net.shadowking21.baublemounts.components.MountComponents;
import net.shadowking21.baublemounts.components.MountRecord;
import net.shadowking21.baublemounts.items.MountBauble;
import net.shadowking21.baublemounts.items.MountBaubleBroken;
import net.shadowking21.baublemounts.utils.Utils;

import java.util.Optional;

public class Events {
    //@SubscribeEvent
    //public void onBaubleClick(PlayerInteractEvent.RightClickBlock event) {
    //    if (event.getEntity().level().isClientSide) return;
    //    if (event.getEntity() instanceof ServerPlayer player)
    //    {
    //        MountBauble.spawnMount(player, event.getItemStack(), event.getHitVec().getBlockPos());;
    //    }
    //}
    @SubscribeEvent
    public void onMountDead(LivingDeathEvent event)
    {
        if (event.getEntity().level().isClientSide) return;
        LivingEntity entity = event.getEntity();
        for (ServerPlayer player : entity.getServer().getPlayerList().getPlayers()) {
            if (Utils.isMountBaubleEqualOnPlayer(player, entity))
            {
                ItemStack brokenMountBauble = new ItemStack(MountBaubleBroken.BAUBLEBROKEN.get());
                ItemStack mountBauble = Utils.getMountBauble(player);
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.merge(mountBauble.get(MountComponents.MOUNT_COMPONENTS).compoundTag());
                String uuid = mountBauble.get(MountComponents.MOUNT_COMPONENTS).uuid();
                brokenMountBauble.set(MountComponents.MOUNT_COMPONENTS, new MountRecord(compoundTag, uuid));
                if (!BMConfig.CONFIG.brokenBaubleAppearance.get())
                    brokenMountBauble = ItemStack.EMPTY;
                Utils.updateMountBauble(player, brokenMountBauble);
            }
        }
    }

    @SubscribeEvent
    public void onMountBaubleCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack result = event.getCrafting();

        for (int i = 0; i < event.getInventory().getContainerSize(); i++) {
            ItemStack ingredient = event.getInventory().getItem(i);

            if (ingredient.getItem() == MountBaubleBroken.BAUBLEBROKEN.get()) {
                CompoundTag resultTag = new CompoundTag();
                resultTag.merge(ingredient.get(MountComponents.MOUNT_COMPONENTS).compoundTag());
                String uuid = ingredient.get(MountComponents.MOUNT_COMPONENTS).uuid();
                result.set(MountComponents.MOUNT_COMPONENTS, new MountRecord(resultTag, uuid));
                break;
            }
        }
    }
    //@SubscribeEvent
    //public void onItemTooltip(ItemTooltipEvent event) {
    //    ItemStack stack = event.getItemStack();
    //    if (stack.has(MountComponents.MOUNT_COMPONENTS) && event.getEntity() != null) {
    //        CompoundTag compoundTag = stack.get(MountComponents.MOUNT_COMPONENTS.get()).compoundTag();
    //        Optional<Entity> entity = EntityType.create(compoundTag, event.getEntity().level());
    //        if (entity.isPresent())
    //        {
    //            LivingEntity entity1 = (LivingEntity) entity.get();
    //            Component component;
    //            event.getToolTip().add(Component.translatable("tooltip.bauble_mounts.getname"));
    //            event.getToolTip().add(entity1.getDisplayName());
    //            component = Component.literal(entity1.getHealth() + " / " + entity1.getMaxHealth());
    //            event.getToolTip().add(Component.translatable("tooltip.bauble_mounts.gethealth"));
    //            event.getToolTip().add(component);
    //    }
    //    }
    //}

}

package net.shadowking21.baublemounts.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.shadowking21.baublemounts.items.MountBauble;
import net.shadowking21.baublemounts.utils.Utils;

public class Events {
    @SubscribeEvent
    public void onBaubleClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity().level().isClientSide) return;
        if (event.getEntity() instanceof ServerPlayer player)
        {
            MountBauble.spawnMount(player, event.getItemStack(), event.getHitVec().getBlockPos());;
        }
    }

    @SubscribeEvent
    public void onMountDead(LivingDeathEvent event)
    {
        if (event.getEntity().level().isClientSide) return;
        LivingEntity entity = event.getEntity();
        for (ServerPlayer player : entity.getServer().getPlayerList().getPlayers()) {
            if (Utils.isMountBaubleEqualOnPlayer(player, entity))
            {
                ItemStack itemStack = new ItemStack(MountBauble.BAUBLEBROKEN.get());
                ItemStack itemStack1 = Utils.getMountBauble(player);
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.merge(Utils.getMountCompoundTag(itemStack1));
                CompoundTag itemstackTag = Utils.getMountCompoundTag(itemStack).merge(compoundTag);
                Utils.writeCompound(itemStack, itemstackTag);
                Utils.updateMountBauble(player, itemStack);
            }
        }
    }

    @SubscribeEvent
    public void onMountBaubleCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack result = event.getCrafting();

        for (int i = 0; i < event.getInventory().getContainerSize(); i++) {
            ItemStack ingredient = event.getInventory().getItem(i);

            if (ingredient.getItem() == MountBauble.BAUBLEBROKEN.get()) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.merge(Utils.getMountCompoundTag(ingredient));
                CompoundTag resultTag = Utils.getMountCompoundTag(result).merge(compoundTag);
                Utils.writeCompound(result, resultTag);
                break;
            }
        }
    }
    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        if (Utils.getMountCompoundTag(stack).contains("Mount")) {
            CompoundTag compoundTag = Utils.getMountCompoundTag(stack).getCompound("Mount");
            Entity entity = EntityType.create(compoundTag, event.getEntity().level()).get();
            LivingEntity entity1 = (LivingEntity) entity;
            Component component;
            event.getToolTip().add(Component.translatable("tooltip.bauble_mounts.getname"));
            event.getToolTip().add(entity.getDisplayName());
            component = Component.literal( entity1.getHealth() + " / " + entity1.getMaxHealth());
            event.getToolTip().add(Component.translatable("tooltip.bauble_mounts.gethealth"));
            event.getToolTip().add(component);
        }
    }

}

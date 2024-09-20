package net.shadowking21.baublemounts.events;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.shadowking21.baublemounts.items.MountBauble;
import net.shadowking21.baublemounts.items.MountBaubleBroken;
import net.shadowking21.baublemounts.utils.Utils;

import java.awt.*;
import java.util.Optional;

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
                ItemStack itemStack = new ItemStack(MountBaubleBroken.BAUBLEBROKEN.get());
                ItemStack itemStack1 = Utils.getMountBauble(player);
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.merge(itemStack1.getOrCreateTag());
                itemStack.getOrCreateTag().merge(compoundTag);
                Utils.updateMountBauble(player, itemStack);
            }
        }
    }

    @SubscribeEvent
    public void onMountBaubleCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack result = event.getCrafting();

        for (int i = 0; i < event.getInventory().getContainerSize(); i++) {
            ItemStack ingredient = event.getInventory().getItem(i);

            if (ingredient.getItem() == MountBaubleBroken.BAUBLEBROKEN.get()) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.merge(ingredient.getOrCreateTag());
                result.getOrCreateTag().merge(compoundTag);
                break;
            }
        }
    }
    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        if (stack.hasTag() && stack.getTag().contains("Mount") && !stack.getTag().getCompound("Mount").isEmpty()) {
            CompoundTag compoundTag = stack.getTag().getCompound("Mount");
            Optional<Entity> d1 = EntityType.create(compoundTag, event.getEntity().level());
            if(d1.isPresent()) {
                Entity entity = d1.get();
                LivingEntity entity1 = (LivingEntity) entity;
                String d = entity.getDisplayName().getString();
                if (d.contains("[")) d = d.replace("[", " ");
                if (d.contains("]")) d = d.replace("]", " ");

                event.getToolTip().add(Component.translatable("tooltip.bauble_mounts.getname", d));
                event.getToolTip().add(Component.translatable("tooltip.bauble_mounts.gethealth", (entity1.getHealth() + " / " + entity1.getMaxHealth())));
            }
        }
    }

}

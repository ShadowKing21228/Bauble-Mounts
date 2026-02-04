package net.shadowking21.baublemounts.events;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.shadowking21.baublemounts.BMConfig;
import net.shadowking21.baublemounts.registry.ItemRegistry;
import net.shadowking21.baublemounts.utils.MountUtils;

public class MBEvents {

    public static void init()
    {
        onMountDead();
        onRidingPlayerDead();
        onMountBaubleCrafted();
    }

    private static void onMountDead() // If Mount from Bauble died when Player is riding it
    {
        EntityEvent.LIVING_DEATH.register((livingEntity, damageSource) -> {
            var passengerList = livingEntity.getPassengers();

            if (!passengerList.isEmpty())
            {
                for (Entity passenger : passengerList) {

                    if (passenger instanceof Player player && MountUtils.isMountBaubleEqualOnPlayer(player, livingEntity)) {

                        if (BMConfig.config.brokenBaubleAppearance) {
                            livingEntity.setHealth(livingEntity.getMaxHealth() > 10 ? 10 : livingEntity.getMaxHealth()); // Heal Mount to avoid double death processing
                            player.stopRiding();
                            ItemStack mountBauble = MountUtils.baublesHandler.getMountBauble(player);
                            MountUtils.baublesHandler.updateMountBauble(player, MountUtils.changeMountBauble(mountBauble, ItemRegistry.BAUBLEBROKEN.get()));
                        }
                        else
                            MountUtils.baublesHandler.updateMountBauble(player, ItemStack.EMPTY);
                    }
                }
            }
            return EventResult.pass();
        });
    }


    private static void onRidingPlayerDead() // If Player died when he mounted LivEn(Mount) from Bauble
    {
        EntityEvent.LIVING_DEATH.register((livingEntity, damageSource) -> {

            if (livingEntity instanceof Player player && player.getVehicle() != null && MountUtils.isMountBaubleEqualOnPlayer(player, player.getVehicle()))
            {
                player.stopRiding();
                //MountUtils.updateMountData(player.getVehicle(), MountUtils.baublesHandler.getMountBauble(player));
                //player.getVehicle().remove(Entity.RemovalReason.CHANGED_DIMENSION);
            }

            return EventResult.pass();
        });
    }

    private static void onMountBaubleCrafted() {

        PlayerEvent.CRAFT_ITEM.register((player, result, container) -> {

        if (!result.getItem().equals(ItemRegistry.BAUBLECOMMON.get().asItem()))
            return;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack ingredient = container.getItem(i);
            if (ingredient.getItem() == ItemRegistry.BAUBLEBROKEN.get()) {
                result.getOrCreateTag().merge(new CompoundTag().merge(ingredient.getOrCreateTag()));
                result.addTagElement(MountUtils.MOUNT_TAG, ingredient.getTagElement(MountUtils.MOUNT_TAG));
                break;
            }
        }

        });

    }
}

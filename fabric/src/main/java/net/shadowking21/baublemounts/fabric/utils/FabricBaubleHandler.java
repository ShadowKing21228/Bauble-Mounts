package net.shadowking21.baublemounts.fabric.utils;

import dev.emi.trinkets.api.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.shadowking21.baublemounts.utils.IBaublesHandler;

import java.util.Map;

public class FabricBaubleHandler implements IBaublesHandler {

    @Override
    public ItemStack getMountBauble(Player player) {
        return TrinketsApi.getTrinketComponent(player).map(comp -> {
            Map<String, Map<String, TrinketInventory>> groups = comp.getInventory();
            Map<String, TrinketInventory> legsGroup = groups.get("legs");

            if (legsGroup != null) {
                TrinketInventory slotInventory = legsGroup.get("mountbauble");

                if (slotInventory != null) {
                    return slotInventory.getItem(0);
                }
            }
            return ItemStack.EMPTY;
        }).orElse(ItemStack.EMPTY);
    }

    @Override
    public void updateMountBauble(Player player, ItemStack newMountBauble) {
        TrinketsApi.getTrinketComponent(player).ifPresent(comp -> {
            comp.forEach((slotReference, itemStack) -> {
                SlotType slotType = slotReference.inventory().getSlotType();
                if (slotType.getName().equals("mountbauble") &&
                        slotType.getGroup().equals("legs")) {

                    slotReference.inventory().setItem(0, newMountBauble);
                }
            });
        });

    }

}

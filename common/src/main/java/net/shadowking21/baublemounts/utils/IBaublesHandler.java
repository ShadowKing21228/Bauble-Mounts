package net.shadowking21.baublemounts.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IBaublesHandler {

    ItemStack getMountBauble(Player player);

    void updateMountBauble(Player player, ItemStack newMountBauble);
}
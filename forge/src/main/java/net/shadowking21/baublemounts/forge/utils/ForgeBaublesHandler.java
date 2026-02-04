package net.shadowking21.baublemounts.forge.utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.shadowking21.baublemounts.utils.IBaublesHandler;
import net.shadowking21.baublemounts.utils.MountUtils;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.concurrent.atomic.AtomicReference;

public class ForgeBaublesHandler implements IBaublesHandler {

    @Override
    public ItemStack getMountBauble(Player player) {
        LazyOptional<ICuriosItemHandler> abc = CuriosApi.getCuriosInventory(player);
        AtomicReference<ItemStack> stackInSlot = new AtomicReference<>(ItemStack.EMPTY);

        abc.ifPresent(s -> {
            for (int i = 0; i < s.getEquippedCurios().getSlots(); i++) {
                for (Item mountBauble : MountUtils.mountBaubles.get()) {
                    if (mountBauble == s.getEquippedCurios().getStackInSlot(i).getItem() /* || ItemRegistry.VEHICLECOMMON.get() == s.getEquippedCurios().getStackInSlot(i).getItem() */) {
                        stackInSlot.set(s.getEquippedCurios().getStackInSlot(i));
                        break;
                    }
                }
            }
        });

        return stackInSlot.get();
    }

    @Override
    public void updateMountBauble(Player player, ItemStack newMountBauble) {
        LazyOptional<ICuriosItemHandler> abc = CuriosApi.getCuriosInventory(player);
        abc.ifPresent(s -> {
            for (int i = 0; i < s.getEquippedCurios().getSlots(); i++) {

                for (Item mountBauble : MountUtils.mountBaubles.get()) {

                    if (mountBauble == s.getEquippedCurios().getStackInSlot(i).getItem()) {
                        s.getEquippedCurios().setStackInSlot(i, newMountBauble);
                        break;
                    }

                }
            }
        });
    }
}

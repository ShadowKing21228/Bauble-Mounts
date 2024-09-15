package net.shadowking21.baublemounts.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.shadowking21.baublemounts.items.MountBauble;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.concurrent.atomic.AtomicReference;

public class Utils {
    public static ItemStack getMountBauble(Player player) {
        LazyOptional<ICuriosItemHandler> abc = CuriosApi.getCuriosInventory(player);
        AtomicReference<ItemStack> stackInSlot = new AtomicReference<>(ItemStack.EMPTY);
        abc.ifPresent(s ->{
            for (int i = 0; i < s.getEquippedCurios().getSlots(); i++)
            {
                if (MountBauble.BAUBLECOMMON.get() == s.getEquippedCurios().getStackInSlot(i).getItem())
                {
                    stackInSlot.set(s.getEquippedCurios().getStackInSlot(i));
                    break;
                }
            }
            });
        return stackInSlot.get();
    }
    public static boolean isMountBaubleEquippedOnPlayer(Player player)
    {
        LazyOptional<ICuriosItemHandler> abc = CuriosApi.getCuriosInventory(player);
        AtomicReference<Boolean> stackInSlot = new AtomicReference<>(false);
        abc.ifPresent(s ->{
            for (int i = 0; i < s.getEquippedCurios().getSlots(); i++)
            {
                if (MountBauble.BAUBLECOMMON.get() == s.getEquippedCurios().getStackInSlot(i).getItem())
                {
                    stackInSlot.set(true);
                    break;
                }
            }
        });
        return stackInSlot.get();
    }
    public static boolean isMountBaubleEqualOnPlayer(Player player, Entity entity)
    {
        LazyOptional<ICuriosItemHandler> abc = CuriosApi.getCuriosInventory(player);
        AtomicReference<Boolean> stackInSlot = new AtomicReference<>(false);
        abc.ifPresent(s ->{
            for (int i = 0; i < s.getEquippedCurios().getSlots(); i++)
            {
                if (MountBauble.BAUBLECOMMON.get() == s.getEquippedCurios().getStackInSlot(i).getItem())
                {
                    if (entity.getUUID().equals(s.getEquippedCurios().getStackInSlot(i).getTag().getCompound("ID").getUUID("ID"))) {
                        stackInSlot.set(true);
                        break;
                    }
                }
            }
        });
        return stackInSlot.get();
    }
    public static void updateMountBauble(Player player, ItemStack newMountBauble) {
        LazyOptional<ICuriosItemHandler> abc = CuriosApi.getCuriosInventory(player);
        abc.ifPresent(s ->{
            for (int i = 0; i < s.getEquippedCurios().getSlots(); i++)
            {
                if (MountBauble.BAUBLECOMMON.get() == s.getEquippedCurios().getStackInSlot(i).getItem())
                {
                    s.getEquippedCurios().setStackInSlot(i, newMountBauble);
                    break;
                }
            }
        });
    }
}

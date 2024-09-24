package net.shadowking21.baublemounts.utils;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.shadowking21.baublemounts.components.ComponentsCodec;
import net.shadowking21.baublemounts.components.MountComponents;
import net.shadowking21.baublemounts.components.MountRecord;
import net.shadowking21.baublemounts.items.MountBauble;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import javax.xml.crypto.Data;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class Utils {
    public static CompoundTag getMountCompoundTag (ItemStack itemStack)
    {
        if (!itemStack.is(MountBauble.BAUBLECOMMON) || !itemStack.is(MountBauble.BAUBLEBROKEN)) return new CompoundTag();
        CompoundTag compoundTag = new CompoundTag();
        boolean has = itemStack.getComponents().has(MountComponents.MOUNT_COMPONENTS.get());
        if (has && itemStack.getComponents().get(MountComponents.MOUNT_COMPONENTS.get()) != null);
        {
            try {
                compoundTag = itemStack.getComponents().get(MountComponents.MOUNT_COMPONENTS.get()).compoundTag();
            }catch (Exception E){
                E.printStackTrace();
            }
        }
        return compoundTag;
    }
    public static void writeCompound (ItemStack itemStack, CompoundTag compoundTag)
    {
        MountRecord record = new MountRecord(compoundTag, "");
        itemStack.getComponents().get(MountComponents.MOUNT_COMPONENTS.get());
        itemStack.set(MountComponents.MOUNT_COMPONENTS, record);
    }
    public static ItemStack getMountBauble(Player player) {
        Optional<ICuriosItemHandler> abc = CuriosApi.getCuriosInventory(player);
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
        Optional<ICuriosItemHandler> abc = CuriosApi.getCuriosInventory(player);
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
        Optional<ICuriosItemHandler> abc = CuriosApi.getCuriosInventory(player);
        AtomicReference<Boolean> stackInSlot = new AtomicReference<>(false);
        abc.ifPresent(s ->{
            for (int i = 0; i < s.getEquippedCurios().getSlots(); i++)
            {
                if (MountBauble.BAUBLECOMMON.get() == s.getEquippedCurios().getStackInSlot(i).getItem())
                {
                    if (
                            entity.getUUID().equals(getMountCompoundTag(s.getEquippedCurios().getStackInSlot(i)).getCompound("ID").getUUID("ID"))
                    ) {
                        stackInSlot.set(true);
                        break;
                    }
                }
            }
        });
        return stackInSlot.get();
    }
    public static void updateMountBauble(Player player, ItemStack newMountBauble) {
        Optional<ICuriosItemHandler> abc = CuriosApi.getCuriosInventory(player);
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

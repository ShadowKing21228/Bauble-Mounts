package net.shadowking21.baublemounts.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.shadowking21.baublemounts.BaubleMounts;
import net.shadowking21.baublemounts.items.MountBauble;
import net.shadowking21.baublemounts.items.VehicleBauble;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Utils {
    public static List<Item> mountBaubles = List.of(MountBauble.BAUBLECOMMON.get(), VehicleBauble.BAUBLEVEHICLE.get());

    public static ItemStack getMountBauble(Player player) {

        LazyOptional<ICuriosItemHandler> abc = CuriosApi.getCuriosInventory(player);
        AtomicReference<ItemStack> stackInSlot = new AtomicReference<>(ItemStack.EMPTY);
        abc.ifPresent(s -> {
            for (int i = 0; i < s.getEquippedCurios().getSlots(); i++) {
                for (Item mountBauble : mountBaubles) {
                    if (mountBauble == s.getEquippedCurios().getStackInSlot(i).getItem() || VehicleBauble.BAUBLEVEHICLE.get() == s.getEquippedCurios().getStackInSlot(i).getItem()) {
                        stackInSlot.set(s.getEquippedCurios().getStackInSlot(i));
                        break;
                    }
                }
            }
        });
        return stackInSlot.get();
    }

    public static boolean isMountBaubleEquippedOnPlayer(Player player) {
        LazyOptional<ICuriosItemHandler> abc = CuriosApi.getCuriosInventory(player);
        AtomicReference<Boolean> stackInSlot = new AtomicReference<>(false);
        abc.ifPresent(s -> {
            for (int i = 0; i < s.getEquippedCurios().getSlots(); i++) {
                for (Item mountBauble : mountBaubles) {
                    if (mountBauble == s.getEquippedCurios().getStackInSlot(i).getItem()) {
                        stackInSlot.set(true);
                        break;
                    }
                }
            }
        });
        return stackInSlot.get();
    }

    public static boolean isMountBauble(Item item) {
        for (Item mountBauble : mountBaubles) {
            if (item.equals(mountBauble)) return true;
        }
        return false;
    }

    public static boolean isMountBaubleEqualOnPlayer(Player player, Entity entity) {
        LazyOptional<ICuriosItemHandler> abc = CuriosApi.getCuriosInventory(player);
        AtomicReference<Boolean> stackInSlot = new AtomicReference<>(false);
        abc.ifPresent(s -> {
            for (int i = 0; i < s.getEquippedCurios().getSlots(); i++) {
                for (Item mountBauble : mountBaubles) {
                    if (entity != null && mountBauble.equals(s.getEquippedCurios().getStackInSlot(i).getItem()) && entity.getUUID().equals(Objects.requireNonNull(s.getEquippedCurios().getStackInSlot(i).getTag()).getCompound("ID").getUUID("ID"))) {
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
        abc.ifPresent(s -> {
            for (int i = 0; i < s.getEquippedCurios().getSlots(); i++) {
                for (Item mountBauble : mountBaubles) {
                    if (mountBauble == s.getEquippedCurios().getStackInSlot(i).getItem()) {
                        s.getEquippedCurios().setStackInSlot(i, newMountBauble);
                        break;
                    }
                }
            }
        });
    }

    public static void recordMountData(Entity vehicle, Player player, ItemStack mountBauble, InteractionHand hand) {
        CompoundTag compoundtag = new CompoundTag();
        CompoundTag compoundtag2 = new CompoundTag();
        compoundtag2.putUUID("ID", vehicle.getUUID());
        vehicle.save(compoundtag);
        ItemStack itemStack = mountBauble.copy();
        itemStack.addTagElement("Mount", compoundtag);
        itemStack.addTagElement("ID", compoundtag2);
        player.setItemInHand(hand, itemStack);
        vehicle.discard();
    }

    public static void updateMountData(Entity vehicle, ItemStack mountBauble) {
        CompoundTag vehicleTag = new CompoundTag();
        vehicle.save(vehicleTag);
        mountBauble.getOrCreateTag().put("Mount", vehicleTag);
    }

    public static void printStackTrace(String str, Throwable s) {
        StringBuilder strBuilder = new StringBuilder(str + " " + s.getMessage());
        for (StackTraceElement stackTraceElement : s.getStackTrace()) {
            strBuilder.append("\t").append(" ").append("at").append(" ").append(stackTraceElement).append("\n");
        }
        str = strBuilder.toString();

        for (Throwable throwable : s.getSuppressed()) {
            printStackTrace(str, throwable);
        }


        Throwable ourCause = s.getCause();
        if (ourCause != null) {
            printStackTrace(str, ourCause);
        }


        BaubleMounts.LOGGER.error(str);

    }
}

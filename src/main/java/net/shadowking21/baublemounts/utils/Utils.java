package net.shadowking21.baublemounts.utils;

import com.google.errorprone.annotations.MustBeClosed;
import com.jcraft.jorbis.Block;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.slot.SlotTypeReference;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.shadowking21.baublemounts.BMConfig;
import net.shadowking21.baublemounts.components.MountComponents;
import net.shadowking21.baublemounts.components.MountRecord;
import net.shadowking21.baublemounts.items.MountBauble;
import net.shadowking21.baublemounts.items.MountBaubleBroken;
import net.shadowking21.baublemounts.items.VehicleBauble;
import net.sixik.sdmuilibrary.client.utils.math.Vector2;
import net.sixik.sdmuilibrary.client.utils.misc.CenterOperators;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Utils {
    public static List<Item> mountBaubles = List.of(MountBauble.BAUBLECOMMON.get()/*, VehicleBauble.BAUBLEVEHICLE.get()*/);
    public static Vector2 getCenterWithPos(Vector2 pos, Vector2 size, CenterOperators.Type centerType, CenterOperators.Method method) {
        switch (centerType) {
            case CENTER_X -> {
                return new Vector2(pos.x + (method.isAbsolute() ? size.x / 3 : size.x / 2), pos.y);
            }
            case CENTER_Y -> {
                return new Vector2(pos.x, pos.y + (method.isAbsolute() ? size.y / 3 : size.y / 2));
            }
            case CENTER_XY -> {
                return new Vector2(pos.x + (method.isAbsolute() ? size.x / 3 : size.x / 2), pos.y + (method.isAbsolute() ? size.y / 3 : size.y / 2));
            }
            default -> {
                return new Vector2(pos.x, pos.y);
            }
        }
    }
    public static boolean isMountBauble(ItemStack itemStack) {
        for (Item item : mountBaubles)
            if (itemStack.getItem().equals(item)) return true;
        return false;
    }
    public static boolean hasMountComponents(ItemStack itemStack)
    {
        if (itemStack == ItemStack.EMPTY)
            return false;
        if (isMountBauble(itemStack)) {
            return !MountRecord.DEFAULT.equals(new MountRecord(itemStack.get(MountComponents.MOUNT_COMPONENTS).compoundTag(), itemStack.get(MountComponents.MOUNT_COMPONENTS).uuid()));
        }
        return false;
    }
    public static ItemStack getMountBauble(Player player) {
        ItemStack stackInSlot = ItemStack.EMPTY;
        for (ItemStack itemStack : AccessoriesCapability.get(player).getContainer(new SlotTypeReference("mountbauble")).getAccessories().getItems()) {
            if (itemStack.is(MountBauble.BAUBLECOMMON.get()) /*|| itemStack.is(VehicleBauble.BAUBLEVEHICLE.get())*/) {
                stackInSlot = itemStack;
                break;
            }
        }
        return stackInSlot;
    }
    public static boolean isMountBaubleEqualOnPlayer(Player player, Entity entity)
    {
        boolean bool = false;
            for (ItemStack itemStack : AccessoriesCapability.get(player).getContainer(new SlotTypeReference("mountbauble")).getAccessories().getItems())
            {
                if ((MountBauble.BAUBLECOMMON.get() == itemStack.getItem() /*|| itemStack.is(VehicleBauble.BAUBLEVEHICLE.get())*/) && Utils.hasMountComponents(itemStack))
                {
                    if (entity.getUUID().equals((UUID.fromString(itemStack.get(MountComponents.MOUNT_COMPONENTS).uuid()))))
                    {
                        bool = true;
                        break;
                    }
                }
            }
        return bool;
    }
    public static void updateMountBauble(Player player, ItemStack newMountBauble) {
        int i = 0;
            for (ItemStack itemStack : AccessoriesCapability.get(player).getContainer(new SlotTypeReference("mountbauble")).getAccessories().getItems())
            {
                if (MountBauble.BAUBLECOMMON.get() == itemStack.getItem())
                {
                    AccessoriesCapability.get(player).getContainer(new SlotTypeReference("mountbauble")).getAccessories().setItem(i, newMountBauble);
                    break;
                }
                i++;
            }
    }
    public static boolean spawnMount(Player player, ItemStack mountBauble, BlockPos blockPos, InteractionHand interactionHand)
    {
        boolean bool = false;
        if (hasMountComponents(mountBauble)) { // Это Маунт Бабл, но есть ли в нём компоненты?
            player.stopRiding();
            CompoundTag mountTag = mountBauble.get(MountComponents.MOUNT_COMPONENTS).compoundTag();
            if (!mountTag.isEmpty()) { // Компоненты есть, на CompoundTag там не нулевой?
                ItemStack itemStack = mountBauble.copy();
                var var = EntityType.create(mountTag, player.level());
                if (var.isPresent()) { // mountTag не нулевой, но уровень правилен?
                    var.get().setPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
                    player.level().addFreshEntity(var.get());
                    itemStack.set(MountComponents.MOUNT_COMPONENTS, MountRecord.DEFAULT); // Убираем МаунтКомпоненты из предмета
                    if (player.isCreative() || !BMConfig.CONFIG.destructionUponRelease.get()) {
                        player.setItemInHand(interactionHand, itemStack); // Возвращаем предмет если в креативе
                    } else {
                        player.setItemInHand(interactionHand, ItemStack.EMPTY); // Уничтожаем если нет
                    }
                    bool = true;
                }
            }
        }
        return bool;
    }
}

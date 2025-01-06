package net.shadowking21.baublemounts.utils;

import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.slot.SlotTypeReference;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.shadowking21.baublemounts.BMConfig;
import net.shadowking21.baublemounts.components.MountComponents;
import net.shadowking21.baublemounts.components.MountRecord;
import net.shadowking21.baublemounts.items.MountBauble;

import java.util.UUID;

public class Utils {
    //public static CompoundTag getMountCompoundTag (ItemStack itemStack)
    //{
    //    if (!itemStack.is(MountBauble.BAUBLECOMMON) || !itemStack.is(MountBauble.BAUBLEBROKEN)) return new CompoundTag();
    //    CompoundTag compoundTag = new CompoundTag();
    //    boolean has = itemStack.getComponents().has(MountComponents.MOUNT_COMPONENTS.get());
    //    if (has && itemStack.getComponents().get(MountComponents.MOUNT_COMPONENTS.get()) != null);
    //    {
    //        try {
    //            compoundTag = itemStack.get(MountComponents.MOUNT_COMPONENTS.get()).compoundTag();
    //        }catch (Exception E){
    //            E.printStackTrace();
    //        }
    //    }
    //    return compoundTag;
    //}
    //public static void writeCompound (ItemStack itemStack, CompoundTag compoundTag)
    //{
    //    MountRecord record = new MountRecord(compoundTag, "");
    //    itemStack.getComponents().get(MountComponents.MOUNT_COMPONENTS.get());
    //    itemStack.set(MountComponents.MOUNT_COMPONENTS, record);
    //}
    //public static void whileMountBaubleEquipped(Player player)
    //{
    //    while (player.isPassenger())
    //    {
    //        ItemStack itemStack = Utils.getMountBauble((player));
    //        if (itemStack == ItemStack.EMPTY)
    //        {
    //            player.getVehicle().discard();
    //        }
    //    }
    //}
    public static boolean hasMountComponents(ItemStack itemStack)
    {
        if (itemStack == ItemStack.EMPTY)
            return false;
        if (itemStack.getItem() == MountBauble.BAUBLECOMMON.get() || itemStack.getItem() == MountBauble.BAUBLEBROKEN.get()) {
            MountRecord mountRecord = new MountRecord(itemStack.get(MountComponents.MOUNT_COMPONENTS).compoundTag(), itemStack.get(MountComponents.MOUNT_COMPONENTS).uuid());
            return !MountRecord.DEFAULT.equals(mountRecord);
        }
        return false;
    }
    public static ItemStack getMountBauble(Player player) {
        ItemStack stackInSlot = ItemStack.EMPTY;
        for (ItemStack itemStack : AccessoriesCapability.get(player).getContainer(new SlotTypeReference("mountbauble")).getAccessories().getItems()) {
            if (itemStack.is(MountBauble.BAUBLECOMMON.get())) {
                stackInSlot = itemStack;
                break;
            }
        }
        return stackInSlot;
    }
    //public static boolean isMountBaubleEquippedOnPlayer(Player player)
    //{
    //    Optional<ICuriosItemHandler> abc = CuriosApi.getCuriosInventory(player);
    //    AtomicReference<Boolean> stackInSlot = new AtomicReference<>(false);
    //    abc.ifPresent(s ->{
    //        for (int i = 0; i < s.getEquippedCurios().getSlots(); i++)
    //        {
    //            if (MountBauble.BAUBLECOMMON.get() == s.getEquippedCurios().getStackInSlot(i).getItem())
    //            {
    //                stackInSlot.set(true);
    //                break;
    //            }
    //        }
    //    });
    //    return stackInSlot.get();
    //}
    public static boolean isMountBaubleEqualOnPlayer(Player player, Entity entity)
    {
        Boolean bool = false;
            for (ItemStack itemStack : AccessoriesCapability.get(player).getContainer(new SlotTypeReference("mountbauble")).getAccessories().getItems())
            {
                if (MountBauble.BAUBLECOMMON.get() == itemStack.getItem() && Utils.hasMountComponents(itemStack))
                {
                    if (entity.getUUID().equals((UUID.fromString(itemStack.get(MountComponents.MOUNT_COMPONENTS.get()).uuid()))))
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
            CompoundTag mountTag = mountBauble.get(MountComponents.MOUNT_COMPONENTS.get()).compoundTag();
            if (!mountTag.isEmpty()) { // Компоненты есть, на CompoundTag там не нулевой?
                ItemStack itemStack = mountBauble.copy();
                var var = EntityType.create(mountTag, player.level(), EntitySpawnReason.MOB_SUMMONED);
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

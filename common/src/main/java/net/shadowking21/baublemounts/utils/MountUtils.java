package net.shadowking21.baublemounts.utils;

import dev.architectury.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.shadowking21.baublemounts.BMConfig;
import net.shadowking21.baublemounts.registry.ItemRegistry;
import net.shadowking21.baublemounts.registry.SoundRegistry;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class MountUtils {

    public static Supplier<List<Item>> mountBaubles = () -> List.of(ItemRegistry.BAUBLECOMMON.get(), ItemRegistry.VEHICLECOMMON.get());

    public static final String MOUNT_TAG = "Mount";

    public static final String UUID_TAG = "ID";

    public static IBaublesHandler baublesHandler = null;

    public static boolean hasMountTag(ItemStack itemstack) {
        if (!itemstack.hasTag())
            return false;

        return itemstack.getTag().getUUID("ID") != null;
    }

    public static boolean isMountBaubleEqualOnPlayer(Player player, Entity entity) {
        ItemStack stack = baublesHandler.getMountBauble(player);

        if (stack != ItemStack.EMPTY && stack.hasTag()) {
            CompoundTag tag = stack.getOrCreateTag();

            if (tag.contains(MountUtils.UUID_TAG, Tag.TAG_INT_ARRAY)) {
                UUID itemUUID = tag.getUUID(MountUtils.UUID_TAG);
                return itemUUID.equals(entity.getUUID());
            }
        }
        return false;
    }

    public static void clearMountTag(ItemStack mountBauble) {
        mountBauble.getOrCreateTag().remove(MOUNT_TAG);
        mountBauble.getOrCreateTag().remove(UUID_TAG);
    }

    public static Entity createMountOn(ItemStack itemStack, Level level, BlockPos pos) {
        AtomicReference<Entity> mountEntity = new AtomicReference<>();
        Optional<Entity> entityType = EntityType.create(itemStack.getTagElement(MOUNT_TAG), level);

        entityType.ifPresent(entity -> {
            entity.setPos(new Vec3(pos.getX(), pos.getY() + 1, pos.getZ()));
            level.addFreshEntity(entity);
            mountEntity.set(entity);
        });

        return mountEntity.get();
    }

    public static Optional<Entity> createEntityType(ItemStack stack, Level level) {
        return EntityType.create(stack.getTag().getCompound(MOUNT_TAG), level);
    }

    public static void spawnRideMount(ServerPlayer player, ItemStack baubleMount) {
        if (player.isPassenger()) {
            player.stopRiding();
        }

        else if (!player.getCooldowns().isOnCooldown(baubleMount.getItem())) {
            var rideMount = createMountOn(baubleMount, player.level(), player.getOnPos());

            if (rideMount != null) {
                player.startRiding(rideMount, true);
                player.getCooldowns().addCooldown(baubleMount.getItem(), BMConfig.config.cooldownValue * 20);

                player.level().playSound(rideMount, rideMount.getOnPos(), SoundRegistry.mountSummon.get(), SoundSource.NEUTRAL, 1f, 1f);
            }
        }
    }

    public static void spawnRideVehicle(ServerPlayer player, ItemStack baubleMount) {
        if (player.isPassenger()) {
            player.stopRiding();
        }

        else if (!player.getCooldowns().isOnCooldown(baubleMount.getItem())) {
            var rideMount = createMountOn(baubleMount, player.level(), player.getOnPos());

            if (rideMount != null) {
                rideMount.setYRot(player.getYRot());
                rideMount.setXRot(player.getXRot());
                player.startRiding(rideMount, true);
                player.getCooldowns().addCooldown(baubleMount.getItem(), BMConfig.config.cooldownValue * 20);

                player.level().playSound(rideMount, rideMount.getOnPos(), SoundRegistry.mountSummon.get(), SoundSource.NEUTRAL, 1f, 1f);
            }
        }
    }
    
    public static boolean isMountBauble(Item item) {
        for (Item mountBauble : MountUtils.mountBaubles.get()) {
            if (item.equals(mountBauble)) return true;
        }
        return false;
    }

    public static void recordMountData(Entity vehicle, Player player, ItemStack mountBauble, InteractionHand hand) {
        CompoundTag mountTag = new CompoundTag();
        vehicle.save(mountTag);

        ItemStack itemStack = mountBauble.copy();
        itemStack.getOrCreateTag().putUUID(UUID_TAG, vehicle.getUUID()); /////////////////////////
        itemStack.addTagElement(MOUNT_TAG, mountTag);

        player.setItemInHand(hand, itemStack);
        vehicle.remove(Entity.RemovalReason.CHANGED_DIMENSION);
    }

    public static void updateMountData(Entity vehicle, ItemStack mountBauble) {
        CompoundTag vehicleTag = new CompoundTag();
        vehicle.save(vehicleTag);
        mountBauble.addTagElement(MOUNT_TAG, vehicleTag);
    }

    public static ItemStack changeMountBauble(ItemStack mountBauble, Item newMountBauble) {
        ItemStack brokenMountBauble = new ItemStack(newMountBauble);
        brokenMountBauble.getOrCreateTag().merge(mountBauble.getOrCreateTag());
        brokenMountBauble.addTagElement(MOUNT_TAG, mountBauble.getTagElement(MOUNT_TAG));
        return brokenMountBauble;
    }
}

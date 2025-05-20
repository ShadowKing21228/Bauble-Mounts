package net.shadowking21.baublemounts.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.shadowking21.baublemounts.BMConfig;
import net.shadowking21.baublemounts.BaubleMounts;
import net.shadowking21.baublemounts.components.MountComponents;
import net.shadowking21.baublemounts.components.MountRecord;
import net.shadowking21.baublemounts.sounds.MountSound;
import net.shadowking21.baublemounts.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static net.shadowking21.baublemounts.BaubleMounts.ClientModEvents.MOUNT_SUMMON_KEY;

public class VehicleBauble {
    /*public static final DeferredItem<Item> BAUBLEVEHICLE = MountBauble.ITEMS.register("vehicle_bauble", () -> new Item(new Item.Properties().stacksTo(1).component(MountComponents.MOUNT_COMPONENTS, MountRecord.DEFAULT)){
        @Override
        public boolean isFoil(ItemStack itemStack) {
            return Utils.hasMountComponents(itemStack);
        }
        @Override
        public @NotNull InteractionResult useOn(UseOnContext pContext)
        {
            if (pContext.getPlayer() instanceof ServerPlayer serverPlayer) {
                if (spawnMount(serverPlayer, pContext.getItemInHand(), pContext.getClickedPos(), pContext.getHand()))
                    return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        }
        @Override
        public void appendHoverText(ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add(Component.translatable("tooltip.baublemounts.vehicleshiftdescbroken"));
            } else {
                tooltipComponents.add(Component.translatable("tooltip.baublemounts.vehicledesc"));
            }
            if (Utils.hasMountComponents(stack) && context.level() != null)
            {
                Optional<Entity> entityMount = EntityType.create(stack.get(MountComponents.MOUNT_COMPONENTS.get()).compoundTag(), context.level());
                if (entityMount.isPresent()) {
                    String d = entityMount.get().getDisplayName().getString();
                    if (d.contains("[")) d = d.replace("[", " ");
                    if (d.contains("]")) d = d.replace("]", " ");
                    tooltipComponents.add(Component.translatable("tooltip.baublemounts.getname", d));
                }
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static boolean spawnMount(ServerPlayer player, ItemStack baubleMount, BlockPos blockPos, InteractionHand interactionHand)
    {
        if (baubleMount.getItem() == VehicleBauble.BAUBLEVEHICLE.get());
        {
            //player.stopRiding();
            CompoundTag mountTag = baubleMount.getOrCreateTag().getCompound("Mount");
            if (!mountTag.isEmpty())
            {
                ItemStack itemStack = baubleMount.copy();
                var var = EntityType.create(mountTag, player.level());
                var.get().setPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
                player.level().addFreshEntity(var.get());
                itemStack.addTagElement("Mount", new CompoundTag());
                if (player.isCreative()) player.setItemInHand(interactionHand, itemStack);
                else if (BMConfig.destructionUponRelease.get())
                    player.setItemInHand(interactionHand, ItemStack.EMPTY);
                else if (!BMConfig.destructionUponRelease.get())
                    player.setItemInHand(interactionHand, itemStack);
                return true;
            }
        }
        return false;
    }
    public static void spawnRideMount(ServerPlayer player, ItemStack baubleMount)
    {
        if (player.isPassenger()) {
            CompoundTag mountTag = baubleMount.getOrCreateTag().getCompound("Mount");
            var var = EntityType.create(mountTag, player.level());
            Entity entity = player.getVehicle();
            if (entity != null && Objects.equals(baubleMount.getTag().getCompound("ID").getUUID("ID"), entity.getUUID())) {
                player.stopRiding();
                baubleMount.getOrCreateTag().getCompound("Mount").merge(mountTag);
                player.level().playSound(entity, var.get().getOnPos(), MountSound.MOUNT_UNSUMMON.get(), SoundSource.NEUTRAL, 1f, 1f);
            }
        }
        else if (!player.getCooldowns().isOnCooldown(baubleMount.getItem()))
        {
            CompoundTag mountTag = baubleMount.getOrCreateTag().getCompound("Mount");
            BlockPos blockPos = player.getOnPos();
            Entity entity = EntityType.create(mountTag, player.level()).get();
            entity.setPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
            entity.setYRot(player.getYRot());
            entity.setXRot(player.getXRot());
            player.level().addFreshEntity(entity);
            player.startRiding(entity, true);
            player.getCooldowns().addCooldown(baubleMount.getItem(),BMConfig.cooldownValue.get() * 20);

            player.level().playSound(entity, entity.getOnPos(), MountSound.MOUNT_SUMMON.get(), SoundSource.NEUTRAL, 1f, 1f);
        }
    }
*/}


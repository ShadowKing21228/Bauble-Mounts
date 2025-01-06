package net.shadowking21.baublemounts.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
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
import java.util.UUID;

import static net.shadowking21.baublemounts.BaubleMounts.ClientModEvents.MOUNT_SUMMON_KEY;

public class MountBauble {
    private static ResourceKey<Item> bmItemId(String name) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(BaubleMounts.MODID, name));
    }
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(BaubleMounts.MODID);
    public static final DeferredItem<Item> BAUBLEBROKEN = ITEMS.register("mount_bauble_broken", () -> new Item(new Item.Properties().setId(bmItemId("mount_bauble_broken")).stacksTo(1).component(MountComponents.MOUNT_COMPONENTS, MountRecord.DEFAULT)){
        @Override
        public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add(Component.translatable("tooltip.baublemounts.shiftdescbroken"));
            } else {
                tooltipComponents.add(Component.translatable("tooltip.baublemounts.desc"));
            }
            if (Utils.hasMountComponents(stack))
            {
                CompoundTag compoundTag = stack.get(MountComponents.MOUNT_COMPONENTS.get()).compoundTag();
                if (context.level() != null) {
                    Optional<Entity> entityMount = EntityType.create(compoundTag, context.level(), EntitySpawnReason.MOB_SUMMONED);
                    if (entityMount.isPresent()) {
                        Entity entity = entityMount.get();
                        LivingEntity livingEntity = (LivingEntity) entity;
                        String d = entity.getDisplayName().getString();
                        if (d.contains("[")) d = d.replace("[", " ");
                        if (d.contains("]")) d = d.replace("]", " ");

                        tooltipComponents.add(Component.translatable("tooltip.baublemounts.getname", d));
                        tooltipComponents.add(Component.translatable("tooltip.baublemounts.gethealth", (livingEntity.getHealth() + " / " + livingEntity.getMaxHealth())));
                    }
                }
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BAUBLECOMMON = ITEMS.register("mount_bauble", () -> new Item(new Item.Properties().setId(bmItemId("mount_bauble")).stacksTo(1).component(MountComponents.MOUNT_COMPONENTS.get(), MountRecord.DEFAULT))
    {
       @Override
       public boolean isFoil(ItemStack itemStack) {
           return Utils.hasMountComponents(itemStack);
       }
        @Override
        public @NotNull InteractionResult useOn(UseOnContext context) {
           ItemStack mountBauble = context.getItemInHand();
           Player player = context.getPlayer();
           BlockPos blockPos = context.getClickedPos();
           InteractionHand interactionHand = context.getHand();
           if (Utils.spawnMount(player, mountBauble, blockPos, interactionHand))
               return InteractionResult.SUCCESS;
           else
               return InteractionResult.FAIL;
       }
        @Override
        public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add(Component.translatable("tooltip.baublemounts.shiftdesc",  MOUNT_SUMMON_KEY.getKey().getDisplayName().getString()));
            } else {
                tooltipComponents.add(Component.translatable("tooltip.baublemounts.desc"));
            }
            if (Utils.hasMountComponents(stack))
            {
                CompoundTag compoundTag = Objects.requireNonNull(stack.get(MountComponents.MOUNT_COMPONENTS.get())).compoundTag();
                Optional<Entity> entityMount = EntityType.create(compoundTag, Objects.requireNonNull(context.level()), EntitySpawnReason.MOB_SUMMONED);
                if (entityMount.isPresent()) {
                    Entity entity = entityMount.get();
                    LivingEntity entity1 = (LivingEntity) entity;
                    String d = entity.getDisplayName().getString();
                    if (d.contains("[")) d = d.replace("[", " ");
                    if (d.contains("]")) d = d.replace("]", " ");
                    tooltipComponents.add(Component.translatable("tooltip.baublemounts.getname", d));
                    tooltipComponents.add(Component.translatable("tooltip.baublemounts.gethealth", (entity1.getHealth() + " / " + entity1.getMaxHealth())));
                }
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
    public static void spawnRideMount(ServerPlayer player, ItemStack baubleMount)
    {
        if (player.isPassenger()) {
            Entity entity = player.getVehicle();
            var entityType = EntityType.create(baubleMount.get(MountComponents.MOUNT_COMPONENTS.get()).compoundTag(), player.level(), EntitySpawnReason.MOB_SUMMONED);
            if (Objects.equals(UUID.fromString(baubleMount.get(MountComponents.MOUNT_COMPONENTS).uuid()), entity.getUUID())) {
                player.level().playSound(entity, entityType.get().getOnPos(), MountSound.MOUNT_UNSUMMON.get(), SoundSource.NEUTRAL, 1f, 1f);
                player.stopRiding();
            }
        }
        else if (!player.getCooldowns().isOnCooldown(baubleMount))
         {
             CompoundTag mountTag = baubleMount.get(MountComponents.MOUNT_COMPONENTS).compoundTag();
             var var = EntityType.create(mountTag, player.level(), EntitySpawnReason.MOB_SUMMONED);
             if (var.isPresent()) {
                 BlockPos blockPos = player.getOnPos();
                 var.get().setPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
                 player.level().addFreshEntity(var.get());
                 player.startRiding(var.get(), true);
                 player.getCooldowns().addCooldown(baubleMount, BMConfig.CONFIG.cooldownValue.get() * 20);
                 player.level().playSound(var.get(), var.get().getOnPos(), MountSound.MOUNT_SUMMON.get(), SoundSource.NEUTRAL, 1f, 1f);
                 //Utils.whileMountBaubleEquipped(player);
             }
        }
    }
}

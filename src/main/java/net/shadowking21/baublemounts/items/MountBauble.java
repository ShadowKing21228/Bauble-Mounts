package net.shadowking21.baublemounts.items;

import net.minecraft.client.gui.screens.Screen;
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

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(BaubleMounts.MODID);

    public static final DeferredItem<Item> BAUBLECOMMON = ITEMS.register("mount_bauble", () -> new Item(new Item.Properties().stacksTo(1).component(MountComponents.MOUNT_COMPONENTS.get(), MountRecord.DEFAULT))
    {
       @Override
       public boolean isFoil(ItemStack itemStack) {
           return Utils.hasMountComponents(itemStack);
       }
        @Override
        public @NotNull InteractionResult useOn(UseOnContext context) {
           if (Utils.spawnMount(context.getPlayer(), context.getItemInHand(), context.getClickedPos(), context.getHand()))
               return InteractionResult.SUCCESS;
           else
               return InteractionResult.FAIL;
       }
        @Override
        public void appendHoverText(ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add(Component.translatable("tooltip.baublemounts.shiftdesc","§e" + MOUNT_SUMMON_KEY.getKey().getDisplayName().getString() + "§r."));
            } else {
                tooltipComponents.add(Component.translatable("tooltip.baublemounts.desc"));
            }
            if (Utils.hasMountComponents(stack) && context.level() != null)
            {
                Optional<Entity> entityMount = EntityType.create(stack.get(MountComponents.MOUNT_COMPONENTS).compoundTag(), context.level());
                if (entityMount.isPresent()) {
                    String d = entityMount.get().getDisplayName().getString();
                    if (d.contains("[")) d = d.replace("[", " ");
                    if (d.contains("]")) d = d.replace("]", " ");
                    var livingEntity = (LivingEntity) entityMount.get();
                    tooltipComponents.add(Component.translatable("tooltip.baublemounts.getname", d));
                    //tooltipComponents.add(Component.translatable("tooltip.baublemounts.gethealth", (livingEntity.getHealth() + " / " + livingEntity.getMaxHealth())));
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
            var entityType = EntityType.create(baubleMount.get(MountComponents.MOUNT_COMPONENTS).compoundTag(), player.level());
            if (Objects.equals(UUID.fromString(baubleMount.get(MountComponents.MOUNT_COMPONENTS).uuid()), entity.getUUID())) {
                player.level().playSound(entity, entityType.get().getOnPos(), MountSound.MOUNT_UNSUMMON.get(), SoundSource.NEUTRAL, 1f, 1f);
                player.stopRiding();
            }
        }
        else if (!player.getCooldowns().isOnCooldown(baubleMount.getItem()))
         {
             CompoundTag mountTag = baubleMount.get(MountComponents.MOUNT_COMPONENTS).compoundTag();
             var var = EntityType.create(mountTag, player.level());
             if (var.isPresent()) {
                 BlockPos blockPos = player.getOnPos();
                 var.get().setPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
                 player.level().addFreshEntity(var.get());
                 player.startRiding(var.get(), true);
                 player.getCooldowns().addCooldown(baubleMount.getItem(), BMConfig.CONFIG.cooldownValue.get() * 20);
                 player.level().playSound(var.get(), var.get().getOnPos(), MountSound.MOUNT_SUMMON.get(), SoundSource.NEUTRAL, 1f, 1f);
                 //Utils.whileMountBaubleEquipped(player);
             }
        }
    }
}

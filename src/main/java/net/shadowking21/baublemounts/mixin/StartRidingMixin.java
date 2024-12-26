package net.shadowking21.baublemounts.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.shadowking21.baublemounts.BMConfig;
import net.shadowking21.baublemounts.components.MountComponents;
import net.shadowking21.baublemounts.components.MountRecord;
import net.shadowking21.baublemounts.items.MountBauble;
import net.shadowking21.baublemounts.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class StartRidingMixin {
    @Shadow public abstract boolean mayInteract(Level level, BlockPos pos);

    public ServerPlayer player = (ServerPlayer)(Object)this;
    @Inject(method = "startRiding(Lnet/minecraft/world/entity/Entity;Z)Z", at = @At("HEAD"), cancellable = true)
    private void onStartRiding(Entity vehicle, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if (vehicle instanceof LivingEntity l_e)
        {
            ItemStack itemstack = player.getMainHandItem();
            if (!itemstack.equals(ItemStack.EMPTY))
            {
                if (!Utils.hasMountComponents(itemstack) && !Utils.isMountBaubleEqualOnPlayer(player, vehicle))
                {
                    //for (String mount : BMConfig.CONFIG.mountList.get()) {
                    //    if (mount.equals(l_e.getEncodeId())) {
                    //        return;
                    //    }
                    //}
                    cir.setReturnValue(false);
                    CompoundTag entityData = new CompoundTag();
                    vehicle.save(entityData);
                    MountRecord mountRecord = new MountRecord(entityData, vehicle.getUUID().toString()); // Объявление компонента в предмет
                    ItemStack itemStack = itemstack.copy();
                    itemStack.set(MountComponents.MOUNT_COMPONENTS, mountRecord);
                    player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
                    vehicle.discard();
                }
            }
        }
    }
}
package net.shadowking21.baublemounts.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.shadowking21.baublemounts.BMConfig;
import net.shadowking21.baublemounts.items.MountBauble;
import net.shadowking21.baublemounts.items.VehicleBauble;
import net.shadowking21.baublemounts.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class StartRidingMixin {

    public ServerPlayer player = (ServerPlayer)(Object)this;
    @Inject(method = "startRiding(Lnet/minecraft/world/entity/Entity;Z)Z", at = @At("HEAD"), cancellable = true)
    private void onStartRiding(Entity vehicle, boolean force, CallbackInfoReturnable<Boolean> cir) {
        InteractionHand hand = player.getUsedItemHand();
        ItemStack itemstack = player.getItemInHand(hand);

        if (Utils.isMountBauble(itemstack.getItem()) && !itemstack.hasFoil() && !Utils.isMountBaubleEqualOnPlayer(player, vehicle)) {

            for (String mount : BMConfig.mountList.get()) {
                if (mount.equals(vehicle.getEncodeId())) {
                    return;
                }
            }
            if (vehicle instanceof LivingEntity && itemstack.is(MountBauble.BAUBLECOMMON.get())) {
                cir.setReturnValue(false);
                Utils.recordMountData(vehicle, player, itemstack, hand);
            } else if (itemstack.is(VehicleBauble.BAUBLEVEHICLE.get())) {
                cir.setReturnValue(false);
                Utils.recordMountData(vehicle, player, itemstack, hand);
            }
        }
    }
}
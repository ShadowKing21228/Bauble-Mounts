package net.shadowking21.baublemounts.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.shadowking21.baublemounts.BMConfig;
import net.shadowking21.baublemounts.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class StopRidingVehicleMixin {
    public Entity playerEntity = (Entity)(Object)this;
    @Inject(method = "removeVehicle()V", at = @At(value = "HEAD"))
    private void onStopRidingVehicle(CallbackInfo ci)
    {
        if (playerEntity instanceof Player player && player.getVehicle() == null && Utils.isMountBaubleEqualOnPlayer(player, player.getVehicle())) {
            ItemStack itemStack = Utils.getMountBauble(player);
            Utils.updateMountData(player, itemStack);
            Utils.updateMountBauble(player, itemStack);
            player.getCooldowns().addCooldown(itemStack.getItem(), BMConfig.cooldownValue.get() * 20);
            player.getVehicle().discard();
        }
    }
}

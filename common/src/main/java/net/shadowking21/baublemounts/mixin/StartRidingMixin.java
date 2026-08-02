package net.shadowking21.baublemounts.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.shadowking21.baublemounts.registry.ItemRegistry;
import net.shadowking21.baublemounts.utils.MountUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class StartRidingMixin {

    @Unique
    public Entity baublemounts$ridingEntity = (Entity) (Object) this;

    @Inject(method = "startRiding(Lnet/minecraft/world/entity/Entity;Z)Z", at = @At(value = "HEAD"), cancellable = true)
    public void onStartRiding(Entity entity, boolean bl, CallbackInfoReturnable<Boolean> cir)
    {
        if (baublemounts$ridingEntity instanceof Player player
                && ((player.getMainHandItem().getItem() == ItemRegistry.BAUBLECOMMON.get()
            && entity instanceof LivingEntity) // Mount Bauble can absorb only LivEn
            || (player.getMainHandItem().getItem() == ItemRegistry.VEHICLECOMMON.get()
            && !(entity instanceof LivingEntity))) // Vehicle Key can not absorb LivEn -> can absorb only Entity/Vehicles
                && !MountUtils.hasMountTag(player.getMainHandItem())
                && !MountUtils.isMountBaubleEqualOnPlayer(player, entity)) {

            MountUtils.recordMountData(entity, player, player.getMainHandItem(), InteractionHand.MAIN_HAND);
            cir.setReturnValue(true);
        }
    }
}

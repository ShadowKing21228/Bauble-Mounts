package net.shadowking21.baublemounts.mixin;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.shadowking21.baublemounts.BMConfig;
import net.shadowking21.baublemounts.registry.SoundRegistry;
import net.shadowking21.baublemounts.utils.MountUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class StopRidingMixin {

    @Unique
    public LivingEntity baublemounts$playerEntity = (LivingEntity)(Object) this;

    @Inject(method = "dismountVehicle(Lnet/minecraft/world/entity/Entity;)V", at = @At(value = "HEAD"))
    private void onStopRiding(Entity entity, CallbackInfo ci)
    {
        if (baublemounts$playerEntity instanceof Player player
                && !MountUtils.baublesHandler.getMountBauble(player).equals(ItemStack.EMPTY)
                && MountUtils.isMountBaubleEqualOnPlayer(player, entity))
        {
            ItemStack mountBauble = MountUtils.baublesHandler.getMountBauble(player);
            MountUtils.updateMountData(entity, mountBauble);

            player.getCooldowns().addCooldown(mountBauble.getItem(), BMConfig.config.cooldownValue * 20);
            player.level().playSound(entity, entity.getOnPos(), SoundRegistry.mountUnsummon.get(), SoundSource.NEUTRAL, 1f, 1f);
            entity.remove(Entity.RemovalReason.CHANGED_DIMENSION);
        }
    }
}

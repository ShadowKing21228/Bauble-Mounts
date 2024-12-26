package net.shadowking21.baublemounts.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.shadowking21.baublemounts.components.MountComponents;
import net.shadowking21.baublemounts.components.MountRecord;
import net.shadowking21.baublemounts.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(LivingEntity.class)
public class StopRidingMixin {
    public LivingEntity player = (LivingEntity)(Object)this;
    @Inject(method = "dismountVehicle(Lnet/minecraft/world/entity/Entity;)V", at = @At(value = "HEAD"), cancellable = true)
    private void onStopRiding(Entity entity, CallbackInfo ci)
    {
        if (player instanceof Player) {
            ItemStack itemStack = Utils.getMountBauble((Player) player);
            if (itemStack != ItemStack.EMPTY && itemStack.has(MountComponents.MOUNT_COMPONENTS) && Utils.hasMountComponents(itemStack)) {
                UUID uuid1 = UUID.fromString(itemStack.getComponents().get(MountComponents.MOUNT_COMPONENTS.get()).uuid());
                if (uuid1.equals(entity.getUUID())) {
                    CompoundTag compoundTag = new CompoundTag();
                    entity.save(compoundTag);
                    itemStack.set(MountComponents.MOUNT_COMPONENTS, new MountRecord(compoundTag, uuid1.toString()));
                    Utils.updateMountBauble((Player) player, itemStack);
                    entity.discard();
                }
            }
        }
    }
}

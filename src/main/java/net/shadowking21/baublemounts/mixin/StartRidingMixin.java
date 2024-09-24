package net.shadowking21.baublemounts.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.shadowking21.baublemounts.items.MountBauble;
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
        if (vehicle instanceof LivingEntity l_e)
        {
            ItemStack itemstack = player.getMainHandItem();
            if (itemstack.is((Item) MountBauble.BAUBLECOMMON.get()))
            {
                cir.setReturnValue(false);
                CompoundTag compoundtag = new CompoundTag();
                CompoundTag compoundtag2 = new CompoundTag();
                compoundtag2.putUUID("ID", vehicle.getUUID());
                CompoundTag entityData = new CompoundTag();
                CompoundTag idData = new CompoundTag();
                vehicle.save(entityData);
                compoundtag.put("Mount", entityData);
                idData.put("ID", compoundtag2);
                ItemStack itemStack = itemstack.copy();
                Utils.writeCompound(itemStack, compoundtag);
                Utils.writeCompound(itemStack, idData);
                player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
                vehicle.discard();
            }
        }
    }
}
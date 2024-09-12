package net.shadowking21.baublemounts.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.shadowking21.baublemounts.items.MountBauble;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(ServerPlayer.class)
public abstract class StartRidingMixin {
public ServerPlayer player = (ServerPlayer)(Object)this;
    @Inject(method = "startRiding(Lnet/minecraft/world/entity/Entity;Z)Z", at = @At("HEAD"), cancellable = true)
    private void onStartRiding(Entity vehicle, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if (vehicle instanceof LivingEntity l_e)
        {
            ItemStack itemstack = player.getMainHandItem();
            if (itemstack.is(MountBauble.BAUBLECOMMON.get()))
            {
                cir.setReturnValue(false);
                CompoundTag compoundtag = new CompoundTag();
                CompoundTag compoundtag2 = new CompoundTag();
                compoundtag2.putUUID("ID", vehicle.getUUID());
                vehicle.save(compoundtag);
                ItemStack itemStack = itemstack.copy();
                itemStack.addTagElement("Mount", compoundtag);
                itemStack.addTagElement("ID", compoundtag2);
                player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
                vehicle.discard();
            }
        }
    }
}
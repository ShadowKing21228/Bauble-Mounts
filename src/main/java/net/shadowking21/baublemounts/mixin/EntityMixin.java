package net.shadowking21.baublemounts.mixin;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.shadowking21.baublemounts.BaubleMounts;
import net.shadowking21.baublemounts.items.MountBauble;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Entity.class)
public class EntityMixin {

    @Shadow @Nullable private Entity vehicle;
    public Entity thisEntity = (Entity)(Object)this;

    @Inject(method = "startRiding(Lnet/minecraft/world/entity/Entity;Z)Z", at = @At(value = "HEAD"), cancellable = true)
    public void sdm$startRiding(Entity entity, boolean p_19967_, CallbackInfoReturnable<Boolean> cir){
        if(BaubleMounts.isIceAndFireLoaded()) {
            if (thisEntity instanceof Player player && entity instanceof EntityDragonBase dragon) {
                ItemStack itemstack = player.getMainHandItem();
                if (itemstack.is(MountBauble.BAUBLECOMMON.get()))
                {
                    cir.setReturnValue(false);
                    CompoundTag compoundtag = new CompoundTag();
                    CompoundTag compoundtag2 = new CompoundTag();
                    compoundtag2.putUUID("ID", dragon.getUUID());
                    dragon.save(compoundtag);
                    ItemStack itemStack = itemstack.copy();
                    itemStack.addTagElement("Mount", compoundtag);
                    itemStack.addTagElement("ID", compoundtag2);
                    player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
                    dragon.discard();
                }
            }
        }
    }
}

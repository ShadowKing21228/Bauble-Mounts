package net.shadowking21.baublemounts.mixin.iceandfire;


import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.world.item.ItemStack;
import net.shadowking21.baublemounts.items.MountBauble;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = EntityDragonBase.class)
public class EntityDragonBaseMixin {

    @Redirect(method = "mobInteract(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"))
    private boolean sdm$mobInteract(ItemStack instance){
        return instance.isEmpty() || instance.is(MountBauble.BAUBLECOMMON.get());
    }


}

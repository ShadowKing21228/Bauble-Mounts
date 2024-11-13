package net.shadowking21.baublemounts.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundCustomSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.shadowking21.baublemounts.BaubleMounts;
import net.shadowking21.baublemounts.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(LivingEntity.class)
public class StopRidingMixin {
    public LivingEntity player = (LivingEntity)(Object)this;
    @Inject(method = "dismountVehicle(Lnet/minecraft/world/entity/Entity;)V", at = @At(value = "HEAD"))
    private void onStopRiding(Entity entity, CallbackInfo ci)
    {
        if (player instanceof ServerPlayer player) {
            ItemStack itemStack = Utils.getMountBauble((Player) player);
                if (itemStack.getOrCreateTag().contains("ID")) {
                    UUID uuid1 = itemStack.getOrCreateTag().getCompound("ID").getUUID("ID");
                    if (uuid1.equals(entity.getUUID())) {
                        CompoundTag compoundTag = new CompoundTag();
                        entity.save(compoundTag);
                        itemStack.getOrCreateTag().put("Mount", compoundTag);
                        Utils.updateMountBauble((Player) player, itemStack);
                        entity.discard();
                        ResourceLocation d1 = new ResourceLocation(BaubleMounts.MODID, "mount_unsummon");
                        player.connection.send(new ClientboundCustomSoundPacket(d1, SoundSource.NEUTRAL, player.position(), 1,1,player.level.random.nextLong()));
                    }
                }
        }
    }
}

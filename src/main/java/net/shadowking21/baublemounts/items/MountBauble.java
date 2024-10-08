package net.shadowking21.baublemounts.items;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundCustomSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shadowking21.baublemounts.BaubleMounts;
import net.shadowking21.baublemounts.sounds.MountSound;

import java.util.Objects;

public class MountBauble {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BaubleMounts.MODID);

    public static final RegistryObject<Item> BAUBLECOMMON = ITEMS.register("mount_bauble", () ->
            new Item(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).stacksTo(1)){
        @Override
        public boolean isFoil(ItemStack itemStack) {
            return itemStack.getTag() != null && itemStack.getTag().contains("Mount") && !itemStack.getTag().getCompound("Mount").isEmpty();
        }
    });
    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }

    public static void spawnMount(ServerPlayer player, ItemStack baubleMount, BlockPos blockPos)
    {
        if (baubleMount.getItem() == MountBauble.BAUBLECOMMON.get());
        {
            player.stopRiding();
            CompoundTag mountTag = baubleMount.getOrCreateTag().getCompound("Mount");
            if (!mountTag.isEmpty())
            {
                ItemStack itemStack = baubleMount.copy();
                var var = EntityType.create(mountTag, player.getLevel());
                var.get().setPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
                player.getLevel().addFreshEntity(var.get());
                itemStack.addTagElement("Mount", new CompoundTag());
                if (player.isCreative())
                {
                    player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
                }
                else
                {
                    player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                }
            }
        }
    }
    public static void spawnRideMount(ServerPlayer player, ItemStack baubleMount)
    {
        if (player.isPassenger()) {
            CompoundTag mountTag = baubleMount.getOrCreateTag().getCompound("Mount");
            var var = EntityType.create(mountTag, player.getLevel());
            Entity entity = player.getVehicle();
            if (Objects.equals(baubleMount.getTag().getCompound("ID").getUUID("ID"), entity.getUUID())) {
                player.stopRiding();
                ResourceLocation d1 = new ResourceLocation(BaubleMounts.MODID, "mount_unsummon");
                player.connection.send(new ClientboundCustomSoundPacket(d1, SoundSource.NEUTRAL, player.position(), 1,1,player.level.random.nextLong()));
            }
        }
        else if (!player.getCooldowns().isOnCooldown(baubleMount.getItem()))
         {
             CompoundTag mountTag = baubleMount.getOrCreateTag().getCompound("Mount");
             var var = EntityType.create(mountTag, player.getLevel());
            BlockPos blockPos = player.getOnPos();
            var.get().setPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
            player.getLevel().addFreshEntity(var.get());
            player.startRiding(var.get(), true);
             player.getCooldowns().addCooldown(baubleMount.getItem(),100);

             ResourceLocation d1 = new ResourceLocation(BaubleMounts.MODID, "mount_summon");
             player.connection.send(new ClientboundCustomSoundPacket(d1, SoundSource.NEUTRAL, player.position(), 1,1,player.level.random.nextLong()));
         }
    }
}

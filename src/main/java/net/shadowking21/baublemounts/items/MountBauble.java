package net.shadowking21.baublemounts.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.shadowking21.baublemounts.BaubleMounts;
import net.shadowking21.baublemounts.utils.Utils;

import java.util.Objects;

public class MountBauble {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(BaubleMounts.MODID);
    public static final DeferredItem<Item> BAUBLEBROKEN = ITEMS.register("mount_bauble_broken", () -> new Item(new Item.Properties().stacksTo(1)){});

    public static final DeferredItem<Item> BAUBLECOMMON = ITEMS.register("mount_bauble", () -> new Item(new Item.Properties().stacksTo(1))
    {
       @Override
       public boolean isFoil(ItemStack itemStack) {
           return Utils.getMountCompoundTag(itemStack).contains("Mount");
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
            CompoundTag mountTag = Utils.getMountCompoundTag(baubleMount).getCompound("Mount");
            if (!mountTag.isEmpty())
            {
                ItemStack itemStack = baubleMount.copy();
                var var = EntityType.create(mountTag, player.level());
                var.get().setPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
                player.level().addFreshEntity(var.get());
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.put("Mount", new CompoundTag());
                Utils.writeCompound(itemStack, compoundTag);
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
            CompoundTag mountTag = Utils.getMountCompoundTag(baubleMount).getCompound("Mount");
            var var = EntityType.create(mountTag, player.level());
            Entity entity = player.getVehicle();
            if (Objects.equals(Utils.getMountCompoundTag(baubleMount).getCompound("ID").getUUID("ID"), entity.getUUID())) {
                player.stopRiding();
            }
        }
        else if (!player.getCooldowns().isOnCooldown(baubleMount.getItem()))
         {
             CompoundTag mountTag = Utils.getMountCompoundTag(baubleMount).getCompound("Mount");
             var var = EntityType.create(mountTag, player.level());
            BlockPos blockPos = player.getOnPos();
            var.get().setPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
            player.level().addFreshEntity(var.get());
            player.startRiding(var.get(), true);
             player.getCooldowns().addCooldown(baubleMount.getItem(),100);
        }
    }
}

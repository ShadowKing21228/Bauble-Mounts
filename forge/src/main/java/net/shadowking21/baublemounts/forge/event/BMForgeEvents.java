package net.shadowking21.baublemounts.forge.event;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.shadowking21.baublemounts.registry.ItemRegistry;
import net.shadowking21.baublemounts.utils.MountUtils;
import top.theillusivec4.curios.api.event.CurioUnequipEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BMForgeEvents {

    private static final String BOOK_OF_DRAGONS_DRAGON_BASE = "net.magister.bookofdragons.entity.base.dragon.DragonBase";

    private static final boolean BOOK_OF_DRAGON_MOD = ModList.get().isLoaded("bookofdragons");

    @SubscribeEvent
    public void onMountBaubleUnequip(CurioUnequipEvent event) {

        if (event.getEntity() instanceof Player player
                && MountUtils.isMountBauble(event.getStack().getItem())
                && player.isPassenger()
                && MountUtils.isMountBaubleEqualOnPlayer(player, player.getVehicle()))
        {
            player.stopRiding();
        }
    }

    @SubscribeEvent
    public void onBookOfDragonsDragonInteract(PlayerInteractEvent.EntityInteract event) {
        if (!BOOK_OF_DRAGON_MOD) {
            return;
        }

        Player player = event.getEntity();
        Entity target = event.getTarget();

        if (event.getHand() != InteractionHand.MAIN_HAND
                || !isBookOfDragonsDragon(target)
                || !canBookOfDragonsDragonBeMountedBy(target, player)) {
            return;
        }

        ItemStack mainHandItem = player.getMainHandItem();
        if (mainHandItem.getItem() != ItemRegistry.BAUBLECOMMON.get()
                || MountUtils.hasMountTag(mainHandItem)
                || MountUtils.isMountBaubleEqualOnPlayer(player, target)) {
            return;
        }

        if (!player.level().isClientSide()) {
            MountUtils.recordMountData(target, player, mainHandItem, InteractionHand.MAIN_HAND);
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    private static boolean isBookOfDragonsDragon(Entity entity) {
        Class<?> currentClass = entity.getClass();
        while (currentClass != null) {
            if (BOOK_OF_DRAGONS_DRAGON_BASE.equals(currentClass.getName())) {
                return true;
            }
            currentClass = currentClass.getSuperclass();
        }
        return false;
    }

    private static boolean canBookOfDragonsDragonBeMountedBy(Entity entity, Player player) {
        try {
            Method canBeMountedBy = entity.getClass().getMethod("canBeMountedBy", Player.class);
            return Boolean.TRUE.equals(canBeMountedBy.invoke(entity, player));
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            return false;
        }
    }
}

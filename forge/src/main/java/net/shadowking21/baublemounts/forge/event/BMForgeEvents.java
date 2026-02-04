package net.shadowking21.baublemounts.forge.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.shadowking21.baublemounts.utils.MountUtils;
import top.theillusivec4.curios.api.event.CurioUnequipEvent;

public class BMForgeEvents {

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
}

package net.shadowking21.baublemounts.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.shadowking21.baublemounts.items.MountBauble;
import net.shadowking21.baublemounts.items.VehicleBauble;
import net.shadowking21.baublemounts.utils.Utils;

public class SendSpawnEntityC2S extends BaseC2SMessage {
    public SendSpawnEntityC2S()
    {

    }
    public SendSpawnEntityC2S(FriendlyByteBuf buf)
    {

    }
    @Override
    public MessageType getType() {
        return ModNetwork.SendSpawn;
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {

    }
    @Override
    public void handle(NetworkManager.PacketContext packetContext) {
        ServerPlayer player = (ServerPlayer) packetContext.getPlayer();
        ItemStack itemStack = Utils.getMountBauble(player);
        if (itemStack.hasFoil() && itemStack.getItem().equals(MountBauble.BAUBLECOMMON.get())) {
            MountBauble.spawnRideMount(player, itemStack);
        } else if (itemStack.hasFoil() && itemStack.getItem().equals(VehicleBauble.BAUBLEVEHICLE.get())) {
            VehicleBauble.spawnRideMount(player, itemStack);
        }
    }
}

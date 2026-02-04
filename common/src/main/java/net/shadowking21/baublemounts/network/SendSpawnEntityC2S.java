package net.shadowking21.baublemounts.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.shadowking21.baublemounts.registry.ItemRegistry;
import net.shadowking21.baublemounts.registry.PacketRegistry;
import net.shadowking21.baublemounts.utils.MountUtils;

public class SendSpawnEntityC2S extends BaseC2SMessage {

    public SendSpawnEntityC2S() {}

    public SendSpawnEntityC2S(FriendlyByteBuf buf) {}

    @Override
    public MessageType getType() {
        return PacketRegistry.sendSpawn;
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {}

    @Override
    public void handle(NetworkManager.PacketContext packetContext) {
        ServerPlayer player = (ServerPlayer) packetContext.getPlayer();
        ItemStack itemStack = MountUtils.baublesHandler.getMountBauble(player);

        if (MountUtils.hasMountTag(itemStack) && itemStack.getItem().equals(ItemRegistry.BAUBLECOMMON.get()))
            MountUtils.spawnRideMount(player, itemStack);


        else if (MountUtils.hasMountTag(itemStack) && itemStack.getItem().equals(ItemRegistry.VEHICLECOMMON.get()))
            MountUtils.spawnRideVehicle(player, itemStack);

    }
}

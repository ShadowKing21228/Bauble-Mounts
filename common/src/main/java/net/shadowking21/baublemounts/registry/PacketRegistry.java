package net.shadowking21.baublemounts.registry;

import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import net.shadowking21.baublemounts.Baublemounts;
import net.shadowking21.baublemounts.network.SendSpawnEntityC2S;

public interface PacketRegistry {

    SimpleNetworkManager NETWORK_MANAGER = SimpleNetworkManager.create(Baublemounts.MOD_ID);

    MessageType sendSpawn = NETWORK_MANAGER.registerC2S("sendspawn", SendSpawnEntityC2S::new);

    static void init() {}
}


package net.shadowking21.baublemounts.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import net.shadowking21.baublemounts.BaubleMounts;

public interface ModNetwork {
    SimpleNetworkManager NETWORK_MANAGER = SimpleNetworkManager.create(BaubleMounts.MODID);
    MessageType SendSpawn = NETWORK_MANAGER.registerC2S("sendspawn", SendSpawnEntityC2S::new);
    static void init()
    {

    }
}


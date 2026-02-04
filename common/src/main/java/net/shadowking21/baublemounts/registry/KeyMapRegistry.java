package net.shadowking21.baublemounts.registry;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;
import net.shadowking21.baublemounts.network.SendSpawnEntityC2S;

public class KeyMapRegistry {

    public static final KeyMapping MOUNTSUMMON_KEY = new KeyMapping(
            "key.baublemounts.mountsummon",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_R,
            "key.categories.baublemounts"
    );

    public static void init()
    {
        KeyMappingRegistry.register(MOUNTSUMMON_KEY);

        ClientTickEvent.CLIENT_POST.register(minecraft -> {
            while (MOUNTSUMMON_KEY.consumeClick()) {
                new SendSpawnEntityC2S().sendToServer();
            }
        });
    }

}

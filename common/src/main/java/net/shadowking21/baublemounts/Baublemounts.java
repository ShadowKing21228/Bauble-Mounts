package net.shadowking21.baublemounts;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.shadowking21.baublemounts.events.MBEvents;
import net.shadowking21.baublemounts.registry.PacketRegistry;
import net.shadowking21.baublemounts.registry.ItemRegistry;
import net.shadowking21.baublemounts.registry.KeyMapRegistry;
import net.shadowking21.baublemounts.registry.SoundRegistry;

public final class Baublemounts {

    public static final String MOD_ID = "baublemounts";

    public static final boolean IS_CLIENT_SIDE = Platform.getEnvironment() == Env.CLIENT;

    //public static final Supplier<RegistrarManager> REGISTRAR_MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    public static void init() {
        ItemRegistry.init();
        SoundRegistry.init();
        PacketRegistry.init();
        MBEvents.init();

        if (IS_CLIENT_SIDE)
            clientInit();
    }

    public static void clientInit()
    {
        KeyMapRegistry.init();
    }
}

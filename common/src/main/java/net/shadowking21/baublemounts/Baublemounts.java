package net.shadowking21.baublemounts;

import com.google.common.base.Suppliers;
import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.RegistrarManager;
import net.shadowking21.baublemounts.events.MBEvents;
import net.shadowking21.baublemounts.registry.PacketRegistry;
import net.shadowking21.baublemounts.registry.ItemRegistry;
import net.shadowking21.baublemounts.registry.KeyMapRegistry;
import net.shadowking21.baublemounts.registry.SoundRegistry;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

public final class Baublemounts {

    public static final String MOD_ID = "baublemounts";

    public static final Supplier<RegistrarManager> REGISTRAR_MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    public static void init() {
        ItemRegistry.init();
        SoundRegistry.init();
        PacketRegistry.init();
        KeyMapRegistry.init();
        MBEvents.init();
    }
}

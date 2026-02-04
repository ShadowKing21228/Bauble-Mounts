package net.shadowking21.baublemounts.fabric;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.fabricmc.loader.api.FabricLoader;
import net.shadowking21.baublemounts.Baublemounts;
import net.fabricmc.api.ModInitializer;
import net.shadowking21.baublemounts.fabric.config.ConfigInit;
import net.shadowking21.baublemounts.fabric.utils.FabricBaubleHandler;
import net.shadowking21.baublemounts.utils.MountUtils;
import net.shadowking21.shadowconfig.ShadowConfig;

public final class BaublemountsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        
        ConfigInit.init();
        MountUtils.baublesHandler = new FabricBaubleHandler();
        Baublemounts.init();
    }
}

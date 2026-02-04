package net.shadowking21.baublemounts.forge;

import net.minecraftforge.common.MinecraftForge;
import net.shadowking21.baublemounts.Baublemounts;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.shadowking21.baublemounts.forge.config.ConfigInit;
import net.shadowking21.baublemounts.forge.event.BMForgeEvents;
import net.shadowking21.baublemounts.forge.utils.ForgeBaublesHandler;
import net.shadowking21.baublemounts.utils.MountUtils;

@Mod(Baublemounts.MOD_ID)
public final class BaublemountsForge {

    public BaublemountsForge() {
        EventBuses.registerModEventBus(Baublemounts.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ConfigInit.init();
        Baublemounts.init();
        MinecraftForge.EVENT_BUS.register(new BMForgeEvents());
        MountUtils.baublesHandler = new ForgeBaublesHandler();
    }

}
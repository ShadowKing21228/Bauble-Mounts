package net.shadowking21.baublemounts.forge.config;

import net.shadowking21.baublemounts.BMConfig;
import net.shadowking21.baublemounts.Baublemounts;
import net.shadowking21.shadowconfig.config.BaseShadowConfig;
import net.shadowking21.shadowconfig.config.exstensions.toml.SCTomlConfig;

public class ConfigInit {

    public static BaseShadowConfig<BMConfigForge> config;

    public static void init()
    {
        config = SCTomlConfig.Builder.builder(BMConfigForge.class)
                .defaults(new BMConfigForge())
                .modId(Baublemounts.MOD_ID)
                .build();

        BMConfig.config = getConfig();
    }

    public static BMConfig getConfig()
    {
        var commonConfig = new BMConfig();
        var loaderConfig = config.getCurrentConfig();
        commonConfig.destructionUponRelease = loaderConfig.destructionUponRelease;
        commonConfig.isCustomTooltipRender = loaderConfig.isCustomTooltipRender;
        commonConfig.brokenBaubleAppearance = loaderConfig.brokenBaubleAppearance;
        commonConfig.cooldownValue = loaderConfig.cooldownValue;
        commonConfig.mountList = loaderConfig.mountList;
        return commonConfig;
    }
}

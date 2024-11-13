package net.shadowking21.baublemounts;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class BMConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue cooldownValue;
    public static final ForgeConfigSpec.BooleanValue destructionUponRelease;
    public static final ForgeConfigSpec.BooleanValue brokenBaubleAppearance;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> mountList;

    static {
        BUILDER.push("General");

        cooldownValue = BUILDER
                .comment("This is cooldown value of mount spawn in SECONDS. Default: 5")
                .defineInRange("cooldownValue", 5, 0, 86400);

        destructionUponRelease = BUILDER
                .comment("If true, Mount Bauble will be destroyed when the mount is released, otherwise the Mount Bauble will remain in your hand without the mount inside it. Default: true")
                .define("destructionUponRelease", true);

        brokenBaubleAppearance = BUILDER
                .comment("If true, Broken Mount Bauble will appear in the slot when the mount dies, otherwise, it will not appear and the mount cannot be resurrected. Default: true")
                .define("brokenBaubleAppearance", true);

        mountList = BUILDER
                .comment("A blacklist of entities (It is assumed that mounts) that cannot be placed in Mount Bauble")
                .defineList("mountList", Arrays.asList("modname:entityname"),
                        obj -> obj instanceof String);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}

package net.shadowking21.baublemounts;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.UnmodifiableCommentedConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForgeConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BMConfig {
    public static final BMConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;
    static {
        Pair<BMConfig, ModConfigSpec> pair =
                new ModConfigSpec.Builder().configure(BMConfig::new);

        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }
    public final ModConfigSpec.ConfigValue<Integer> cooldownValue;
    public final ModConfigSpec.ConfigValue<Boolean> destructionUponRelease;
    public final ModConfigSpec.ConfigValue<Boolean> brokenBaubleAppearance;
    //public final ModConfigSpec.ConfigValue<List<? extends String>> mountList;
    //public Supplier<String> newElementSupplier;
    //public Predicate<Object> elementValidator = obj -> {
    //    if (!(obj instanceof List<?>)) {
    //        return false;
    //    }
    //    List<?> list = (List<?>) obj;
    //    return list.stream().allMatch(element -> element instanceof String);
    //};

    public BMConfig(ModConfigSpec.Builder builder) {
        builder.translation("baublemounts.configuration.section.baublemounts.common.toml");
        {
            builder.push("General");
            cooldownValue = builder
                    .translation("config.baublemounts.coolownValue")
                    .comment("This is cooldown value of mount spawn in SECONDS. Default: 5")
                    .defineInRange("cooldownValue", 5, 0, 86400);
            destructionUponRelease = builder
                    .translation("config.baublemounts.destructionUponRelease")
                    .comment("If true, Mount Bauble will be destroyed when the mount is released, otherwise the Mount Bauble will remain in your hand without the mount inside it. Default: true")
                    .define("destructionUponRelease", true);
            brokenBaubleAppearance = builder
                    .translation("config.baublemounts.brokenBaubleAppearance")
                    .comment("If true, Broken Mount Bauble will appear in the slot when the mount dies, otherwise, it will not appear and the mount cannot be resurrected. Default: true")
                    .define("brokenBaubleAppearance", true);
            //mountList = builder
            //        .translation("config.baublemounts.mountList")
            //        .comment("A blacklist of entities (It is assumed that mounts) that cannot be placed in Mount Bauble")
            //        .defineListAllowEmpty("mountList", Arrays.asList("modname:entityname"), newElementSupplier, elementValidator);
            builder.pop();
        }
    }
}

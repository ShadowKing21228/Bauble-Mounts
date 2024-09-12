package net.shadowking21.baublemounts.items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shadowking21.baublemounts.BaubleMounts;

public class MountBaubleBroken {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BaubleMounts.MODID);

    public static final RegistryObject<Item> BAUBLEBROKEN = ITEMS.register("mount_bauble_broken", () ->
            new Item(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).stacksTo(1)){});
    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}

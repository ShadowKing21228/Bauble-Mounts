package net.shadowking21.baublemounts.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.shadowking21.baublemounts.items.MountBauble;

import static net.shadowking21.baublemounts.Baublemounts.MOD_ID;

public class ItemRegistry {

    static DeferredRegister<Item> items = DeferredRegister.create(MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<MountBauble> BAUBLECOMMON = items.register(new ResourceLocation(MOD_ID, "mount_bauble"),
                () -> new MountBauble("tooltip.baublemounts.shiftdesc", true));

    public static final RegistrySupplier<MountBauble> BAUBLEBROKEN = items.register(new ResourceLocation(MOD_ID, "mount_bauble_broken"),
                () -> new MountBauble("tooltip.baublemounts.shiftdescbroken", false));

    public static final RegistrySupplier<MountBauble> VEHICLECOMMON = items.register(new ResourceLocation(MOD_ID, "vehicle_bauble"),
                () -> new MountBauble("tooltip.baublemounts.vehicleshiftdesc", true));

    public static void init() {
        items.register();
    }
}

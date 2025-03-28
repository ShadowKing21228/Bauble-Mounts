package net.shadowking21.baublemounts.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shadowking21.baublemounts.BaubleMounts;

import java.util.List;
import java.util.Optional;

public class VehicleBaubleBroken {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BaubleMounts.MODID);

    public static final RegistryObject<Item> BAUBLEVEHICLEBROKEN = ITEMS.register("vehicle_bauble_broken", () -> new Item(new Item.Properties().stacksTo(1)){
        @Override
        public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add(Component.translatable("tooltip.baublemounts.vehicleshiftdescbroken"));
            } else {
                tooltipComponents.add(Component.translatable("tooltip.baublemounts.desc"));
            }
            if (stack.hasTag() && stack.getTag().contains("Mount") && !stack.getTag().getCompound("Mount").isEmpty()) {
                CompoundTag compoundTag = stack.getTag().getCompound("Mount");
                Optional<Entity> d1 = EntityType.create(compoundTag, level);
                if (d1.isPresent()) {
                    Entity entity = d1.get();
                    String d = entity.getDisplayName().getString();
                    if (d.contains("[")) d = d.replace("[", " ");
                    if (d.contains("]")) d = d.replace("]", " ");

                    tooltipComponents.add(Component.translatable("tooltip.baublemounts.getname", d));
                    //tooltipComponents.add(Component.translatable("tooltip.baublemounts.gethealth", (entity1.getHealth() + " / " + entity1.getMaxHealth())));
                }
            }
            super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
        }});
    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}

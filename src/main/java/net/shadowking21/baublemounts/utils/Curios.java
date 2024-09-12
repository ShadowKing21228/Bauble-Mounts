package net.shadowking21.baublemounts.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
public class Curios {
public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag unused) {
    return CuriosApi.createCurioProvider(new ICurio() {

        @Override
        public ItemStack getStack() {
            return stack;
        }

        @Override
        public void curioTick(SlotContext slotContext) {
            // ticking logic here
        }
    });
}
}
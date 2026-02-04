package net.shadowking21.baublemounts.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.shadowking21.baublemounts.BMConfig;
import net.shadowking21.baublemounts.registry.KeyMapRegistry;
import net.shadowking21.baublemounts.utils.MountUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MountBauble extends Item {

    protected String descKey;
    protected boolean canReveal;

    public MountBauble(String descKey, boolean canReveal) {
        super(new Properties().arch$tab(CreativeModeTabs.TOOLS_AND_UTILITIES).stacksTo(1));
        this.descKey = descKey;
        this.canReveal = canReveal;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemStack) {
        return MountUtils.hasMountTag(itemStack);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        var handItem = pContext.getItemInHand();

        if (canReveal && MountUtils.hasMountTag(handItem)) {

            MountUtils.createMountOn(handItem, pContext.getLevel(), pContext.getClickedPos());
            MountUtils.clearMountTag(handItem);

            if (!(!BMConfig.config.destructionUponRelease || pContext.getPlayer().isCreative()))
                handItem.setCount(0);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable(descKey, "§e" + KeyMapRegistry.MOUNTSUMMON_KEY.getName() + "§r."));
        }

        else {
            tooltipComponents.add(Component.translatable("tooltip.baublemounts.desc"));
        }

        if (MountUtils.hasMountTag(stack)) {

            var mountInBauble = MountUtils.createEntityType(stack, level);

            if (mountInBauble.isPresent()) {
                String d = mountInBauble.get().getDisplayName().getString();
                if (d.contains("[")) d = d.replace("[", " ");
                if (d.contains("]")) d = d.replace("]", " ");
                tooltipComponents.add(Component.translatable("tooltip.baublemounts.getname", d));

                if (mountInBauble.get() instanceof LivingEntity livingMountInBauble)
                    tooltipComponents.add(Component.translatable("tooltip.baublemounts.gethealth", (livingMountInBauble.getHealth() + " / " + livingMountInBauble.getMaxHealth())));
            }

        }
    }

}

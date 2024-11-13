package net.shadowking21.baublemounts.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.sixik.sdmuilib.client.utils.GLHelper;
import net.sixik.sdmuilib.client.utils.RenderHelper;
import net.sixik.sdmuilib.client.utils.math.Vector2;
import net.sixik.sdmuilib.client.utils.math.Vector2d;
import net.sixik.sdmuilib.client.utils.misc.CenterOperators;
import net.sixik.sdmuilib.client.utils.renders.TextureRenderHelper;

import java.util.List;
import java.util.Vector;
@OnlyIn(Dist.CLIENT)
public class TooltipHandler {
    public static void register(RegisterClientTooltipComponentFactoriesEvent event)
    {
        event.register(BaubleMountsTooltipComponent.class, TooltipRender::new);
    }
    public static class TooltipRender implements ClientTooltipComponent
    {
        public BaubleMountsTooltipComponent component;
        public TooltipRender(BaubleMountsTooltipComponent component)
        {
            this.component = component;
        }
        @Override
        public int getHeight() {
            return 0;
        }

        @Override
        public int getWidth(Font font) {
            return 0;
        }
        public int tick = 90;

        public Vector2 calculateSize(List<ClientTooltipComponent> list)
        {
            int x= 0;
            int y= 0;
            for (ClientTooltipComponent clientTooltipComponent : list) {
                if (clientTooltipComponent.getWidth(Minecraft.getInstance().font) > x) x = clientTooltipComponent.getWidth(Minecraft.getInstance().font);
                y += clientTooltipComponent.getHeight();
            }
            return new Vector2(x, y);
        }

        public static Vector2d getEntitySize(LivingEntity livingEntity, double scale) {
            AABB d = livingEntity.getBoundingBox();
            return new Vector2d(d.maxX * scale, d.minY * scale);
        }

        @Override
        public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
            Vector2 screenSize = new Vector2(Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight());
            List<ClientTooltipComponent> var = net.minecraftforge.client.ForgeHooksClient.gatherTooltipComponents(component.itemStack, component.itemStack.getTooltipLines(Minecraft.getInstance().player, TooltipFlag.NORMAL), x, screenSize.x, screenSize.y, font);
            Vector2 vector2 = calculateSize(var);
            Vector2d vector2d = new Vector2d(0, 0);
            Vector2 tooltipSize = new Vector2(0 , 0);
            if (component.entity != null)
            {
                vector2d = getEntitySize((LivingEntity) component.entity, 0.85);
                tooltipSize.setY((int) (vector2.y+(8*var.size()) + vector2d.y) + (65 + 40));
            }
            else tooltipSize.setY(vector2.y+(8*var.size()));
            if (vector2d.x > vector2.x+16)
                tooltipSize.setX(vector2.x);
            else
                tooltipSize.setX(vector2.x+16);
            Vector2 entityPos = GLHelper.getCenterWithPos(new Vector2(x, 0), tooltipSize, CenterOperators.Type.CENTER_X, CenterOperators.Method.ABSOLUTE);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, 900);
            TextureRenderHelper.renderSlicedTexture(guiGraphics, new ResourceLocation("baublemounts:textures/tooltip/button.png"), x, y, tooltipSize.x, tooltipSize.y, 10, 64, 64);

            int l = x+8;
            int i1 = y+8;
            int k1 = i1;
            List<ClientTooltipComponent> list;
            for(int l1 = 0; l1 < var.size(); ++l1) {
                ClientTooltipComponent clienttooltipcomponent1 = var.get(l1);
                clienttooltipcomponent1.renderText(font, l, k1, guiGraphics.pose().last().pose(), guiGraphics.bufferSource());
                k1 += clienttooltipcomponent1.getHeight() + (l1 == 0 ? 2 : 0);
            }

            k1 = i1;
            for(int k2 = 0; k2 < var.size(); ++k2) {
                ClientTooltipComponent clienttooltipcomponent2 = var.get(k2);
                clienttooltipcomponent2.renderImage(font, l, k1, guiGraphics);
                k1 += clienttooltipcomponent2.getHeight() + (k2 == 0 ? 2 : 0);
            }
            if (component.entity != null)
                RenderHelper.drawLivingEntity(guiGraphics, entityPos.x+10, k1+65, 23, tick, 0, (LivingEntity) component.entity);

            guiGraphics.pose().popPose();
            tick++;
        }
    }
    public static class BaubleMountsTooltipComponent implements net.minecraft.world.inventory.tooltip.TooltipComponent
    {
        public ItemStack itemStack;
        public Entity entity;
        public BaubleMountsTooltipComponent (ItemStack itemStack, Entity entity)
        {
            this.itemStack = itemStack;
            this.entity = entity;
        }
    }
}

package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.hud.HUDComponentManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Armor extends HUDComponent {
    public Armor() {
        super("Armor", 200, 200);
    }

    static final RenderItem itemRender = mc.getRenderItem();

    @Override
    public void renderComponent() {
        GlStateManager.enableTexture2D();
        int iteration = 0;
        for (ItemStack is : mc.player.inventory.armorInventory) {
            ++iteration;

            if (is.isEmpty())
                continue;

            int x = ((9 - iteration) * 14);
            itemRender.zLevel = 200.0f;
            itemRender.renderItemAndEffectIntoGUI(is, (HUDComponentManager.getComponentByName("Armor").getX() - 70) + x , HUDComponentManager.getComponentByName("Armor").getY());
            itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, (HUDComponentManager.getComponentByName("Armor").getX() - 70) + x, HUDComponentManager.getComponentByName("Armor").getY(), "");
            itemRender.zLevel = 0.0f;
            width = x - 8;
            height = 17;
        }

        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }
}

package me.linus.momentum.gui.hud.element.elements;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.element.AnchorPoint;
import me.linus.momentum.util.client.color.ThemeColor;
import me.linus.momentum.module.modules.client.HUD;
import me.linus.momentum.gui.hud.element.HUDElement;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.init.Items;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Totem extends HUDElement {
    public Totem() {
        super("Totem", 2, 57, Category.COMBAT, AnchorPoint.None);
    }

    @Override
    public void renderElement() {
        FontUtil.drawString("Totems: " + TextFormatting.WHITE + InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING), this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = Momentum.fontManager.getCustomFont().getStringWidth("Totems: " + InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING)) + 2;
    }
}

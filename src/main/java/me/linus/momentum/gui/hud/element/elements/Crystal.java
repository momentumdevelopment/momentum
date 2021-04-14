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

public class Crystal extends HUDElement {
    public Crystal() {
        super("Crystal", 2, 57, Category.COMBAT, AnchorPoint.None);
    }

    @Override
    public void renderElement() {
        FontUtil.drawString("Crystals: " + TextFormatting.WHITE + InventoryUtil.getItemCount(Items.END_CRYSTAL), this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = Momentum.fontManager.getCustomFont().getStringWidth("Crystals: " + InventoryUtil.getItemCount(Items.END_CRYSTAL)) + 2;
    }
}

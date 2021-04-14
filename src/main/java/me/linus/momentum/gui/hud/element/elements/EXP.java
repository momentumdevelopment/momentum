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
 * @author Papa-Quill
 * @since 12/17/2020
 */

public class EXP extends HUDElement {
    public EXP() {
        super("EXP", 2, 57, Category.COMBAT, AnchorPoint.None);
    }

    @Override
    public void renderElement() {
        FontUtil.drawString("EXP: " + TextFormatting.WHITE + InventoryUtil.getItemCount(Items.EXPERIENCE_BOTTLE), this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = Momentum.fontManager.getCustomFont().getStringWidth("EXP: " + InventoryUtil.getItemCount(Items.EXPERIENCE_BOTTLE)) + 2;
    }
}

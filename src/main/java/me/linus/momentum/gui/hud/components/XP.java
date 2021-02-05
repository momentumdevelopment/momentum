package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.module.modules.client.HUDEditor;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.init.Items;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class XP extends HUDComponent {
    public XP() {
        super("XP", 2, 57);
    }

    @Override
    public void renderComponent() {
        FontUtil.drawString("XPs: " + TextFormatting.AQUA + InventoryUtil.getItemCount(Items.EXPERIENCE_BOTTLE), this.x, this.y, HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = Momentum.fontManager.getCustomFont().getStringWidth("XPs: " + InventoryUtil.getItemCount(Items.EXPERIENCE_BOTTLE)) + 2;
    }
}

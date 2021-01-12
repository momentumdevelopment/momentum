package me.linus.momentum.gui.hud.components;

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

public class Totem extends HUDComponent {
    public Totem() {
        super("Totem", 2, 57);
    }

    @Override
    public void renderComponent() {
        FontUtil.drawString("Totems: " + TextFormatting.WHITE + InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING), this.x, this.y, HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = (int) (FontUtil.getStringWidth("Totems: " + InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING)) + 2);
    }
}

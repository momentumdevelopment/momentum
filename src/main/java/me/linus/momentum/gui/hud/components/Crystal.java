package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.module.modules.client.HUDEditor;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.init.Items;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Crystal extends HUDComponent {
    public Crystal() {
        super("Crystal", 2, 57);
    }

    @Override
    public void renderComponent() {
        FontUtil.drawString("Crystals: " + InventoryUtil.getItemCount(Items.END_CRYSTAL), this.x, this.y, HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = Momentum.fontManager.getCustomFont().getStringWidth("Crystals: " + InventoryUtil.getItemCount(Items.END_CRYSTAL)) + 2;
    }
}

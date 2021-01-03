package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.util.player.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.util.text.TextFormatting;

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
        Momentum.fontManager.getCustomFont().drawStringWithShadow(TextFormatting.GRAY + "Crystals: " + TextFormatting.WHITE + InventoryUtil.getItemCount(Items.END_CRYSTAL), this.x, this.y, -1);
        width = Momentum.fontManager.getCustomFont().getStringWidth("Crystals: " + InventoryUtil.getItemCount(Items.END_CRYSTAL)) + 2;
    }
}

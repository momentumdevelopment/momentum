package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.util.player.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;

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
        Momentum.fontManager.getCustomFont().drawStringWithShadow(TextFormatting.GRAY + "Totems:" + TextFormatting.WHITE + " " + InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING), this.x, this.y, new Color(255, 255, 255).getRGB());
        width = Momentum.fontManager.getCustomFont().getStringWidth("Totems: " + InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING)) + 2;
    }
}

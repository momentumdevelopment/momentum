package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;

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
        int crystals = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
        FontUtil.drawStringWithShadow(TextFormatting.GRAY + "Crystals: " + TextFormatting.WHITE + crystals, this.x, this.y, new Color(255, 255, 255).getRGB());
        width = (int) FontUtil.getStringWidth("Crystals: " + crystals) + 2;
    }
}

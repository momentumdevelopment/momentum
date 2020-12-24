package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.HUDComponentManager;
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

public class Totem extends HUDComponent {
    public Totem() {
        super("Totem", 2, 57);
    }

    @Override
    public void renderComponent() {
        int totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        FontUtil.drawStringWithShadow(TextFormatting.GRAY + "Totems:" + TextFormatting.WHITE + " " + totems, HUDComponentManager.getComponentByName("Totem").getX(), HUDComponentManager.getComponentByName("Totem").getY(), new Color(255, 255, 255).getRGB());
        width = (int) FontUtil.getStringWidth("Totems: " + totems) + 2;
    }
}

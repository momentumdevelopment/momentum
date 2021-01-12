package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.module.modules.client.HUDEditor;
import me.linus.momentum.util.player.rotation.RotationUtil;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Direction extends HUDComponent {
    public Direction() {
        super("Direction", 2, 68);
    }

    @Override
    public void renderComponent() {
        FontUtil.drawString(RotationUtil.getFacing() + TextFormatting.WHITE + " " + RotationUtil.getTowards(), this.x, this.y, HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = (int) (FontUtil.getStringWidth(RotationUtil.getFacing() + " " + RotationUtil.getTowards()) + 2);
    }
}

package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.module.modules.client.HUDEditor;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.world.TickUtil;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class TPS extends HUDComponent {
    public TPS() {
        super("TPS", 2, 57);
    }

    @Override
    public void renderComponent() {
        FontUtil.drawString("TPS " + TextFormatting.WHITE + TickUtil.TPS, this.x, this.y, HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = Momentum.fontManager.getCustomFont().getStringWidth(TickUtil.TPS + " TPS") + 2;
    }
}

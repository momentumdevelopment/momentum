package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.AnchorPoint;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.module.modules.client.HUDEditor;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class FPS extends HUDComponent {
    public FPS() {
        super("FPS", 2, 13, AnchorPoint.BottomRight);
    }

    public static Checkbox average = new Checkbox("Average", false);

    @Override
    public void setup() {
        addSetting(average);
    }

    @Override
    public void renderComponent() {
        FontUtil.drawString("FPS" + TextFormatting.WHITE + " " + Minecraft.getDebugFPS(), this.x, this.y, HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = Momentum.fontManager.getCustomFont().getStringWidth(Minecraft.getDebugFPS() + " FPS") + 2;
    }
}

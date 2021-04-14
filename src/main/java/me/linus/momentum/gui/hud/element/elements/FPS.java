package me.linus.momentum.gui.hud.element.elements;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.element.AnchorPoint;
import me.linus.momentum.util.client.color.ThemeColor;
import me.linus.momentum.module.modules.client.HUD;
import me.linus.momentum.gui.hud.element.HUDElement;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class FPS extends HUDElement {
    public FPS() {
        super("FPS", 2, 13, Category.INFO, AnchorPoint.BottomRight);
    }

    public static Checkbox average = new Checkbox("Average", false);

    @Override
    public void setup() {
        addSetting(average);
    }

    @Override
    public void renderElement() {
        FontUtil.drawString("FPS" + TextFormatting.WHITE + " " + Minecraft.getDebugFPS(), this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = Momentum.fontManager.getCustomFont().getStringWidth(Minecraft.getDebugFPS() + " FPS") + 2;
    }
}

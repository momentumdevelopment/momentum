package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class FPS extends HUDComponent {
    public FPS() {
        super("FPS", 2, 13);
    }

    private static final Checkbox average = new Checkbox("Average", false);

    @Override
    public void setup() {
        addSetting(average);
    }

    @Override
    public void renderComponent() {
        Momentum.fontManager.getCustomFont().drawStringWithShadow(Minecraft.getDebugFPS() + " " + TextFormatting.GRAY + "FPS", this.x, this.y, new Color(255, 255, 255).getRGB());
        width = Momentum.fontManager.getCustomFont().getStringWidth(Minecraft.getDebugFPS() + " FPS") + 2;
    }
}

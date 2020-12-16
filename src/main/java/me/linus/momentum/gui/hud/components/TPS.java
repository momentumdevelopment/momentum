package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.module.modules.hud.FPSModule;
import me.linus.momentum.module.modules.hud.TPSModule;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.world.TickUtil;

import java.awt.*;

public class TPS extends HUDComponent {
    public TPS() {
        super("TPS", 2, 57, null);
    }

    @Override
    public void render() {
        FontUtil.drawStringWithShadow(TickUtil.TPS + " TPS", Momentum.componentManager.getComponentByName("TPS").getX(), Momentum.componentManager.getComponentByName("TPS").getY(), new Color(255, 255, 255).getRGB());
        width = (int) FontUtil.getStringWidth(TickUtil.TPS + " TPS") + 2;
    }

    @Override
    public void mouseHovered(int mouseX, int mouseY) {
        if (isMouseOnComponent(mouseX, mouseY)) colors = new Color(82, 81, 77, 125).getRGB();
        else colors = new Color(117, 116, 110, 125).getRGB();
    }

    public boolean isMouseOnComponent(int x, int y) {
        if (x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height) {
            return true;
        }
        return false;
    }
}

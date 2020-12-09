package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.module.modules.hud.WelcomerModule;
import me.linus.momentum.util.render.FontUtil;

import java.awt.*;

public class Welcomer extends HUDComponent<WelcomerModule> {
    public Welcomer() {
        super("Welcomer", 200, 2, WelcomerModule.INSTANCE);
    }

    @Override
    public void render() {
        FontUtil.drawStringWithShadow("Welcome " + mc.player.getName() + "! :^)", Momentum.componentManager.getComponentByName("Welcomer").getX(), Momentum.componentManager.getComponentByName("Welcomer").getY(), new Color(255, 255, 255).getRGB());
        width = (int) FontUtil.getStringWidth("Welcome " + mc.player.getName() + "! :^)") + 2;
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

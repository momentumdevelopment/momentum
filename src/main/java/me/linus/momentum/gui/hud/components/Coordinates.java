package me.linus.momentum.gui.hud.components;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.module.modules.hud.CoordinatesModule;
import me.linus.momentum.module.modules.hud.FPSModule;
import me.linus.momentum.util.render.FontUtil;

import java.awt.*;

public class Coordinates extends HUDComponent {
    public Coordinates() {
        super("Coordinates", 2, 350, null);
    }

    String coords;

    @Override
    public void render() {
        if (mc.player.dimension == -1) {
            coords = ChatFormatting.GRAY + "XYZ " + ChatFormatting.WHITE + mc.player.getPosition().getX() + ", " + mc.player.getPosition().getY() + ", " + mc.player.getPosition().getZ() +
                    ChatFormatting.GRAY + " [" + ChatFormatting.WHITE + mc.player.getPosition().getX() * 8 + ", " + mc.player.getPosition().getZ() * 8 + ChatFormatting.GRAY + "]";
        } else {
            coords = ChatFormatting.GRAY + "XYZ " + ChatFormatting.WHITE + mc.player.getPosition().getX() + ", " + mc.player.getPosition().getY() + ", " + Math.floor(mc.player.getPosition().getZ()) +
                    ChatFormatting.GRAY + " [" + ChatFormatting.WHITE + mc.player.getPosition().getX() / 8 + ", " + mc.player.getPosition().getZ() / 8 + ChatFormatting.GRAY + "]";
        }

        FontUtil.drawStringWithShadow(coords, Momentum.componentManager.getComponentByName("Coordinates").getX(), Momentum.componentManager.getComponentByName("Coordinates").getY(), new Color(255, 255, 255).getRGB());
        width = (int) FontUtil.getStringWidth(coords) + 2;
    }

    @Override
    public void mouseHovered(int mouseX, int mouseY) {
        if (isMouseOnComponent(mouseX, mouseY))
            colors = new Color(82, 81, 77, 125).getRGB();

        else
            colors = new Color(117, 116, 110, 125).getRGB();
    }

    public boolean isMouseOnComponent(int x, int y) {
        if (x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height) {
            return true;
        }

        return false;
    }
}

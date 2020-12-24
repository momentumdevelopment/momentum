package me.linus.momentum.gui.hud.components;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.hud.HUDComponentManager;
import me.linus.momentum.util.render.FontUtil;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Coordinates extends HUDComponent {
    public Coordinates() {
        super("Coordinates", 2, 350);
    }

    String coords;

    @Override
    public void renderComponent() {
        if (mc.player.dimension == -1) {
            coords = ChatFormatting.GRAY + "XYZ " + ChatFormatting.WHITE + mc.player.getPosition().getX() + ", " + mc.player.getPosition().getY() + ", " + mc.player.getPosition().getZ() +
                    ChatFormatting.GRAY + " [" + ChatFormatting.WHITE + mc.player.getPosition().getX() * 8 + ", " + mc.player.getPosition().getZ() * 8 + ChatFormatting.GRAY + "]";
        } else {
            coords = ChatFormatting.GRAY + "XYZ " + ChatFormatting.WHITE + mc.player.getPosition().getX() + ", " + mc.player.getPosition().getY() + ", " + Math.floor(mc.player.getPosition().getZ()) +
                    ChatFormatting.GRAY + " [" + ChatFormatting.WHITE + mc.player.getPosition().getX() / 8 + ", " + mc.player.getPosition().getZ() / 8 + ChatFormatting.GRAY + "]";
        }

        FontUtil.drawStringWithShadow(coords, HUDComponentManager.getComponentByName("Coordinates").getX(), HUDComponentManager.getComponentByName("Coordinates").getY(), new Color(255, 255, 255).getRGB());
        width = (int) FontUtil.getStringWidth(coords) + 2;
    }
}

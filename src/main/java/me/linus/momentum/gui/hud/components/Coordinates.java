package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.client.system.MathUtil;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/23/2020
 */

public class Coordinates extends HUDComponent {
    public Coordinates() {
        super("Coordinates", 2, 350);
    }

    private static final Mode mode = new Mode("Mode", "Normal", "In-Line");
    private static final Checkbox overWorld = new Checkbox("OverWorld", true);
    private static final Checkbox nether = new Checkbox("Nether", true);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(overWorld);
        addSetting(nether);
    }

    String overWorldCoords;
    String netherCoords;

    @Override
    public void renderComponent() {
        overWorldCoords = mc.player.dimension != -1 ? TextFormatting.GRAY + "XYZ " + TextFormatting.WHITE + MathUtil.roundAvoid(mc.player.posX, 1) + " " + MathUtil.roundAvoid(mc.player.posY, 1) + " " + MathUtil.roundAvoid(mc.player.posZ, 1) : TextFormatting.GRAY + "XYZ " + TextFormatting.WHITE + MathUtil.roundAvoid(mc.player.posX * 8, 1) + " " + MathUtil.roundAvoid(mc.player.posY * 8, 1) + " " + MathUtil.roundAvoid(mc.player.posZ * 8, 1);
        netherCoords = mc.player.dimension == -1 ? TextFormatting.RED + "XYZ " + TextFormatting.WHITE + MathUtil.roundAvoid(mc.player.posX, 1) + " " + MathUtil.roundAvoid(mc.player.posY, 1) + " " + MathUtil.roundAvoid(mc.player.posZ, 1) : TextFormatting.RED + "XYZ " + TextFormatting.WHITE + MathUtil.roundAvoid(mc.player.posX / 8, 1) + " " + MathUtil.roundAvoid(mc.player.posY / 8, 1) + " " + MathUtil.roundAvoid(mc.player.posZ / 8, 1);

        if (mode.getValue() == 0) {
            if (overWorld.getValue())
                Momentum.fontManager.getCustomFont().drawStringWithShadow(netherCoords, this.x, this.y, new Color(255, 255, 255).getRGB());

            if (nether.getValue())
                Momentum.fontManager.getCustomFont().drawStringWithShadow(overWorldCoords, this.x, this.y + 10, new Color(255, 255, 255).getRGB());
        }

        else
            Momentum.fontManager.getCustomFont().drawStringWithShadow((overWorld.getValue() ? overWorldCoords : "") + " " + (nether.getValue() ? netherCoords : ""), this.x, this.y, new Color(255, 255, 255).getRGB());

        width = Momentum.fontManager.getCustomFont().getStringWidth(mode.getValue() == 0 ? overWorldCoords : overWorldCoords + " " + netherCoords) + 2;
        height = mode.getValue() == 0 ? (mc.fontRenderer.FONT_HEIGHT * 2) + 4 : mc.fontRenderer.FONT_HEIGHT + 4;
    }
}

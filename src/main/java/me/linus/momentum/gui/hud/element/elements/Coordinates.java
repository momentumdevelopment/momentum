package me.linus.momentum.gui.hud.element.elements;

import me.linus.momentum.Momentum;
import me.linus.momentum.util.client.color.ThemeColor;
import me.linus.momentum.gui.hud.element.AnchorPoint;
import me.linus.momentum.module.modules.client.HUD;
import me.linus.momentum.gui.hud.element.HUDElement;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/23/2020
 */

public class Coordinates extends HUDElement {
    public Coordinates() {
        super("Coordinates", 2, 350, Category.INFO, AnchorPoint.BottomLeft);
    }

    public static Mode mode = new Mode("Mode", "Normal", "In-Line");
    public static Checkbox overWorld = new Checkbox("OverWorld", true);
    public static Checkbox nether = new Checkbox("Nether", true);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(overWorld);
        addSetting(nether);
    }

    String overWorldCoords;
    String netherCoords;

    @Override
    public void renderElement() {
        overWorldCoords = mc.player.dimension != -1 ? "XYZ " + TextFormatting.WHITE + MathUtil.roundAvoid(mc.player.posX, 1) + " " + MathUtil.roundAvoid(mc.player.posY, 1) + " " + MathUtil.roundAvoid(mc.player.posZ, 1) : TextFormatting.GRAY + "XYZ " + TextFormatting.WHITE + MathUtil.roundAvoid(mc.player.posX * 8, 1) + " " + MathUtil.roundAvoid(mc.player.posY * 8, 1) + " " + MathUtil.roundAvoid(mc.player.posZ * 8, 1);
        netherCoords = mc.player.dimension == -1 ? TextFormatting.RED + "XYZ " + TextFormatting.WHITE + MathUtil.roundAvoid(mc.player.posX, 1) + " " + MathUtil.roundAvoid(mc.player.posY, 1) + " " + MathUtil.roundAvoid(mc.player.posZ, 1) : TextFormatting.RED + "XYZ " + TextFormatting.WHITE + MathUtil.roundAvoid(mc.player.posX / 8, 1) + " " + MathUtil.roundAvoid(mc.player.posY / 8, 1) + " " + MathUtil.roundAvoid(mc.player.posZ / 8, 1);

        if (mode.getValue() == 0) {
            if (overWorld.getValue())
                FontUtil.drawString(netherCoords, this.x, this.y, -1);

            if (nether.getValue())
                FontUtil.drawString(overWorldCoords, this.x, this.y + 10, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        }

        else
            FontUtil.drawString((overWorld.getValue() ? overWorldCoords : "") + " " + (nether.getValue() ? netherCoords : ""), this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);

        width = Momentum.fontManager.getCustomFont().getStringWidth(mode.getValue() == 0 ? overWorldCoords : overWorldCoords + " " + netherCoords) + 2;
        height = mode.getValue() == 0 ? (mc.fontRenderer.FONT_HEIGHT * 2) + 4 : mc.fontRenderer.FONT_HEIGHT + 4;
    }
}

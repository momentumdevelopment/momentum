package me.linus.momentum.util.render;

import me.linus.momentum.Momentum;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.modules.client.ClickGui;

/**
 * @author linustouchtips
 * @since 11/29/2020
 */

public class FontUtil implements MixinInterface {

    public static void drawStringWithShadow(String text, float x, float y, int color) {
        switch (ClickGui.font.getValue()) {
            case 0:
                Momentum.latoFont.drawStringWithShadow(text, x, y, color);
                break;
            case 1:
                Momentum.ubuntuFont.drawStringWithShadow(text, x, y, color);
                break;
            case 2:
                Momentum.verdanaFont.drawStringWithShadow(text, x, y, color);
                break;
            case 3:
                Momentum.comfortaaFont.drawStringWithShadow(text, x, y, color);
                break;
            case 4:
                Momentum.comicFont.drawStringWithShadow(text, x, y, color);
                break;
            case 5:
                mc.fontRenderer.drawStringWithShadow(text, (int) x, (int) y, color);
                break;
        }
    }


    public static void drawString(String text, float x, float y, int color) {
        switch (ClickGui.font.getValue()) {
            case 0:
                Momentum.latoFont.drawString(text, x, y, color);
                break;
            case 1:
                Momentum.ubuntuFont.drawString(text, x, y, color);
                break;
            case 2:
                Momentum.verdanaFont.drawString(text, x, y, color);
                break;
            case 3:
                Momentum.comfortaaFont.drawString(text, x, y, color);
                break;
            case 4:
                Momentum.comicFont.drawString(text, x, y, color);
                break;
            case 5:
                mc.fontRenderer.drawString(text, (int) x, (int) y, color);
                break;
        }
    }

    public static float getStringWidth(String text) {
        if (ClickGui.font.getValue() == 0)
            return Momentum.latoFont.getStringWidth(text);

        if (ClickGui.font.getValue() == 1)
            return Momentum.verdanaFont.getStringWidth(text);

        if (ClickGui.font.getValue() == 2)
           return Momentum.comfortaaFont.getStringWidth(text);

        if (ClickGui.font.getValue() == 3)
           return Momentum.comicFont.getStringWidth(text);

        else
           return mc.fontRenderer.getStringWidth(text);
    }
}

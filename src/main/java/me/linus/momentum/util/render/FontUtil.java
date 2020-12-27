package me.linus.momentum.util.render;

import me.linus.momentum.Momentum;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.modules.client.ClickGUI;

/**
 * @author linustouchtips
 * @since 11/29/2020
 */

public class FontUtil implements MixinInterface {

    /**
     * string rendering
     */

    public static void drawStringWithShadow(String text, float x, float y, int color) {
        switch (ClickGUI.font.getValue()) {
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
        switch (ClickGUI.font.getValue()) {
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

    /**
     * font info
     */

    public static float getStringWidth(String text) {
        switch (ClickGUI.font.getValue()) {
            case 0:
                return Momentum.latoFont.getStringWidth(text);
            case 1:
                return Momentum.ubuntuFont.getStringWidth(text);
            case 2:
                return Momentum.verdanaFont.getStringWidth(text);
            case 3:
                return Momentum.comfortaaFont.getStringWidth(text);
            case 4:
                return Momentum.comicFont.getStringWidth(text);
            case 5:
                return mc.fontRenderer.getStringWidth(text);
        }

        return -1;
    }

    public static float getFontHeight() {
        switch (ClickGUI.font.getValue()) {
            case 0:
                return Momentum.latoFont.FONT_HEIGHT;
            case 1:
                return Momentum.ubuntuFont.FONT_HEIGHT;
            case 2:
                return Momentum.verdanaFont.FONT_HEIGHT;
            case 3:
                return Momentum.comfortaaFont.FONT_HEIGHT;
            case 4:
                return Momentum.comicFont.FONT_HEIGHT;
            case 5:
                return mc.fontRenderer.FONT_HEIGHT;
        }

        return -1;
    }
}

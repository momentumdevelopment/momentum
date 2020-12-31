package me.linus.momentum.util.render;

import me.linus.momentum.Momentum;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.util.client.font.FontRender;

import java.awt.*;
import java.io.InputStream;

/**
 * @author linustouchtips
 * @since 11/29/2020
 */

public class FontUtil implements MixinInterface {
    
    public static FontRender verdana = null;
    public static FontRender lato = null;
    public static FontRender ubuntu = null;
    public static FontRender comfortaa = null;
    public static FontRender comicsans = null;

    public void load() {
        try {
            this.lato = new FontRender(FontUtil.getFont("Lato.ttf", 40.0f));
            this.comfortaa = new FontRender(FontUtil.getFont("comfortaa.ttf", 40.0f));
            this.comicsans = new FontRender(FontUtil.getFont("comic-sans.ttf", 40.0f));
            this.verdana = new FontRender(FontUtil.getFont("Verdana.ttf", 40.0f));
            this.ubuntu = new FontRender(FontUtil.getFont("Ubuntu.ttf", 40.0f));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Font getFont(String fontName, float size) {
        try {
            InputStream inputStream = FontUtil.class.getResourceAsStream("/assets/momentum/fonts/" + fontName);
            Font awtClientFont = Font.createFont(0, inputStream);
            awtClientFont = awtClientFont.deriveFont(0, size);
            inputStream.close();

            return awtClientFont;
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("default", 0, (int)size);
        }
    }

    public FontRender getCustomFont() {
        switch (ClickGUI.font.getValue()) {
            case 0:
                return this.lato;
            case 1:
                return this.ubuntu;
            case 2:
                return this.verdana;
            case 3:
                return this.comfortaa;
            case 4:
                return this.comicsans;
        }

        return this.lato;
    }

    /**
     * font info
     */

    public static float getStringWidth(String text) {
        return Momentum.fontManager.getCustomFont().getStringWidth(text);
    }

    public static float getFontHeight() {
        return Momentum.fontManager.getCustomFont().FONT_HEIGHT;
    }
}

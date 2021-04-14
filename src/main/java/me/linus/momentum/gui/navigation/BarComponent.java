package me.linus.momentum.gui.navigation;

import me.linus.momentum.managers.ScreenManager;
import me.linus.momentum.managers.ScreenManager.Screen;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.color.ThemeColor;
import me.linus.momentum.util.render.GUIUtil;
import me.linus.momentum.util.render.Render2DUtil;
import me.linus.momentum.util.render.builder.Render2DBuilder.Render2DMode;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 03/10/21
 */

public class BarComponent implements MixinInterface {

    public ResourceLocation resourceLocation;
    public GuiScreen guiScreen;
    public Screen screen;

    public BarComponent(ResourceLocation resourceLocation, GuiScreen guiScreen, Screen screen) {
        this.resourceLocation = resourceLocation;
        this.guiScreen = guiScreen;
        this.screen = screen;
    }

    public void drawBarImage(int x, int y) {
        int color = new Color(18, 18, 18, 0).getRGB();
        if (GUIUtil.mouseOver(x, y, x + 26, y + 26)) {
            color = new Color(23, 23, 23, 40).getRGB();

            if (GUIUtil.ldown)
                ScreenManager.setScreen(guiScreen);
        }

        Render2DUtil.drawRect(x - 2, y - 2, x + 26, y + 28, 0, ScreenManager.screen.equals(this.screen) ? ThemeColor.COLOR : color, -1 , false, Render2DMode.Normal);

        GlStateManager.enableAlpha();
        mc.getTextureManager().bindTexture(this.resourceLocation);

        // mc is stupid
        GlStateManager.color(1, 1, 1, 1);

        GL11.glPushMatrix();
        GuiScreen.drawScaledCustomSizeModalRect(x, y, 0, 0, 256,256,26,26,256,256);
        GL11.glPopMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
    }
}

package me.linus.momentum.util.render.builder;

import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

/**
 * @author linustouchtips
 * @since 02/07/2021
 */

public class Render2DBuilder implements MixinInterface {

    public static void prepareScissor(int x, double y, int width, double height) {
        GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);
        GL11.glScissor(x * new ScaledResolution(mc).getScaleFactor(), (int) (new ScaledResolution(mc).getScaledHeight() - height) * new ScaledResolution(mc).getScaleFactor(), (width - x) * new ScaledResolution(mc).getScaleFactor(), (int) (height - y) * new ScaledResolution(mc).getScaleFactor());
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }

    public static void restoreScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopAttrib();
    }

    public static void prepareScale(float factorX, float factorY) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(factorX, factorY, 1);
    }

    public static void restoreScale() {
        GlStateManager.popMatrix();
    }

    public static void scaleProportion(int x, int y, float factorX, float factorY) {
        GlStateManager.scale(factorX, factorY, 1);
        GlStateManager.translate(x, y, 1);
    }

    public static void rotate(int x, int y, int z, int angle) {
        GlStateManager.rotate(x, y, z, angle);
    }

    public static void translate(int x, int y) {
        GlStateManager.translate(x, y, 1);
    }

    public enum Render2DMode {
        Normal,
        Border,
        Both
    }
}

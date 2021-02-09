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

    public static void prepareScissor(int x, int y, int x2, int y2) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        GL11.glScissor((int) (x * (float) factor), (int)(((float) scale.getScaledHeight() - y2) * (float) factor), (int) ((x2 - x) * (float) factor), (int) ((y2 - y) * (float) factor));
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

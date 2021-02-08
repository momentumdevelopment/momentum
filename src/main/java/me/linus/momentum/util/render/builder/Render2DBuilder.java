package me.linus.momentum.util.render.builder;

import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.client.gui.ScaledResolution;
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

    public static void scaleProportion(int x, int y, int z, float factor) {
        GL11.glScalef(factor, factor, factor);
        GL11.glTranslatef(x, y, z);
    }

    public static void rotate(int x, int y, int z, int angle) {
        GL11.glRotatef(x, y, z, angle);
    }
}

package me.linus.momentum.module.modules.render.esp.modes;

import me.linus.momentum.module.modules.render.esp.ESPMode;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.opengl.GL11.GL_POLYGON_OFFSET_FILL;

/**
 * @author linustouchtips
 * @since 01/07/2021
 */

public class Textured extends ESPMode {

    @Override
    public void drawESPPre(double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        GL11.glEnable(GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(1.0f, -1100000.0f);
    }

    @Override
    public void drawESPPost(double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        GL11.glPolygonOffset(1.0f, 1000000.0f);
        GL11.glDisable(GL_POLYGON_OFFSET_FILL);
    }
}

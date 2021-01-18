package me.linus.momentum.module.modules.render.esp.modes;

import me.linus.momentum.module.modules.render.esp.ESPMode;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author linustouchtips
 * @since 01/07/2021
 */

public class Textured extends ESPMode {

    @Override
    public void drawESPPre(double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        GL11.glEnable(32823);
        GL11.glPolygonOffset(1.0f, -1100000.0f);
    }

    @Override
    public void drawESPPost(double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        GL11.glPolygonOffset(1.0f, 1000000.0f);
        GL11.glDisable(32823);
    }
}

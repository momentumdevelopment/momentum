package me.linus.momentum.mixin.mixins;

import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.render.NoRender;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {
    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void hurtCameraEffect(float ticks, CallbackInfo info){
        if (ModuleManager.getModuleByName("NoRender").isEnabled() && NoRender.hurtCamera.getValue())
            info.cancel();
    }
}

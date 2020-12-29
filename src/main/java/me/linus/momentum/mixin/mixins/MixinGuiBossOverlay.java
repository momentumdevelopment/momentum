package me.linus.momentum.mixin.mixins;

import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.render.NoRender;
import net.minecraft.client.gui.GuiBossOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

@Mixin(GuiBossOverlay.class)
public class MixinGuiBossOverlay {

    @Inject(method = "renderBossHealth", at = @At("HEAD"), cancellable = true)
    private void renderBossHealth(CallbackInfo ci) {
        if (ModuleManager.getModuleByName("NoRender").isEnabled() && NoRender.bossBar.getValue())
            ci.cancel();
    }
}

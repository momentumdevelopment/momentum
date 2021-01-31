package me.linus.momentum.mixin.mixins;

import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.render.NoRender;
import net.minecraft.client.renderer.entity.RenderWitherSkull;
import net.minecraft.entity.projectile.EntityWitherSkull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderWitherSkull.class)
public class MixinRenderWitherSkull {

    @Inject(method = "doRender", at = @At("HEAD"), cancellable = true)
    public void doRender(EntityWitherSkull entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (ModuleManager.getModuleByName("NoRender").isEnabled() && NoRender.witherSkulls.getValue())
            ci.cancel();
    }
}

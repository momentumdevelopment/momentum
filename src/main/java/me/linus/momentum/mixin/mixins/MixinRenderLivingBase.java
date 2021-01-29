package me.linus.momentum.mixin.mixins;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.render.ESP;
import me.linus.momentum.module.modules.render.NoRender;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

@Mixin(RenderLivingBase.class)
public abstract class MixinRenderLivingBase<T extends EntityLivingBase> extends Render<T> implements MixinInterface {
    protected MixinRenderLivingBase() {
        super(null);
    }

    @Shadow
    protected ModelBase mainModel;

    @Shadow
    protected abstract boolean isVisible(EntityLivingBase entityLivingBaseIn);

    @Inject(method = "renderModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void renderModelWrapper(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, CallbackInfo ci) {
        try {
            if (ModuleManager.getModuleByName("ESP").isEnabled() && ESP.mode.getValue() == 0)
                ESP.espMode.drawESPMixin(mainModel, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        } catch (Exception exception) {

        }
    }

    @Overwrite
    protected void renderModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        if (mc.player.getDistance(entitylivingbaseIn) < 1 && ModuleManager.getModuleByName("NoRender").isEnabled() && NoRender.noCluster.getValue())
            GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);

        if (!this.bindEntityTexture(entitylivingbaseIn))
            return;

        try {
            if (ModuleManager.getModuleByName("ESP").isEnabled()) {
                if ((ESP.mode.getValue() == 3) || (ESP.mode.getValue() == 4))
                    GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);

                ESP.espMode.drawESPMixin(mainModel, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

                if (!(ESP.mode.getValue() == 3) || !(ESP.mode.getValue() == 4))
                    mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            }
        } catch (Exception exception) {

        }

        mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
    }
}

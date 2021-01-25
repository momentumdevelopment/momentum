package me.linus.momentum.mixin.mixins;

import me.linus.momentum.event.events.player.RotationEvent;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.render.ESP;
import me.linus.momentum.module.modules.render.NoRender;
import me.linus.momentum.util.player.rotation.RotationUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
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
        try {
            if (!this.bindEntityTexture(entitylivingbaseIn))
                return;

            if ((ESP.mode.getValue() == 3) || (ESP.mode.getValue() == 4) && ModuleManager.getModuleByName("ESP").isEnabled())
                GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);

            if (mc.player.getDistance(entitylivingbaseIn) < 1 && ModuleManager.getModuleByName("NoRender").isEnabled() && NoRender.noCluster.getValue())
                GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);

            if (ModuleManager.getModuleByName("ESP").isEnabled())
                ESP.espMode.drawESPMixin(mainModel, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

            if (!(ESP.mode.getValue() == 3) || !(ESP.mode.getValue() == 4) || !ModuleManager.getModuleByName("ESP").isEnabled())
                mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        } catch (Exception exception) {

        }
    }

    @Overwrite
    protected void applyRotations(T entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
        RotationEvent event = new RotationEvent();
        MinecraftForge.EVENT_BUS.post(event);

        GlStateManager.rotate(180.0F - (event.isCanceled() ? event.getYaw() : rotationYaw), 0.0F, 1.0F, 0.0F);
        if (entityLiving.deathTime > 0) {
            float f = ((float)entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            GlStateManager.rotate(f * 90.0f, 0.0F, 0.0F, 1.0F);
        } else {
            String s = TextFormatting.getTextWithoutFormattingCodes(entityLiving.getName());
            if (s != null && ("Dinnerbone".equals(s) || "Grumm".equals(s)) && (!(entityLiving instanceof EntityPlayer) || ((EntityPlayer)entityLiving).isWearing(EnumPlayerModelParts.CAPE))) {
                GlStateManager.translate(0.0F, entityLiving.height + 0.1F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }
}

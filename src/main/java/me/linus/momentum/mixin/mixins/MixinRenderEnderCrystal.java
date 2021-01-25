package me.linus.momentum.mixin.mixins;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.render.ESP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

@Mixin(RenderEnderCrystal.class)
public abstract class MixinRenderEnderCrystal implements MixinInterface {

    @Shadow
    public ModelBase modelEnderCrystal;

    @Shadow
    public ModelBase modelEnderCrystalNoBase;

    @Final
    @Shadow
    private static ResourceLocation ENDER_CRYSTAL_TEXTURES;

    @Shadow
    public abstract void doRender(EntityEnderCrystal p0, double p1, double p2, double p3, float p4, float p5);

    @Redirect(method = { "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void render1(ModelBase modelBase, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (ModuleManager.getModuleByName("ESP").isEnabled() && ESP.crystals.getValue() && (ESP.mode.getValue() == 3 || ESP.mode.getValue() == 4))
            return;

        else
            modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Redirect(method = { "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V", ordinal = 1))
    private void render2(ModelBase modelBase, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (ModuleManager.getModuleByName("ESP").isEnabled() && ESP.crystals.getValue() && (ESP.mode.getValue() == 3 || ESP.mode.getValue() == 4))
            return;

        else
            modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Inject(method = { "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V" }, at = { @At("RETURN") }, cancellable = true)
    public void IdoRender(EntityEnderCrystal entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo callback) {
        try {
            if (ModuleManager.getModuleByName("ESP").isEnabled() && ESP.crystals.getValue())
                ESP.espMode.drawESPCrystal(modelEnderCrystal, modelEnderCrystalNoBase, entity, x, y, z, entityYaw, partialTicks, callback, ENDER_CRYSTAL_TEXTURES);
        } catch (Exception exception) {

        }
    }
}

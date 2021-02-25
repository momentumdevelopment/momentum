package me.linus.momentum.mixin.mixins;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.modules.render.Skeleton;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

@Mixin(ModelPlayer.class)
public class MixinModelPlayer implements MixinInterface {

    @Inject(method = "setRotationAngles", at = @At("RETURN"))
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo info) {
        if (mc.world != null && mc.player != null && entityIn instanceof EntityPlayer)
            Skeleton.addEntity((EntityPlayer) entityIn, (ModelPlayer) (Object) this);
    }
}

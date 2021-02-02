package me.linus.momentum.mixin.mixins;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.combat.AutoCrystal;
import me.linus.momentum.module.modules.render.SkyColor;
import me.linus.momentum.util.combat.crystal.CrystalManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author linustouchtips
 * @since 02/01/2021
 */

@Mixin(World.class)
public class MixinWorld implements MixinInterface {

    @Inject(method = "onEntityRemoved", at = @At("HEAD"), cancellable = true)
    public void onEntityRemoved(Entity entity, CallbackInfo info) {
        if (entity instanceof EntityEnderCrystal && mc.player.getDistance(entity) < AutoCrystal.breakRange.getValue())
            CrystalManager.swings = 0;
    }

    @Inject(method = "getSkyColor", at = @At("HEAD"), cancellable = true)
    public void getSkyColor(Entity entityIn, float partialTicks, CallbackInfoReturnable<Vec3d> callback) {
        if (ModuleManager.getModuleByName("SkyColor").isEnabled()) {
            callback.cancel();
            callback.setReturnValue(new Vec3d(SkyColor.skyPicker.getColor().getRed(), SkyColor.skyPicker.getColor().getGreen(), SkyColor.skyPicker.getColor().getBlue()));
        }
    }
}

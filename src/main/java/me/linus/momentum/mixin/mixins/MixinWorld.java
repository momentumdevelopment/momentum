package me.linus.momentum.mixin.mixins;

import me.linus.momentum.managers.CrystalManager;
import me.linus.momentum.managers.GearManager;
import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.modules.combat.AutoCrystal;
import me.linus.momentum.module.modules.render.SkyColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.player.EntityPlayer;
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
        if (entity instanceof EntityEnderCrystal && mc.player.getDistance(entity) < AutoCrystal.breakRange.getValue()) {
            CrystalManager.swings = 0;
            CrystalManager.placements = 0;
            CrystalManager.cleared = true;
        }
    }

    @Inject(method = "onEntityAdded", at = @At(value = "HEAD"))
    public void onEntityAdded(Entity entityIn, CallbackInfo info) {
        try {
            if (entityIn instanceof EntityExpBottle) {
                for (EntityPlayer entityPlayer : mc.world.playerEntities) {
                    if (entityPlayer.getDistance(entityIn) < 2) {
                        if (GearManager.expMap.containsKey(entityPlayer))
                            GearManager.expMap.put(entityPlayer, GearManager.expMap.get(entityPlayer) + 1);
                        else
                            GearManager.expMap.put(entityPlayer, 1);
                    }
                }
            }

            if (entityIn instanceof EntityEnderCrystal) {
                for (EntityPlayer entityPlayer : mc.world.playerEntities) {
                    if (entityPlayer.getDistance(entityIn) < 2) {
                        if (GearManager.crystalMap.containsKey(entityPlayer))
                            GearManager.crystalMap.put(entityPlayer, GearManager.crystalMap.get(entityPlayer) + 1);
                        else
                            GearManager.crystalMap.put(entityPlayer, 1);
                    }
                }
            }
        } catch (Exception ignored) {

        }
    }

    @Inject(method = "getSkyColor", at = @At("HEAD"), cancellable = true)
    public void getSkyColor(Entity entityIn, float partialTicks, CallbackInfoReturnable<Vec3d> callback) {
        if (ModuleManager.getModuleByName("SkyColor").isEnabled()) {
            callback.cancel();
            callback.setReturnValue(new Vec3d(SkyColor.skyPicker.getColor().getRed(), SkyColor.skyPicker.getColor().getGreen(), SkyColor.skyPicker.getColor().getBlue()));
        }
    }
}

package me.linus.momentum.mixin.mixins;

import me.linus.momentum.managers.ModuleManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

@Mixin(Entity.class)
public class MixinEntity {

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaking()Z"))
    public boolean isSneaking(Entity entity) {
        return ModuleManager.getModuleByName("SafeWalk").isEnabled() || entity.isSneaking();
    }
}

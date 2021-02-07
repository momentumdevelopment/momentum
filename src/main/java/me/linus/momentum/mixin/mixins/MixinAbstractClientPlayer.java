package me.linus.momentum.mixin.mixins;

import me.linus.momentum.Momentum;
import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.module.modules.render.ESP;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer {

    @Shadow
    @Nullable
    protected abstract NetworkPlayerInfo getPlayerInfo();

    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
    public void getLocationCape(CallbackInfoReturnable<ResourceLocation> cir){
        if (ModuleManager.getModuleByName("Capes").isEnabled() && Momentum.capeManager.hasCape(getPlayerInfo().getGameProfile().getId()))
            cir.setReturnValue(new ResourceLocation("momentum:momentum_cape.png"));
    }

    @Inject(method = "getLocationSkin", at = @At("HEAD"), cancellable = true)
    public void getLocationSkin(CallbackInfoReturnable<ResourceLocation> cir) {
        if (ModuleManager.getModuleByName("ESP").isEnabled() && ESP.mode.getValue() == 6)
            cir.setReturnValue(new ResourceLocation("momentum:texturechams.png"));
    }
}

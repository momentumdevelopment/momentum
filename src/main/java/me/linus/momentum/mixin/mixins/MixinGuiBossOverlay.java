package me.linus.momentum.mixin.mixins;

import me.linus.momentum.event.events.world.BossBarEvent;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraftforge.common.MinecraftForge;
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
        BossBarEvent event = new BossBarEvent();
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}

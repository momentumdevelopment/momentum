package me.linus.momentum.mixin.mixins;

import me.linus.momentum.event.events.world.DamageBlockEvent;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.player.Reach;
import me.linus.momentum.module.modules.player.Swing;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

@Mixin(value = PlayerControllerMP.class/*, priority = 999*/)
public class MixinPlayerControllerMP implements MixinInterface {

    @Inject(method = "resetBlockRemoving", at = @At(value = "HEAD"), cancellable = true)
    private void resetBlock(CallbackInfo ci) {
        if (ModuleManager.getModuleByName("EntityMine").isEnabled() && Swing.noReset.getValue())
            ci.cancel();
    }

    @Inject(method = "onPlayerDamageBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", at = @At("HEAD"), cancellable = true)
    private void onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> cir){
        DamageBlockEvent event = new DamageBlockEvent(posBlock, directionFacing);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled())
            cir.setReturnValue(false);
    }

    @Inject(method = "getBlockReachDistance", at = @At("RETURN"), cancellable = true)
    private void getReachDistanceHook(final CallbackInfoReturnable<Float> distance) {
        if (ModuleManager.getModuleByName("Reach").isEnabled()) {
            distance.setReturnValue((float) Reach.distance.getValue());
        }
    }
}

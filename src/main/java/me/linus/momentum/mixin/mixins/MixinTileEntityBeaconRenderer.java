package me.linus.momentum.mixin.mixins;

import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.render.NoRender;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.tileentity.TileEntityBeacon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityBeaconRenderer.class)
public class MixinTileEntityBeaconRenderer {

    @Inject(method = "render", at = @At("INVOKE"), cancellable = true)
    private void renderBeacon(TileEntityBeacon te, double x, double y, double z, float partialTicks, int destroyStage, float alpha, CallbackInfo info) {
        if (ModuleManager.getModuleByName("NoRender").isEnabled() && NoRender.beacons.getValue())
            info.cancel();
    }
}

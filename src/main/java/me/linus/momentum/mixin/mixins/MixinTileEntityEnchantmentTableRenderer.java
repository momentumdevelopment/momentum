package me.linus.momentum.mixin.mixins;

import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.render.NoRender;
import net.minecraft.client.renderer.tileentity.TileEntityEnchantmentTableRenderer;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityEnchantmentTableRenderer.class)
public class MixinTileEntityEnchantmentTableRenderer {

    @Inject(method = "render", at = @At(value = "INVOKE"), cancellable = true)
    private void renderEnchantingTableBook(TileEntityEnchantmentTable te, double x, double y, double z, float partialTicks, int destroyStage, float alpha, CallbackInfo info) {
        if (ModuleManager.getModuleByName("NoRender").isEnabled() && NoRender.enchantmentTables.getValue())
            info.cancel();
    }
}

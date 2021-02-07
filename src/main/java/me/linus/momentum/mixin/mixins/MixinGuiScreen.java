package me.linus.momentum.mixin.mixins;

import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.module.modules.render.ItemPreview;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

@Mixin(GuiScreen.class)
public class MixinGuiScreen {

    @Inject(method = "renderToolTip", at = @At("HEAD"), cancellable = true)
    public void renderToolTip(ItemStack stack, int x, int y, CallbackInfo info) {
        if (ModuleManager.getModuleByName("ItemPreview").isEnabled() && ItemPreview.shulkers.getValue())
            ItemPreview.tooltipShulker(stack, x, y, info);

        if (ModuleManager.getModuleByName("ItemPreview").isEnabled() && ItemPreview.maps.getValue())
            ItemPreview.tooltipMap(stack, x, y, info);
    }
}

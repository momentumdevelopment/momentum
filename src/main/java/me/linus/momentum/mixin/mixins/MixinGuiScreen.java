package me.linus.momentum.mixin.mixins;

import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.module.modules.render.ItemPreview;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

@Mixin(GuiScreen.class)
public class MixinGuiScreen implements MixinInterface {

    /*
    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo info) {
        if (ClickGUI.blurEffect.getValue())
            mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
    }

    @Inject(method = "onGuiClosed", at = @At("HEAD"), cancellable = true)
    public void onGuiClosed(CallbackInfo info) {
        if (ClickGUI.blurEffect.getValue()) {
            try {
                mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            } catch (Exception ignored) {

            }
        }
    }

     */

    @Inject(method = "renderToolTip", at = @At("HEAD"), cancellable = true)
    public void renderToolTip(ItemStack stack, int x, int y, CallbackInfo info) {
        if (ModuleManager.getModuleByName("ItemPreview").isEnabled() && ItemPreview.shulkers.getValue())
            ItemPreview.tooltipShulker(stack, x, y, info);

        if (ModuleManager.getModuleByName("ItemPreview").isEnabled() && ItemPreview.maps.getValue())
            ItemPreview.tooltipMap(stack, x, y, info);
    }
}

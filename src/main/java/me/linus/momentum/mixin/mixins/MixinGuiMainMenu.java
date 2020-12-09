package me.linus.momentum.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.linus.momentum.Momentum;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

/**
 * @author bon
 * @since 11/12/20
 */

//TODO: Figure out how to properly do mixin priority for good compatibility
@Mixin(value = GuiMainMenu.class, priority = 1006)
public class MixinGuiMainMenu extends GuiScreen {
	
	String s = "Momentum Client";
	String s2 = "Version " + Momentum.VERSION;
	
	@Inject(
		method = "drawScreen",
		at = @At("TAIL"),
		cancellable = true)
	public void drawText(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		mc.fontRenderer.drawStringWithShadow(s, 2, mc.displayHeight - 70, -1);
		mc.fontRenderer.drawStringWithShadow(s2, 2, mc.displayHeight - 60, -1);
	}
	
}

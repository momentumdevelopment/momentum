package me.linus.momentum.mixin.mixins;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author bon
 * @since 11/12/20
 */

// TODO: Figure out how to properly do mixin priority for good compatibility
@Mixin(value = GuiMainMenu.class/*, priority = 1006*/)
public class MixinGuiMainMenu extends GuiScreen {

	@Inject(method = "drawScreen", at = @At("TAIL"), cancellable = true)
	public void drawText(int mouseX, int mouseY, float partialTicks, CallbackInfo callbackInfo) {
		FontUtil.drawString(Momentum.NAME + TextFormatting.WHITE + " " + Momentum.VERSION, 2, 2, ThemeColor.BRIGHT);
	}
}

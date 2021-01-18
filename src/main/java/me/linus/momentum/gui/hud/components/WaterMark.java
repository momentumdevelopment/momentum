package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.module.modules.client.HUDEditor;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class WaterMark extends HUDComponent {
	public WaterMark() {
		super("WaterMark", 2, 2);
	}

	public static Slider scale = new Slider("Scale", 0.0D, 1.0D, 10.0D, 1);

	@Override
	public void setup() {
		addSetting(scale);
	}

	@Override
	public void renderComponent() {
		GlStateManager.pushMatrix();
		GlStateManager.scale(scale.getValue(), scale.getValue(), scale.getValue());
		FontUtil.drawString("Momentum " + TextFormatting.WHITE + Momentum.VERSION, this.x, this.y, HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : -1);
		GlStateManager.popMatrix();

		width = (int) (scale.getValue() * Momentum.fontManager.getCustomFont().getStringWidth("Momentum " + TextFormatting.WHITE + Momentum.VERSION) + 2);
		height = (int) (scale.getValue() * (mc.fontRenderer.FONT_HEIGHT + 3));
	}
}

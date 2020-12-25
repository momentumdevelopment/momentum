package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.module.modules.client.Colors;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;

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
		FontUtil.drawStringWithShadow("Momentum " + TextFormatting.WHITE + Momentum.VERSION, this.x, this.y, new Color((int) Colors.r.getValue(), (int)  Colors.g.getValue() ,(int)  Colors.b.getValue()).getRGB());
		GlStateManager.popMatrix();

		width = (int) (scale.getValue() * FontUtil.getStringWidth("Momentum " + TextFormatting.WHITE + Momentum.VERSION) + 2);
		height = (int) (scale.getValue() * (mc.fontRenderer.FONT_HEIGHT + 3));
	}
}

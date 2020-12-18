package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.module.modules.client.Colors;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class WaterMark extends HUDComponent {
	public WaterMark() {
		super("WaterMark", 2, 2, null);
	}

	@Override
	public void render() {
		FontUtil.drawStringWithShadow("Momentum " + TextFormatting.WHITE + Momentum.VERSION, Momentum.componentManager.getComponentByName("WaterMark").getX(), Momentum.componentManager.getComponentByName("WaterMark").getY(), new Color((int) Colors.r.getValue(), (int)  Colors.g.getValue() ,(int)  Colors.b.getValue()).getRGB());
		width = (int) (FontUtil.getStringWidth("Momentum " + TextFormatting.WHITE + Momentum.VERSION) + 2);
	}

	@Override
	public void mouseHovered(int mouseX, int mouseY) {
		if (isMouseOnComponent(mouseX, mouseY)) colors = new Color(82, 81, 77, 125).getRGB();
		else colors = new Color(117, 116, 110, 125).getRGB();
	}

	public boolean isMouseOnComponent(int x, int y) {
		return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
	}
}

package me.linus.momentum.gui.theme;

import me.linus.momentum.module.modules.client.Colors;
import me.linus.momentum.util.client.ColorUtil;

/**
 * @author bon & linustouchtips
 * @since 12/17/20
 */

public class ThemeColor {
	public static int COLOR;
	public static int BRIGHT;
	public static int GRAY;

	public static void updateColors() {
		GRAY = ColorUtil.toRGBA(112, 112, 112, 255);
		BRIGHT = ColorUtil.toRGBA(Colors.clientPicker.getColor().getRed(), Colors.clientPicker.getColor().getGreen(), Colors.clientPicker.getColor().getBlue(), 255);
		COLOR = Colors.rainbow.getValue() ? ColorUtil.rainbow(1) : Colors.clientPicker.getColor().getRGB();
	}
}

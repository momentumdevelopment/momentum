package me.linus.momentum.gui.theme;

import me.linus.momentum.module.modules.client.Colors;
import me.linus.momentum.util.client.ColorUtil;

/**
 * @author bon & linustouchtips
 * @since 12/17/20
 */

public class ThemeColor {
	public static boolean GRADIENT;
	public static int COLOR;
	public static int BRIGHT;
	public static int GRAY;

	public static void updateColors() {
		GRAY = ColorUtil.toRGBA(112, 112, 112, 255);
		BRIGHT = ColorUtil.toRGBA(Colors.clientPicker.getColor().getRed(), Colors.clientPicker.getColor().getGreen(), Colors.clientPicker.getColor().getBlue(), 255);

		if (Colors.rainbow.getValue()) {
			if (Colors.gradient.getValue())
				GRADIENT = true;

			else {
				GRADIENT = false;
				COLOR = ColorUtil.rainbow(1);
			}
		}

		else if (!Colors.rainbow.getValue())
			COLOR = ColorUtil.toRGBA(Colors.clientPicker.getColor().getRed(), Colors.clientPicker.getColor().getGreen(), Colors.clientPicker.getColor().getBlue(), Colors.clientPicker.getColor().getAlpha());
	}
}

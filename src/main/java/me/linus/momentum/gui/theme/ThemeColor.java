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
	public static int BLACK;
	public static int DARKBLACK;

	public static void updateColors() {
		GRAY = ColorUtil.toRGBA(112, 112, 112, 255);
		BLACK = ColorUtil.toRGBA(0, 0, 0, 70);
		DARKBLACK = ColorUtil.toRGBA(0, 0, 0, 120);
		BRIGHT = ColorUtil.toRGBA((int) Colors.r.getValue(), (int) Colors.g.getValue(), (int) Colors.b.getValue(), 255);

		if (Colors.rainbow.getValue()) {
			if (Colors.gradient.getValue())
				GRADIENT = true;

			else {
				GRADIENT = false;
				COLOR = ColorUtil.rainbow(1);
			}
		}

		else if (!Colors.rainbow.getValue())
			COLOR = ColorUtil.toRGBA((int) Colors.r.getValue(), (int) Colors.g.getValue(), (int) Colors.b.getValue(), (int) Colors.a.getValue());
	}
}

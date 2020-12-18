package me.linus.momentum.gui.theme;

import me.linus.momentum.module.modules.client.Colors;
import me.linus.momentum.util.client.color.ColorUtil;

/**
 * @author bon
 * @since 12/17/20
 */

public class Color {
	public static boolean GRADIENT;
	public static int COLOR;

	public static void updateColors() {
		if (Colors.rainbow.getValue()) {
			if (Colors.gradient.getValue()) {
				GRADIENT = true;
			} else {
				GRADIENT = false;
				COLOR = ColorUtil.rainbow(1);
			}
		} else if (!Colors.rainbow.getValue()) {
			COLOR = ColorUtil.toRGBA((int) Colors.r.getValue(), (int) Colors.g.getValue(), (int) Colors.b.getValue(), (int) Colors.a.getValue());
		}
	}
}

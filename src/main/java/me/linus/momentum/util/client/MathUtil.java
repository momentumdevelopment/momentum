package me.linus.momentum.util.client;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author bon & linustouchtips
 * @since 11/26/2020
 */

public class MathUtil {

	public static double roundDouble(double number, int scale) {
		BigDecimal bd = new BigDecimal(number);
		bd = bd.setScale(scale, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static double roundAvoid(double value, int places) {
		double scale = Math.pow(10, places);
		return Math.round(value * scale) / scale;
	}

	public static int roundUp(double value) {
		double difference = 1 - value;
		return (int) (value + difference);
	}

	public static float clamp(float val, float min, float max) {
		if (val <= min)
			val = min;

		if (val >= max)
			val = max;

		return val;
	}
}

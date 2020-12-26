package me.linus.momentum.util.client.system;

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

	public static float clamp(float val, float min, float max) {
		if (val <= min)
			val = min;

		if (val >= max)
			val = max;

		return val;
	}

	public static float[] calcAngle(Vec3d from, Vec3d to) {
		double difX = to.x - from.x;
		double difY = (to.y - from.y) * -1.0;
		double difZ = to.z - from.z;
		double dist = MathHelper.sqrt(difX * difX + difZ * difZ);
		return new float[] {
				(float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0), (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist)))
		};
	}

	public static double round(double value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(3, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static double degToRad(double deg) {
		return deg * (float) (Math.PI / 180.0f);
	}

	public static float calculateAngle(float serverValue, float currentValue) {
		return ((currentValue - serverValue)) / 4;
	}
}

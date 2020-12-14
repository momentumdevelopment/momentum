package me.linus.momentum.util.client.color;

import java.awt.Color;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.Module;
import me.linus.momentum.module.modules.client.Colors;
import me.linus.momentum.util.client.friend.FriendManager;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

/**
 * @author bon & linustouchtips
 * @since 11/12/20
 */

public abstract class ColorUtil implements MixinInterface {
	
	public static int rainbow(long offset) {
		float hue = (float) ((((System.currentTimeMillis()*(Colors.speed.getValue() / 10)) + (offset * 500)) % (30000L / (Colors.difference.getValue()/100))) / (30000.0f / (Colors.difference.getValue()/20)));
		int rgb = Color.HSBtoRGB(hue, (float) Colors.saturation.getValue(), (float) Colors.brightness.getValue());
		int red = rgb >> 16 & 255;
        int green = rgb >> 8 & 255;
        int blue = rgb & 255;
        int color = toRGBA(red, green, blue, (int) Colors.a.getValue());
		return color;
	}
	
	public static int toRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + (b << 0) + (a << 24);
    }

	public static void hexColor(int hexColor) {
		float red = (hexColor >> 16 & 0xFF) / 255.0F;
		float green = (hexColor >> 8 & 0xFF) / 255.0F;
		float blue = (hexColor & 0xFF) / 255.0F;
		float alpha = (hexColor >> 24 & 0xFF) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
	}

	public static int getColorByCategory(Module mod) {
		if (mod.getCategory().equals(Module.Category.PLAYER)) return new Color(37, 205, 84).getRGB();
		if (mod.getCategory().equals(Module.Category.RENDER)) return new Color(231, 164, 73).getRGB();
		if (mod.getCategory().equals(Module.Category.MOVEMENT)) return new Color(217, 49, 103).getRGB();
		if (mod.getCategory().equals(Module.Category.COMBAT)) return new Color(56, 103, 224).getRGB();
		if (mod.getCategory().equals(Module.Category.CLIENT)) return new Color(234, 71, 71).getRGB();
		if (mod.getCategory().equals(Module.Category.MISC)) return new Color(122, 61, 217).getRGB();
		else return 0;
	}

	public static Color getEntityColor(Entity e) {
		if (EntityUtil.isPassive(e))
			return new Color(0, 200, 0);
		if (EntityUtil.isHostileMob(e))
			return new Color(131, 19, 199);
		if (FriendManager.isFriend(e.getName()))
			return new Color(85, 231, 215);
		else
			return new Color(215, 46, 46);
	}

	public static Color getHealthColor(float health) {
		if (health <= 4)
			return new Color(200, 0, 0);
		if (health <= 8)
			return new Color(231, 143, 85);
		if (health <= 12)
			return new Color(219, 201, 106);
		if (health <= 16)
			return new Color(117, 231, 85);
		else
			return new Color(44, 186, 19);
	}
}

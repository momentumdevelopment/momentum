package me.linus.momentum.util.client;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.Module;
import me.linus.momentum.module.modules.client.Colors;
import me.linus.momentum.module.modules.render.ESP;
import me.linus.momentum.module.modules.render.StorageESP;
import me.linus.momentum.module.modules.render.Tracers;
import me.linus.momentum.managers.social.friend.FriendManager;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.*;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;

/**
 * @author bon & linustouchtips
 * @since 11/12/20
 */

public abstract class ColorUtil implements MixinInterface {

	public static int rainbow(long offset) {
		float hue = (float) ((((System.currentTimeMillis() * (Colors.speed.getValue() / 10)) + (offset * 500)) % (30000L / (Colors.difference.getValue() / 100))) / (30000.0f / (Colors.difference.getValue() / 20)));
		int rgb = Color.HSBtoRGB(hue, (float) Colors.saturation.getValue(), (float) Colors.brightness.getValue());
		int red = rgb >> 16 & 255;
		int green = rgb >> 8 & 255;
		int blue = rgb & 255;
		int color = toRGBA(red, green, blue, Colors.clientPicker.getColor().getAlpha());
		return color;
	}

	public static Color staticRainbow() {
		float hue = (float) (System.currentTimeMillis() % 11520L) / 12000.0f;
		int rgb = Color.HSBtoRGB(hue, 1.0f, 1.0f);
		int red = rgb >> 16 & 255;
		int green = rgb >> 8 & 255;
		int blue = rgb & 255;
		return new Color(red, green, blue);
	}

	public static Color alphaStep(Color color, int index, int count) {
		float[] hsb = new float[3];
		Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
		float brightness = Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) index / (float) count * 2.0F) % 2.0F - 1.0F);
		brightness = 0.5F + 0.5F * brightness;
		hsb[2] = brightness % 2.0F;
		return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
	}

	public static int toRGBA(int r, int g, int b, int a) {
		return (r << 16) + (g << 8) + (b << 0) + (a << 24);
	}

	public static int getColorByCategory(Module mod) {
		switch (mod.getCategory()) {
			case PLAYER:
				return new Color(37, 205, 84).getRGB();
			case RENDER:
				return new Color(231, 164, 73).getRGB();
			case MOVEMENT:
				return new Color(217, 49, 103).getRGB();
			case COMBAT:
				return new Color(56, 103, 224).getRGB();
			case CLIENT:
				return new Color(234, 71, 71).getRGB();
			case MISC:
				return new Color(122, 61, 217).getRGB();
		}

		return -1;
	}

	public static Color getEntityColor(Entity e) {
		if (EntityUtil.isPassive(e))
			return ESP.animalPicker.getColor();
		if (EntityUtil.isHostileMob(e))
			return ESP.mobsPicker.getColor();
		if (FriendManager.isFriend(e.getName()))
			return new Color(85, 231, 215);
		if (EntityUtil.isVehicle(e))
			return ESP.vehiclePicker.getColor();
		if (e instanceof EntityEnderCrystal)
			return ESP.crystalPicker.getColor();
		else
			return ESP.playerPicker.getColor();
	}

	public static Color getTracerColor(Entity e) {
		if (EntityUtil.isPassive(e))
			return Tracers.animalPicker.getColor();
		if (EntityUtil.isHostileMob(e))
			return Tracers.mobsPicker.getColor();
		if (FriendManager.isFriend(e.getName()))
			return new Color(85, 231, 215);
		if (e instanceof EntityItem)
			return Tracers.itemsPicker.getColor();
		if (e instanceof EntityEnderCrystal)
			return Tracers.crystalPicker.getColor();
		else
			return Tracers.playerPicker.getColor();
	}

	public static Color getHealthColor(float health) {
		if (health <= 4)
			return new Color(200, 0, 0);
		else if (health <= 8)
			return new Color(231, 143, 85);
		else if (health <= 12)
			return new Color(219, 201, 106);
		else if (health <= 16)
			return new Color(117, 231, 85);
		else
			return new Color(44, 186, 19);
	}

	public static Color getStorageColor(TileEntity tileEntity) {
		if (tileEntity instanceof TileEntityChest)
			return StorageESP.chestPicker.getColor();
		else if (tileEntity instanceof TileEntityEnderChest)
			return StorageESP.enderPicker.getColor();
		else if (tileEntity instanceof TileEntityHopper)
			return StorageESP.hopperPicker.getColor();
		else if (tileEntity instanceof TileEntityDropper)
			return StorageESP.dropperPicker.getColor();
		else if (tileEntity instanceof TileEntityFurnace)
			return StorageESP.furnacePicker.getColor();
		else if (tileEntity instanceof TileEntityBed)
			return StorageESP.bedPicker.getColor();
		else if (tileEntity instanceof TileEntityShulkerBox)
			return StorageESP.shulkerPicker.getColor();
		else
			return new Color(255, 255, 255, 255);
	}

	public static TextFormatting getHealthText(float health) {
		if (health <= 4)
			return TextFormatting.RED;
		else if (health <= 8)
			return TextFormatting.GOLD;
		else if (health <= 12)
			return TextFormatting.YELLOW;
		else if (health <= 16)
			return TextFormatting.DARK_GREEN;
		else
			return TextFormatting.GREEN;
	}

	public static TextFormatting getPingText(float ping) {
		if (ping <= 20)
			return TextFormatting.DARK_GREEN;
		else if (ping <= 50)
			return TextFormatting.GREEN;
		else if (ping <= 90)
			return TextFormatting.YELLOW;
		else if (ping <= 130)
			return TextFormatting.GOLD;
		else
			return TextFormatting.RED;
	}
}
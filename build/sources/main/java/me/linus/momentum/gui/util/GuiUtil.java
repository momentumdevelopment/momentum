package me.linus.momentum.gui.util;

/**
 * @author bon
 * @since 11/19/20
 */

public class GuiUtil {
	
	public static int mX;
	public static int mY;
	public static int keydown;
	public static boolean ldown;
	public static boolean lheld;
	public static boolean rdown;
	
	public static boolean mouseOver(int minX, int minY, int maxX, int maxY) {
		return mX >= minX && mY >= minY && mX <= maxX && mY <= maxY;
	}
	
	public static void mouseListen(int mouseX, int mouseY) {
		mX = mouseX;
		mY = mouseY;
		ldown = false;
		rdown = false;
		keydown = -1;
	}
	
	public static void lclickListen() {
		ldown = true;
		lheld = true;
	}
	
	public static void rclickListen() {
		rdown = true;
	}
	
	public static void releaseListen() {
		lheld = false;
	}
	
	public static void keyListen(int key) {
		keydown = key;
	}
	
}
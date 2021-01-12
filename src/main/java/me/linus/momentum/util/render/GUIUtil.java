package me.linus.momentum.util.render;

/**
 * @author bon
 * @since 11/19/20
 */

public class GUIUtil {
	
	public static int mouseX;
	public static int mouseY;
	public static int keydown;
	public static boolean ldown;
	public static boolean lheld;
	public static boolean rdown;
	
	public static boolean mouseOver(int minX, int minY, int maxX, int maxY) {
		return mouseX >= minX && mouseY >= minY && mouseX <= maxX && mouseY <= maxY;
	}
	
	public static void mouseListen(int mX, int mY) {
		mouseX = mX;
		mouseY = mY;
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
package me.linus.momentum.gui.window;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.main.GUIScreen;
import me.linus.momentum.gui.main.HUDScreen;
import me.linus.momentum.gui.main.WindowScreen;
import me.linus.momentum.gui.navigation.BarComponent;
import me.linus.momentum.gui.window.windows.ConsoleWindow;
import me.linus.momentum.gui.window.windows.SocialWindow;
import me.linus.momentum.managers.AnimationManager;
import me.linus.momentum.managers.ScreenManager;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.util.client.color.ThemeColor;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.render.GUIUtil;
import me.linus.momentum.util.render.Render2DUtil;
import me.linus.momentum.util.render.builder.Render2DBuilder;
import me.linus.momentum.util.render.builder.Render2DBuilder.Render2DMode;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Window implements MixinInterface {

    public int x;
    public int y;
    public int width;
    public int height;

    public boolean ldown;
    public boolean rdown;
    public boolean dragging;
    public boolean expanding;
    public boolean isTyping;

    public static double barOffset;
    public static double windowOffset;

    public int lastmX;
    public int lastmY;

    public String name;
    public ResourceLocation icon;
    public AnimationManager animationManager = new AnimationManager(200, false);

    public static List<Window> windows = new ArrayList<>();
    public static List<BarComponent> barComponents = new ArrayList<>();

    public Window(String name, int x, int y, int width, int height, ResourceLocation icon) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.icon = icon;
    }

    public static void createWindows() {
        windows.add(new ConsoleWindow());
        windows.add(new SocialWindow());

        barComponents.add(new BarComponent(new ResourceLocation("momentum:modules.png"), new GUIScreen(), ScreenManager.Screen.Click));
        barComponents.add(new BarComponent(new ResourceLocation("momentum:hud-editor.png"), new HUDScreen(), ScreenManager.Screen.Hud));
        barComponents.add(new BarComponent(new ResourceLocation("momentum:console.png"), new WindowScreen(), ScreenManager.Screen.Console));
    }

    public void drawWindowTitle() {
        Render2DUtil.drawRect(x - 2, y, (x + this.width + 2), y + 14, 0, ThemeColor.COLOR, -1, false, Render2DMode.Normal);
        FontUtil.drawString(name, x + 1, y + 3, -1);

        int cancelColor = new Color(255, 255, 255, 190).getRGB();
        if (GUIUtil.mouseOver(x + this.width - 8, y + 2, x + this.width + 2, y + 14 - 2)) {
            cancelColor = new Color(255, 255, 255, 255).getRGB();
        }

        Render2DUtil.drawLine(x + this.width + 1, y + 14 - 2, x + this.width - 9, y + 2, 3, cancelColor);
        Render2DUtil.drawLine(x + this.width + 1, y + 2, x + this.width - 9, y + 14 - 2, 3, cancelColor);
    }

    public static void drawBar() {
        Render2DUtil.drawRect(0, 0, new ScaledResolution(mc).getScaledWidth(), 30, 1, new Color(18, 18, 18, 90).getRGB(), -1, false, Render2DMode.Normal);

        Render2DBuilder.prepareScale(2.25f, 2.25f);
        FontUtil.drawString(Momentum.NAME + " " + Momentum.VERSION, 3, 2, -1);
        Render2DBuilder.restoreScale();
    }

    public void drawWindowBase() {

    }

    public void drawWindow() {

    }

    public static void drawSideBar() {
        if (ClickGUI.navigation.getValue()) {
            drawBar();

            barOffset = 1;
            for (BarComponent barComponent : barComponents) {
                barComponent.drawBarImage((int) (new ScaledResolution(mc).getScaledWidth() - (barOffset * 28)), 1);
                barOffset++;
            }
        }

        windowOffset = 0;
        Render2DUtil.drawRect((int) (windowOffset), new ScaledResolution(mc).getScaledHeight() - 30, new ScaledResolution(mc).getScaledWidth(), new ScaledResolution(mc).getScaledHeight(), 0, new Color(18, 18, 18, 90).getRGB(), new Color(0, 0, 0, 90).getRGB(), false, Render2DMode.Both);
        for (Window window : windows) {
            int windowColor = new Color(20, 20, 20, 90).getRGB();
            if (GUIUtil.mouseOver((int) (windowOffset), new ScaledResolution(mc).getScaledHeight() - 30, (int) (3 + windowOffset + FontUtil.getStringWidth(window.name)), new ScaledResolution(mc).getScaledHeight())) {
                windowColor = new Color(25, 25, 25, 90).getRGB();

                if (GUIUtil.ldown)
                    window.animationManager.setState(window.animationManager.getAnimationFactor() < 0.95);
            }

            Render2DUtil.drawRect((int) (3 + windowOffset), new ScaledResolution(mc).getScaledHeight() - 30, 3 + windowOffset + FontUtil.getStringWidth(window.name), new ScaledResolution(mc).getScaledHeight(), 1, window.animationManager.getAnimationFactor() > 0.05 ? ThemeColor.COLOR : windowColor, -1, false, Render2DMode.Normal);
            FontUtil.drawString(window.name, (float) (5 + windowOffset), new ScaledResolution(mc).getScaledHeight() - 18, -1);

            windowOffset += FontUtil.getStringWidth(window.name);
        }
    }

    public void mouseListen() {
        if (dragging) {
            this.x = GUIUtil.mX - (lastmX - this.x);
            this.y = GUIUtil.mY - (lastmY - this.y);
        }

        if (expanding) {
            this.width = GUIUtil.mX - this.x;
            this.height = GUIUtil.mY - this.y;
        }

        lastmX = GUIUtil.mX;
        lastmY = GUIUtil.mY;
    }

    public void reset() {
        ldown = false;
        rdown = false;
    }

    public void lclickListen(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (GUIUtil.mouseOver(this.x, this.y, this.x + this.width, this.y + 14))
            this.dragging = true;

        if (GUIUtil.mouseOver(this.x + this.width - 20, this.y + this.height, this.x + this.width, this.y + this.height + 30))
            this.expanding = true;

        if (GUIUtil.mouseOver(this.x, this.y + 14 + this.height, this.x + this.width - 30, this.y + this.height + 30))
            this.isTyping = !this.isTyping;
    }

    public void rclickListen(int mouseX, int mouseY, int mouseButton) {
        if (GUIUtil.mouseOver(this.x, this.y, this.x + this.width, this.y + 14))
            animationManager.setState(false);
    }

    public void mouseWheelListen() {

    }

    public void releaseListen(int mouseX, int mouseY, int state) {
        ldown = false;
        dragging = false;
        expanding = false;
    }

    public void keyListen(char typedChar, int keyCode) {

    }

    public int getX() {
        return this.x;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int newY) {
        this.y = newY;
    }
}
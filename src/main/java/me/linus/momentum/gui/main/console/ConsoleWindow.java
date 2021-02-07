package me.linus.momentum.gui.main.console;

import me.linus.momentum.gui.main.gui.Window;
import me.linus.momentum.gui.theme.Theme;
import me.linus.momentum.gui.theme.themes.DefaultTheme;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.util.render.gui.GUIUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConsoleWindow implements MixinInterface {

    public int x;
    public int y;

    public boolean ldown;
    public boolean rdown;
    public boolean dragging;
    public boolean expanding;
    public static boolean isTyping;

    public boolean opened = true;

    public int currentTheme;

    public int lastmX;
    public int lastmY;
    public String name;
    public Screen screen;
    public static List<ConsoleWindow> windows = new ArrayList<>();

    public ConsoleWindow(String name, int x, int y, Screen screen) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.screen = screen;
    }

    public static void initConsole() {
        windows.add(new ConsoleWindow("Console", 2, 2, Screen.Command));
    }

    public void drawConsole(int mouseX, int mouseY, float partialTicks) {
        mouseListen();

        currentTheme = ClickGUI.theme.getValue();
        Theme current = Theme.getTheme(currentTheme);
        current.drawConsoleTitle(name, x , y);

        if (opened)
            current.drawConsole(x, y);

        reset();

        if (mc != null && !ClickGUI.allowOverflow.getValue())
            resetOverflow();
    }

    public void resetOverflow() {
        int screenWidth = new ScaledResolution(mc).getScaledWidth();
        int screenHeight = new ScaledResolution(mc).getScaledHeight();

        if (this.x > screenWidth)
            this.x = screenWidth;

        if (this.y > screenHeight)
            this.y = screenHeight;

        if (this.x < 0)
            this.x = 0;

        if (y < 0)
            this.y = 0;
    }

    void mouseListen() {
        if (dragging) {
            x = GUIUtil.mX - (lastmX - x);
            y = GUIUtil.mY - (lastmY - y);
        }

        if (expanding) {
            mc.player.sendChatMessage("troll");
            DefaultTheme.consoleWidth = GUIUtil.mX - (lastmX - x);
            DefaultTheme.consoleHeight = GUIUtil.mY - (lastmY - y);
        }

        lastmX = GUIUtil.mX;
        lastmY = GUIUtil.mY;
    }

    void reset() {
        ldown = false;
        rdown = false;
    }

    public void lclickListen(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (GUIUtil.mouseOver(x + DefaultTheme.consoleWidth - 3, y + DefaultTheme.consoleHeight + 213, x + DefaultTheme.consoleWidth, y + DefaultTheme.consoleHeight + 216))
            expanding = true;

        if (GUIUtil.mouseOver(x, y, x + DefaultTheme.consoleWidth, y + DefaultTheme.consoleHeight))
            dragging = true;

        if (GUIUtil.mouseOver(x, y + DefaultTheme.consoleHeight + 200, x + DefaultTheme.consoleWidth, y + DefaultTheme.consoleHeight + 216))
            isTyping = !isTyping;
    }

    public void rclickListen(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (GUIUtil.mouseOver(x, y, x + DefaultTheme.consoleWidth, y + DefaultTheme.consoleHeight))
            opened = !opened;
    }

    public void mouseWheelListen() {
        int scrollWheel = Mouse.getDWheel();

        for (Window windows : Window.windows) {
            if (scrollWheel < 0)
                windows.setY((int) (windows.getY() - ClickGUI.scrollSpeed.getValue()));

            else if (scrollWheel > 0)
                windows.setY((int) (windows.getY() + ClickGUI.scrollSpeed.getValue()));
        }
    }

    public void releaseListen(int mouseX, int mouseY, int state) {
        ldown = false;
        dragging = false;
        expanding = false;
    }

    public void keyListen(char typedChar, int keyCode) {
        if (isTyping) {
            if (keyCode == Keyboard.KEY_RETURN)
                DefaultTheme.resetText();

            else {
                String tempText = "";

                if (ChatAllowedCharacters.isAllowedCharacter(typedChar))
                    tempText = typedChar + "";

                else {
                    if (keyCode == Keyboard.KEY_BACK) {
                        if (DefaultTheme.typedCharacters.length() >= 1)
                            DefaultTheme.typedCharacters = DefaultTheme.typedCharacters.substring(0, DefaultTheme.typedCharacters.length() -1);
                    }
                }

                DefaultTheme.typedCharacters += tempText;
            }
        }
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

    public enum Screen {
        Command,
        AltManager,
        Info
    }
}

package me.linus.momentum.gui.main.console;

import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.util.render.gui.GUIUtil;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

/**
 * @author olliem5 & linustouchtips
 * @since 11/16/20
 */

public class Console extends GuiScreen {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        for (ConsoleWindow consoleWindow : ConsoleWindow.windows) {
            consoleWindow.drawConsole(mouseX, mouseY, partialTicks);
        }

        GUIUtil.mouseListen(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {

            for (ConsoleWindow consoleWindow : ConsoleWindow.windows) {
                consoleWindow.lclickListen(mouseX, mouseY, mouseButton);
            }

            GUIUtil.lclickListen();
        }

        if (mouseButton == 1) {

            for (ConsoleWindow consoleWindow : ConsoleWindow.windows) {
                consoleWindow.rclickListen(mouseX, mouseY, mouseButton);
            }

            GUIUtil.rclickListen();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0) {

            for (ConsoleWindow consoleWindow : ConsoleWindow.windows) {
                consoleWindow.releaseListen(mouseX, mouseY, state);
            }

            GUIUtil.releaseListen();
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        for (ConsoleWindow consoleWindow : ConsoleWindow.windows) {
            consoleWindow.keyListen(typedChar, keyCode);
        }

        GUIUtil.keyListen(keyCode);
    }

    @Override
    public void onGuiClosed() {
        try {
            super.onGuiClosed();
            ModuleManager.getModuleByName("Console").disable();

            mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return ClickGUI.pauseGame.getValue();
    }
}

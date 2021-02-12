package me.linus.momentum.gui.main.window;

import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.managers.WindowManager;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.util.render.GUIUtil;
import me.linus.momentum.util.render.Render2DUtil;
import me.linus.momentum.util.render.builder.Render2DBuilder;
import net.minecraft.client.gui.GuiScreen;
import me.linus.momentum.gui.window.Window;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.io.IOException;

public class WindowScreen extends GuiScreen {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        int windowOffset = 0;
        for (Window window : WindowManager.getWindows()) {
            GlStateManager.pushMatrix();
            GlStateManager.enableTexture2D();

            Render2DUtil.drawRect(0, 0, 30, new ScaledResolution(mc).getScaledHeight(), 1, new Color(18, 18, 18, 90).getRGB(), new Color(0, 0, 0, 90).getRGB(), false, Render2DBuilder.Render2DMode.Both);
            window.drawSideBar(windowOffset);

            GlStateManager.scale(window.animationManager.getAnimationFactor(), window.animationManager.getAnimationFactor(), 1);

            if (window.visible) {
                window.mouseListen();
                window.mouseWheelListen();
                window.drawWindowTitle();
                window.drawWindowBase();
                window.drawWindow();
                window.reset();
            }

            window.animationManager.updateTime();

            GlStateManager.popMatrix();

            windowOffset++;
        }

        GUIUtil.mouseListen(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            for (Window window : WindowManager.getWindows()) {
                window.lclickListen(mouseX, mouseY, mouseButton);
            }

            GUIUtil.lclickListen();
        }

        if (mouseButton == 1) {
            for (Window window : WindowManager.getWindows()) {
                window.rclickListen(mouseX, mouseY, mouseButton);
            }

            GUIUtil.rclickListen();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0) {
            for (Window window : WindowManager.getWindows()) {
                window.releaseListen(mouseX, mouseY, state);
            }

            GUIUtil.releaseListen();
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        for (Window window : WindowManager.getWindows()) {
            window.keyListen(typedChar, keyCode);
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

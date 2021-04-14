package me.linus.momentum.gui.main;

import me.linus.momentum.gui.window.Window;
import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.render.GUIUtil;
import me.linus.momentum.util.render.Render2DUtil;
import me.linus.momentum.util.render.builder.Render2DBuilder;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.io.IOException;

public class WindowScreen extends GuiScreen {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        Window.drawSideBar();

        for (Window window : Window.windows) {
            GlStateManager.pushMatrix();
            GlStateManager.enableTexture2D();

            GlStateManager.scale(MathUtil.clamp((float) window.animationManager.getAnimationFactor(), 0, 1), MathUtil.clamp((float) window.animationManager.getAnimationFactor(), 0, 1), 1);

            if (window.animationManager.getAnimationFactor() > 0.05) {
                window.mouseListen();
                window.mouseWheelListen();
                window.drawWindowTitle();
                window.drawWindowBase();
                window.drawWindow();
                window.reset();
            }

            GlStateManager.popMatrix();
        }

        GUIUtil.mouseListen(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            for (Window window : Window.windows) {
                window.lclickListen(mouseX, mouseY, mouseButton);
            }

            GUIUtil.lclickListen();
        }

        if (mouseButton == 1) {
            for (Window window : Window.windows) {
                window.rclickListen(mouseX, mouseY, mouseButton);
            }

            GUIUtil.rclickListen();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0) {
            for (Window window : Window.windows) {
                window.releaseListen(mouseX, mouseY, state);
            }

            GUIUtil.releaseListen();
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        for (Window window : Window.windows) {
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
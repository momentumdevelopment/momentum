package me.linus.momentum.gui.main;

import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.gui.click.Frame;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.render.GUIUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;

/**
 * @author linustouchtips & bon
 * @since 03/11/21
 */

public class GUIScreen extends GuiScreen {

    public static String description = "";

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        
        for (Frame frame : Frame.frames) {
            frame.mouseWheelListen();
            frame.renderFrame();
        }

        if (ClickGUI.descriptions.getValue())
            FontUtil.drawString(description, (new ScaledResolution(mc).getScaledWidth() / 2) - (FontUtil.getStringWidth(description) / 2) + 20, 10, -1);

        GUIUtil.mouseListen(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            for (Frame frame : Frame.frames)
                frame.lclickListen();

            GUIUtil.lclickListen();
        }

        if (mouseButton == 1) {
            for (Frame frame : Frame.frames)
                frame.rclickListen();

            GUIUtil.rclickListen();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0) {
            for (Frame frame : Frame.frames)
                frame.releaseListen();

            GUIUtil.releaseListen();
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        for (Frame frame : Frame.frames)
            frame.keyListen(keyCode);

        GUIUtil.keyListen(keyCode);
    }

    @Override
    public void onGuiClosed() {
        try {
            super.onGuiClosed();
            ModuleManager.getModuleByName("ClickGUI").disable();

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

package me.linus.momentum.gui.window;

import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.managers.AnimationManager;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.render.GUIUtil;
import me.linus.momentum.util.render.Render2DUtil;
import me.linus.momentum.util.render.builder.Render2DBuilder.Render2DMode;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

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

    public boolean visible = false;
    public boolean opened;

    public int lastmX;
    public int lastmY;

    public String name;
    public ResourceLocation icon;
    public AnimationManager animationManager = new AnimationManager(200);
    
    public Window(String name, int x, int y, int width, int height, ResourceLocation icon) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.icon = icon;
    }
    
    public void drawWindowTitle() {
        Render2DUtil.drawRect(x - 2, y, (x + this.width + 2), y + 14, 0, ThemeColor.COLOR, -1, false, Render2DMode.Normal);
        FontUtil.drawString(name, x + 1, y + 3, -1);

        int cancelColor = new Color(255, 255, 255, 190).getRGB();
        if (GUIUtil.mouseOver(x + this.width - 8, y + 2, x + this.width + 2, y + 14 - 2)) {
            cancelColor = new Color(255, 255, 255, 255).getRGB();

            if (GUIUtil.ldown)
                visible = !visible;
        }

        Render2DUtil.drawLine(x + this.width + 1, y + 14 - 2, x + this.width - 9, y + 2, 3, cancelColor);
        Render2DUtil.drawLine(x + this.width + 1, y + 2, x + this.width - 9, y + 14 - 2, 3, cancelColor);
    }
    
    public void drawWindowBase() {

    }
    
    public void drawWindow() {
        
    }

    public void drawSideBar(int windowsOffset) {
        int windowColor = 0xCC232323;
        if (GUIUtil.mouseOver(1, 1 + (windowsOffset * 35), 29, 30 + (windowsOffset * 35))) {
            windowColor = 0xCC383838;

            if (GUIUtil.ldown) {
                visible = !visible;
                animationManager.updateState();
            }
        }

        Render2DUtil.drawRect(1, 1 + (windowsOffset * 35), 29, 30 + (windowsOffset * 35), 1, this.visible ? ThemeColor.COLOR : windowColor, -1, false, Render2DMode.Normal);

        GlStateManager.enableAlpha();
        mc.getTextureManager().bindTexture(this.icon);
        GlStateManager.color(1, 1, 1, 0.47f);
        GL11.glPushMatrix();
        GuiScreen.drawScaledCustomSizeModalRect(1, 1 + (windowsOffset * 35), 0, 0, 256,256,26,26,256,256);
        GL11.glPopMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);

        windowsOffset++;
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

    public void rclickListen(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (GUIUtil.mouseOver(this.x, this.y, this.x + this.width, this.y + 14))
            this.opened = !this.opened;
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

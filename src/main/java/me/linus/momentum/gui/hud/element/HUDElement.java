package me.linus.momentum.gui.hud.element;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.modules.client.HUD;
import me.linus.momentum.setting.Setting;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.render.GUIUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.common.MinecraftForge;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * @author linustouchtips & bon
 * @since 03/11/21
 */

public class HUDElement implements MixinInterface {

    public int x;
    public int y;
    public int width;
    public int height;

    public boolean ldown;
    public boolean rdown;
    public boolean dragging;

    public boolean drawn;
    public boolean opened;
    public boolean background;
    public int color;

    public int lastmX;
    public int lastmY;

    public String name;
    public Category category;
    public AnchorPoint anchorPoint;

    public List<Setting> settingList = new ArrayList<>();

    public HUDElement(String name, int x, int y, Category category, AnchorPoint anchorPoint) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.category = category;
        this.anchorPoint = anchorPoint;
        this.width = (int) FontUtil.getStringWidth(name);
        this.height = (int) FontUtil.getFontHeight() + 1;
        this.drawn = false;
        this.background = true;

        setup();
    }

    public void renderElementOverlay() {
        mouseListen();

        GlStateManager.enableTexture2D();

        color = (GUIUtil.mouseOver(x - 1, y - 1, x + width, y + height)) ? new Color(82, 81, 77, 125).getRGB() : new Color(117, 116, 110, 125).getRGB();

        if (GUIUtil.mouseOver(x - 1, y - 1, x + width, y + height) && GUIUtil.rdown)
            drawn = false;

        if (drawn) {
            if (background)
                GuiScreen.drawRect(x - 1, y - 1, x + width, y + height, color);

            renderElement();
        }

        resetMouse();

        if (!HUD.allowOverflow.getValue())
            resetOverflow();
    }

    public void renderElement() {

    }

    public void setup() {

    }

    public void resetOverflow() {
        int screenWidth = new ScaledResolution(mc).getScaledWidth();
        int screenHeight = new ScaledResolution(mc).getScaledHeight();

        if (this.width < 0) {
            if (this.x > screenWidth)
                this.x = screenWidth;

            if (this.x + this.width < 0)
                this.x = -this.width;
        }

        else {
            if (this.x < 0)
                this.x = 0;

            if (this.x + this.width > screenWidth)
                this.x = screenWidth - this.width;
        }

        if (this.y < 0)
            this.y = 0;

        if (this.y + this.height > screenHeight)
            this.y = screenHeight - this.height;
    }

    public void addSetting(Setting setting) {
        settingList.add(setting);
    }

    public void mouseListen() {
        if (dragging) {
            x = lastmX + GUIUtil.mX;
            y = lastmY + GUIUtil.mY;
        }
    }

    public void resetMouse() {
        ldown = false;
        rdown = false;
    }

    public void lclickListen(int button) {
        if (width < 0) {
            if (button == 0 && GUIUtil.mX < x && GUIUtil.mX > x + width && GUIUtil.mY > y && GUIUtil.mY < y + height) {
                lastmX = x - GUIUtil.mX;
                lastmY = y - GUIUtil.mY;
                dragging = true;
            }

            if (button == 2)
                this.drawn = false;
        }

        else {
            if (button == 0 && GUIUtil.mX > x && GUIUtil.mX < x + width && GUIUtil.mY > y && GUIUtil.mY < y + height) {
                lastmX = x - GUIUtil.mX;
                lastmY = y - GUIUtil.mY;
                dragging = true;
            }
        }
    }

    public void rclickListen() {
        if (GUIUtil.mouseOver(x, y, x + width, y + height))
            toggleElement();
    }

    public void releaseListen() {
        ldown = false;
        dragging = false;
    }

    public void toggleElement() {
        drawn = !drawn;
    }

    public void toggleState() {
        if (this.opened)
            MinecraftForge.EVENT_BUS.unregister(this);
        else
            MinecraftForge.EVENT_BUS.register(this);

        this.opened = !this.opened;
    }

    public boolean isOpened() {
        return this.opened;
    }

    public void removeBackground() {
        background = false;
    }

    public String getName() {
        return this.name;
    }

    public boolean isDrawn() {
        return this.drawn;
    }

    public boolean hasSettings() {
        return settingList.size() > 0;
    }

    public List<Setting> getSettings(){
        return settingList;
    }

    public enum Category {
        COMBAT("Combat"),
        INFO("Information"),
        MISC("Miscellaneous");

        String name;

        Category(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

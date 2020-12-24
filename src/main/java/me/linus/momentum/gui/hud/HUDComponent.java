package me.linus.momentum.gui.hud;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.setting.Setting;
import me.linus.momentum.util.render.GUIUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class HUDComponent implements MixinInterface {

    private final String name;

    protected int x;
    protected int y;
    private int dragX = 0;
    private int dragY = 0;
    protected int width = 10;
    protected int height = mc.fontRenderer.FONT_HEIGHT + 3;
    public int colors;

    private boolean opened;
    private boolean dragging = false;
    private boolean enabled;

    public List<Setting> settingsList = new ArrayList<>();

    public HUDComponent(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.enabled = false;
        this.y = y;
        this.opened = false;

        this.setup();
    }

    public void renderInGUI(int mouseX, int mouseY) {
        if (dragging) {
            x = dragX + mouseX;
            y = dragY + mouseY;
        }

        if (GUIUtil.mouseOver(x - 1, y - 1, x + width, y + height))
            colors = new Color(82, 81, 77, 125).getRGB();
        else
            colors = new Color(117, 116, 110, 125).getRGB();

        if (getBackground())
            GuiScreen.drawRect(x - 1, y - 1, x + width, y + height, colors);

        renderComponent();
    }

    public void renderComponent() {
        if (mc != null) {
            int screenWidth = new ScaledResolution(mc).getScaledWidth();
            int screenHeight = new ScaledResolution(mc).getScaledHeight();

            if (width < 0) {
                if (x > screenWidth)
                    x = screenWidth;

                if (x + width < 0)
                    x = -width;
            }

            else {
                if (x < 0)
                    x = 0;

                if (x + width > screenWidth)
                    x = screenWidth - width;
            }

            if (y < 0)
                y = 0;

            if (y + height > screenHeight)
                y = screenHeight - height;
        }
    }

    public void setup() {

    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (width < 0) {
            if (button == 0 && mouseX < x && mouseX > x + width && mouseY > y && mouseY < y + height){
                dragX = x - mouseX;
                dragY = y - mouseY;
                dragging = true;
            }
        } else {
            if (button == 0 && mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height){
                dragX = x - mouseX;
                dragY = y - mouseY;
                dragging = true;
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) dragging = false;
    }

    public void onGuiClosed(){
        dragging = false;
    }

    public void mouseHovered(int mouseX, int mouseY) {

    }

    public void addSetting(Setting s) {
        settingsList.add(s);
    }

    public void toggleState() {
        this.opened = !this.opened;
    }

    public boolean isOpened() {
        return this.opened;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public boolean hasSettings() {
        return this.settingsList.size() > 0;
    }

    public List<Setting> getSettings(){
        return this.settingsList;
    }

    public boolean getBackground() {
        return true;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void toggle() {
        enabled = !enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean in) {
        enabled = in;
    }
}

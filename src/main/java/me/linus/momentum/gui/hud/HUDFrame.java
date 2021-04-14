package me.linus.momentum.gui.hud;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.component.HUDComponent;
import me.linus.momentum.gui.hud.element.HUDElement;
import me.linus.momentum.gui.main.GUIScreen;
import me.linus.momentum.gui.main.HUDScreen;
import me.linus.momentum.gui.main.WindowScreen;
import me.linus.momentum.managers.AnimationManager;
import me.linus.momentum.managers.HUDElementManager;
import me.linus.momentum.managers.ScreenManager;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.gui.hud.element.HUDElement.Category;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.gui.navigation.BarComponent;
import me.linus.momentum.module.modules.client.HUD;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.client.color.ThemeColor;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.render.GUIUtil;
import me.linus.momentum.util.render.Render2DUtil;
import me.linus.momentum.util.render.builder.Render2DBuilder;
import me.linus.momentum.util.render.builder.Render2DBuilder.Render2DMode;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips & bon
 * @since 03/11/21
 */

public class HUDFrame implements MixinInterface {

    public int x;
    public int y;
    public int width;
    public int height;

    public double offset;
    public double barOffset;

    public boolean ldown;
    public boolean rdown;
    public boolean dragging;

    public int lastmX;
    public int lastmY;
    public String name;
    public Category category;
    public AnimationManager animationManager;

    public List<HUDComponent> components = new ArrayList<>();
    public List<BarComponent> barComponents = new ArrayList<>();
    public static List<HUDFrame> frames = new ArrayList<>();

    public HUDFrame(String name, int x, int y, Category category) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.category = category;
        this.width = 105;
        this.height = 14;

        animationManager = new AnimationManager((int) ClickGUI.animationSpeed.getValue(), true);

        for (HUDElement element : HUDElementManager.getElementsInCategory(category)) {
            components.add(new HUDComponent(this, element));
        }

        barComponents.add(new BarComponent(new ResourceLocation("momentum:modules.png"), new GUIScreen(), ScreenManager.Screen.Click));
        barComponents.add(new BarComponent(new ResourceLocation("momentum:hud-editor.png"), new HUDScreen(), ScreenManager.Screen.Hud));
        barComponents.add(new BarComponent(new ResourceLocation("momentum:console.png"), new WindowScreen(), ScreenManager.Screen.Console));
    }

    public static void createHUDFrames() {
        frames.add(new HUDFrame(Category.COMBAT.getName(), 18, 52, Category.COMBAT));
        frames.add(new HUDFrame(Category.INFO.getName(), 133, 52, Category.INFO));
        frames.add(new HUDFrame(Category.MISC.getName(), 248, 52, Category.MISC));
    }

    public void renderHUDFrame() {
        GlStateManager.enableTexture2D();

        mouseListen();

        if (ClickGUI.navigation.getValue()) {
            drawBar();

            barOffset = 1;
            for (BarComponent barComponent : barComponents) {
                barComponent.drawBarImage((int) (new ScaledResolution(mc).getScaledWidth() - (barOffset * 28)), 1);
                barOffset++;
            }
        }

        drawHUDFrame();

        offset = 0;
        if (animationManager.getAnimationFactor() != 0) {
            for (HUDComponent component : components) {
                component.drawComponent(x, y, height, width);
                offset += MathUtil.clamp((float) animationManager.getAnimationFactor(), 0, 1);
            }
        }

        if (ClickGUI.outline.getValue())
            drawOutline();

        animationManager.tick();

        resetMouse();

        if (!HUD.allowOverflow.getValue())
            resetOverflow();
    }

    public void drawHUDFrame() {
        Render2DUtil.drawRect(x - 2, y, (x + width + 2), y + height, 1, ThemeColor.COLOR, ThemeColor.BRIGHT, false, ClickGUI.outline.getValue() ? Render2DMode.Both : Render2DMode.Normal);
        FontUtil.drawString(name, (x + ((x + width) - x) / 2 - FontUtil.getStringWidth(name) / 2), y + 3, -1);
    }

    public void drawOutline() {
        Render2DUtil.drawRect(x, y - 1 + height, x + width, (int) (y + height + (offset * height)), 1, -1, ThemeColor.BRIGHT, false, Render2DMode.Border);
    }

    public void drawBar() {
        Render2DUtil.drawRect(0, 0, new ScaledResolution(mc).getScaledWidth(), 30, 1, new Color(18, 18, 18, 40).getRGB(), -1, false, Render2DMode.Normal);
        Render2DBuilder.prepareScale(2.25f, 2.25f);
        FontUtil.drawString(Momentum.NAME + " " + Momentum.VERSION, 3, 2, -1);
        Render2DBuilder.restoreScale();
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

    void mouseListen() {
        if (dragging) {
            x = GUIUtil.mX - (lastmX - x);
            y = GUIUtil.mY - (lastmY - y);
        }

        lastmX = GUIUtil.mX;
        lastmY = GUIUtil.mY;
    }

    void resetMouse() {
        ldown = false;
        rdown = false;
    }

    public void lclickListen() {
        if (GUIUtil.mouseOver(x, y, x + width, y + height))
            dragging = true;
    }

    public void rclickListen() {
        if (GUIUtil.mouseOver(x, y, x + width, y + height))
            animationManager.toggle();
    }

    public void mouseWheelListen() {
        int scrollWheel = Mouse.getDWheel();

        for (HUDFrame frames : HUDFrame.frames) {
            if (scrollWheel < 0)
                frames.setY((int) (frames.getY() - ClickGUI.scrollSpeed.getValue()));
            else if (scrollWheel > 0)
                frames.setY((int) (frames.getY() + ClickGUI.scrollSpeed.getValue()));
        }
    }

    public void releaseListen() {
        ldown = false;
        dragging = false;
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

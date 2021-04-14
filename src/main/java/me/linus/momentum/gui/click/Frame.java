package me.linus.momentum.gui.click;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.click.component.RootComponent;
import me.linus.momentum.gui.click.component.SubComponent;
import me.linus.momentum.gui.main.GUIScreen;
import me.linus.momentum.gui.main.HUDScreen;
import me.linus.momentum.gui.main.WindowScreen;
import me.linus.momentum.managers.AnimationManager;
import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.managers.ScreenManager.Screen;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.Module;
import me.linus.momentum.module.Module.Category;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.gui.navigation.BarComponent;
import me.linus.momentum.gui.click.component.Component;
import me.linus.momentum.setting.keybind.Keybind;
import me.linus.momentum.setting.keybind.SubKeybind;
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
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips & bon
 * @since 03/11/21
 */

public class Frame implements MixinInterface {

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

    public List<Component> components = new ArrayList<>();
    public List<BarComponent> barComponents = new ArrayList<>();
    public static List<Frame> frames = new ArrayList<>();

    public Frame(String name, int x, int y, Category category) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.category = category;
        this.width = 105;
        this.height = 14;

        animationManager = new AnimationManager((int) ClickGUI.animationSpeed.getValue(), true);

        for (Module module : ModuleManager.getModulesInCategory(category))
            this.components.add(new Component(this, module));

        barComponents.add(new BarComponent(new ResourceLocation("momentum:modules.png"), new GUIScreen(), Screen.Click));
        barComponents.add(new BarComponent(new ResourceLocation("momentum:hud-editor.png"), new HUDScreen(), Screen.Hud));
        barComponents.add(new BarComponent(new ResourceLocation("momentum:console.png"), new WindowScreen(), Screen.Console));
    }

    public static void createFrames() {
        frames.add(new Frame(Category.COMBAT.getName(), 12, 52, Category.COMBAT));
        frames.add(new Frame(Category.PLAYER.getName(), 127, 52, Category.PLAYER));
        frames.add(new Frame(Category.MISC.getName(), 242, 52, Category.MISC));
        frames.add(new Frame(Category.MOVEMENT.getName(), 357, 52, Category.MOVEMENT));
        frames.add(new Frame(Category.RENDER.getName(), 472, 52, Category.RENDER));
        frames.add(new Frame(Category.CLIENT.getName(), 587, 52, Category.CLIENT));
    }

    public void renderFrame() {
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

        drawFrame();

        offset = 0;
        if (animationManager.getAnimationFactor() != 0) {
            for (Component component : components) {
                component.drawComponent(x, y, height, width);
                offset += MathUtil.clamp((float) animationManager.getAnimationFactor(), 0, 1);
            }
        }

        if (ClickGUI.outline.getValue())
            drawOutline();

        animationManager.tick();

        resetMouse();

        if (!ClickGUI.allowOverflow.getValue())
            resetOverflow();
    }

    public void drawFrame() {
        Render2DUtil.drawRect(x - 2, y, (x + width + 2), y + height, 1, ThemeColor.COLOR, ThemeColor.COLOR, false, Render2DMode.Normal);
        FontUtil.drawString(name, (x + ((x + width) - x) / 2 - FontUtil.getStringWidth(name) / 2), y + 3, -1);
    }

    public void drawOutline() {
        Render2DUtil.drawRect(x, y - 1 + height, x + width, (int) (y + height + (offset * height)), 1, -1, ThemeColor.COLOR, false, Render2DMode.Border);
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

    public void mouseListen() {
        if (dragging) {
            x = GUIUtil.mX - (lastmX - x);
            y = GUIUtil.mY - (lastmY - y);
        }

        lastmX = GUIUtil.mX;
        lastmY = GUIUtil.mY;
    }

    public void resetMouse() {
        ldown = false;
        rdown = false;
    }

    public void lclickListen() {
        if (GUIUtil.mouseOver(x, y, x + width, y + height))
            dragging = true;
    }
    
    public void keyListen(int key) {
        for (Component component : components) {
            if (component.binding && key != -1 && key != Keyboard.KEY_ESCAPE && key != Keyboard.KEY_DELETE) {
                component.module.getKeybind().setKeyModifierAndCode(KeyModifier.NONE, key == Keyboard.KEY_BACK ? Keyboard.KEY_NONE : key);
                component.binding = false;
            }

            if (component.binding && key == Keyboard.KEY_ESCAPE)
                component.binding = false;

            if (!component.binding && Keyboard.isKeyDown(component.module.getKeybind().getKeyCode()) && !(mc.currentScreen instanceof GUIScreen))
                component.animationComponentManager.setState(component.module.isEnabled());

            for (SubComponent subComponent : component.subComponents) {
                if (subComponent.binding && key != -1 && key != Keyboard.KEY_ESCAPE && key != Keyboard.KEY_DELETE && subComponent.setting instanceof Keybind) {
                    ((Keybind) subComponent.setting).setKey(key == Keyboard.KEY_BACK ? Keyboard.KEY_NONE : key);
                    subComponent.binding = false;
                }

                if (subComponent.binding && key == Keyboard.KEY_ESCAPE)
                    subComponent.binding = false;

                for (RootComponent rootComponent : subComponent.rootComponents) {
                    if (rootComponent.binding && key != -1 && key != Keyboard.KEY_ESCAPE && key != Keyboard.KEY_DELETE && rootComponent.subsetting instanceof SubKeybind) {
                        ((SubKeybind) rootComponent.subsetting).setKey(key == Keyboard.KEY_BACK ? Keyboard.KEY_NONE : key);
                        rootComponent.binding = false;
                    }

                    if (rootComponent.binding && key == Keyboard.KEY_ESCAPE)
                        rootComponent.binding = false;
                }
            }
        }
    }

    public void rclickListen() {
        if (GUIUtil.mouseOver(x, y, x + width, y + height)) {
            animationManager.toggle();

            for (Component component : components) {
                if (component.module.isOpened())
                    component.module.toggleState();
            }
        }
    }

    public void mouseWheelListen() {
        int scrollWheel = Mouse.getDWheel();

        for (Frame frames : Frame.frames) {
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
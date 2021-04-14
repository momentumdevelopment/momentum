package me.linus.momentum.gui.click.component;

import me.linus.momentum.gui.main.GUIScreen;
import me.linus.momentum.managers.AnimationManager;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.Module;
import me.linus.momentum.gui.click.Frame;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.setting.Setting;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.client.color.ThemeColor;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.render.GUIUtil;
import me.linus.momentum.util.render.Render2DUtil;
import me.linus.momentum.util.render.builder.Render2DBuilder.Render2DMode;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips & bon
 * @since 03/11/21
 */

public class Component implements MixinInterface {

    public Module module;
    public Frame frame;
    public List<SubComponent> subComponents = new ArrayList<>();
    public AnimationManager animationFrameManager;
    public AnimationManager animationComponentManager;
    
    public boolean binding = false;

    public Component(Frame frame, Module module) {
        this.module = module;
        this.frame = frame;
        this.animationFrameManager = new AnimationManager((int) ClickGUI.animationSpeed.getValue(), false);
        this.animationComponentManager = new AnimationManager(200, module.isEnabled());

        for (Setting setting : module.getSettings())
            subComponents.add(new SubComponent(frame, this, setting));

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void drawComponent(int x, int y, int height, int width) {
        if (Keyboard.isKeyDown(module.getKeybind().getKeyCode()))
            animationComponentManager.setState(module.isEnabled());

        double preOffset = frame.offset;

        if (animationFrameManager.getAnimationFactor() > 0.05) {
            frame.offset += animationFrameManager.getAnimationFactor();

            for (SubComponent subComponent : subComponents) {
                subComponent.drawSubComponent(x, y, height, width);
                frame.offset += animationFrameManager.getAnimationFactor();
            }

            int colorBind = 0xCC232323;
            if (GUIUtil.mouseOver(x + 4, (int) (y + height + (frame.offset * height) + 2), (x + width) - 1, (int) ((y + height) + height + (frame.offset * height)))) {
                colorBind = 0xCC383838;

                if (GUIUtil.ldown) {
                    binding = !binding;

                    if (ClickGUI.sounds.getValue())
                        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, (float) ClickGUI.pitch.getValue()));
                }
            }

            Render2DUtil.drawRect(x, y + height + (frame.offset * height), x + width, y + height * 2 + (frame.offset * height), 0, 0x99202020, -1, false, Render2DMode.Normal);
            Render2DUtil.drawRect(x + 4, y + height + (frame.offset * height), (x + width) - 1, (y + height) + height + (frame.offset * height), 0, colorBind, -1, false, Render2DMode.Normal);

            if (!binding)
                FontUtil.drawString("Bind: " + (module.getKeybind().getDisplayName().equalsIgnoreCase("NONE") ? "None" : module.getKeybind().getDisplayName()), x + 7, (float) ((y + height) + 4 + (frame.offset * height)), -1);
            else
                FontUtil.drawString("Listening ...", x + 7, (float) ((y + height) + 4 + (frame.offset * height)), -1);

            animationFrameManager.tick();
        }

        int color = 0xCC232323;
        if (GUIUtil.mouseOver(x, (int) (y + height + 1 + (preOffset * height)), (x + width) - 1, (int) (y + height * 2 + (preOffset * height)))) {
            color = 0xCC383838;

            GUIScreen.description = module.getDescription();

            if (GUIUtil.ldown) {
                module.toggle();
                animationComponentManager.setState(module.isEnabled());

                if (ClickGUI.sounds.getValue())
                    mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, (float) ClickGUI.pitch.getValue()));
            }

            if (GUIUtil.rdown) {
                module.toggleState();
                animationFrameManager.setState(module.isOpened());

                if (ClickGUI.sounds.getValue())
                    mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, (float) ClickGUI.pitch.getValue()));
            }
        }

        Render2DUtil.drawRect(x, y + height + (preOffset * height), x + width, y + height * 2 + (preOffset * height), 0, color, -1, false, Render2DMode.Normal);
        Render2DUtil.drawRect(x, y + height + (preOffset * height), (x + (width * MathUtil.clamp((float) animationComponentManager.getAnimationFactor(), 0, 1))), y + height * 2 + (preOffset * height), 0, ThemeColor.COLOR, -1, false, Render2DMode.Normal);
        FontUtil.drawString(module.getName(), x + 4, (float) (y + height + 4 + (preOffset * height)), -1);

        animationComponentManager.tick();

        if (module.hasSettings() && ClickGUI.indicators.getValue())
            FontUtil.drawString("...", x + width - 12, (float) (y + 1 + height + (preOffset * height)), -1);
    }
}

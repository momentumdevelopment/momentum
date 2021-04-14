package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.render.builder.RenderBuilder.RenderMode;
import me.linus.momentum.util.world.BlockUtil;
import me.linus.momentum.util.world.hole.HoleUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Objects;

/**
 * @author linustouchtips
 * @since 11/29/2020
 */

public class HoleESP extends Module {
    public HoleESP() {
        super("HoleESP", Category.RENDER, "Highlights safe holes to stand in while crystalling");
    }

    public static Mode main = new Mode("Main",  "Glow", "Fill", "None");
    public static SubSlider mainHeight = new SubSlider(main, "Main Height", -1.0D, 1.0D, 3.0D, 1);

    public static Mode outline = new Mode("Outline", "WireFrame", "Claw", "None");
    public static SubSlider outlineHeight = new SubSlider(outline, "Outline Height", -1.0D, 0.0D, 3.0D, 1);

    public static Mode highlight = new Mode("Highlight", "None", "Hide", "Glow");

    public static Checkbox obsidianColor = new Checkbox("Obsidian Color", true);
    public static ColorPicker obsidianPicker = new ColorPicker(obsidianColor, "Obsidian Picker", new Color(144, 0, 255, 45));

    public static Checkbox bedrockColor = new Checkbox("Bedrock Color", true);
    public static ColorPicker bedrockPicker = new ColorPicker(bedrockColor, "Bedrock Picker", new Color(93, 235, 240, 45));

    public static Checkbox doubles = new Checkbox("Doubles", true);
    public static Checkbox viewFrustrum = new Checkbox("Only in View Frustrum", false);
    public static Slider updates = new Slider("Updates", 0.0D, 10.0D, 20.0D, 0);
    public static Slider lineWidth = new Slider("Width", 0.0D, 0.0D, 3.0D, 2);
    public static Slider range = new Slider("Range", 0.0D, 5.0D, 20.0D, 0);

    @Override
    public void setup() {
        addSetting(main);
        addSetting(outline);
        addSetting(highlight);
        addSetting(obsidianColor);
        addSetting(bedrockColor);
        addSetting(doubles);
        addSetting(viewFrustrum);
        addSetting(updates);
        addSetting(lineWidth);
        addSetting(range);
    }

    int currentTick;
    boolean clearRender = false;

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent renderEvent) {
        if (nullCheck())
            return;

        currentTick++;

        if (currentTick < updates.getValue())
            return;

        if (clearRender)
            return;

        RenderUtil.camera.setPosition(Objects.requireNonNull(mc.getRenderViewEntity()).posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);

        for (BlockPos potentialHole : BlockUtil.getNearbyBlocks(mc.player, range.getValue(), false)) {
            if (HoleUtil.isBedRockHole(potentialHole))
                renderHole(potentialHole, Type.Bedrock, 0, 0);
            else if (HoleUtil.isObsidianHole(potentialHole))
                renderHole(potentialHole, Type.Obsidian, 0, 0);

            if (doubles.getValue()) {
                if (HoleUtil.isDoubleObsidianHoleX(potentialHole.west()))
                    renderHole(potentialHole.west(), Type.Obsidian, 1, 0);
                else if (HoleUtil.isDoubleObsidianHoleZ(potentialHole.north()))
                    renderHole(potentialHole.north(), Type.Obsidian, 0, 1);
                else if (HoleUtil.isDoubleBedrockHoleX(potentialHole.west()))
                    renderHole(potentialHole.west(), Type.Bedrock, 1, 0);
                else if (HoleUtil.isDoubleBedrockHoleZ(potentialHole.north()))
                    renderHole(potentialHole.north(), Type.Bedrock, 0, 1);
            }
        }
    }

    public void renderHole(BlockPos hole, Type type, double length, double width) {
        renderMain(hole, type, length, width);
        renderOutline(hole, type, length, width);
    }

    public void renderMain(BlockPos hole, Type type, double length, double width) {
        switch (main.getValue()) {
            case 0:
                RenderUtil.drawBoxBlockPos(hole, mainHeight.getValue() - 1, length, width, type.equals(Type.Obsidian) ? obsidianPicker.getColor() : bedrockPicker.getColor(), RenderMode.Glow);
                break;
            case 1:
                RenderUtil.drawBoxBlockPos(hole, mainHeight.getValue() - 1, length, width, type.equals(Type.Obsidian) ? obsidianPicker.getColor() : bedrockPicker.getColor(), RenderMode.Fill);
                break;
        }

        if (mc.player.getDistanceSq(hole) < 1.5) {
            switch (highlight.getValue()) {
                case 0:
                case 1:
                    break;
                case 2:
                    RenderUtil.drawBoxBlockPos(hole, mainHeight.getValue() - 1, length, width, type.equals(Type.Obsidian) ? obsidianPicker.getColor() : bedrockPicker.getColor(), RenderMode.Glow);
                    break;
            }
        }
    }

    public void renderOutline(BlockPos hole, Type type, double length, double width) {
        switch (outline.getValue()) {
                case 0:
                    RenderUtil.drawBoxBlockPos(hole, outlineHeight.getValue() - 1, length, width, type.equals(Type.Obsidian) ? obsidianPicker.getColor() : bedrockPicker.getColor(), RenderMode.Outline);
                    break;
                case 1:
                    RenderUtil.drawBoxBlockPos(hole, outlineHeight.getValue() - 1, length, width, type.equals(Type.Obsidian) ? obsidianPicker.getColor() : bedrockPicker.getColor(), RenderMode.Claw);
                    break;
        }
    }

    @Override
    public String getHUDData() {
        return " " + main.getMode(main.getValue()) + ", " + outline.getMode(outline.getValue());
    }

    public enum Type {
        Obsidian,
        Bedrock,
        Double
    }
}
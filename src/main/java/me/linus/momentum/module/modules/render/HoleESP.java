package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.combat.CrystalUtil;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.BlockUtil;
import me.linus.momentum.util.world.HoleUtil;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linustouchtips
 * @since 11/29/2020
 */

public class HoleESP extends Module {
    public HoleESP() {
        super("HoleESP", Category.RENDER, "Highlights safe holes to stand in while crystalling");
    }

    private static final Mode main = new Mode("Main", "Fill", "Glow", "None");
    private static final SubSlider mainHeight = new SubSlider(main, "Height", -1.0D, 0.1D, 2.0D, 1);

    private static final Mode outline = new Mode("Outline", "WireFrame", "None");
    private static final SubSlider outlineHeight = new SubSlider(outline, "Height", -1.0D, 0.1D, 2.0D, 1);

    public static Checkbox obsidianColor = new Checkbox("Obsidian Color", true);
    public static SubSlider obbyRed = new SubSlider(obsidianColor, "Red", 0.0D, 93.0D, 255.0D, 0);
    public static SubSlider obbyGreen = new SubSlider(obsidianColor, "Green", 0.0D, 235.0D, 255.0D, 0);
    public static SubSlider obbyBlue = new SubSlider(obsidianColor, "Blue", 0.0D, 240.0D, 255.0D, 0);
    public static SubSlider obbyAlpha = new SubSlider(obsidianColor, "Alpha", 0.0D, 45.0D, 255.0D, 0);

    public static Checkbox bedrockColor = new Checkbox("Bedrock Color", true);
    public static SubSlider bRockRed = new SubSlider(bedrockColor, "Red", 0.0D, 144.0D, 255.0D, 0);
    public static SubSlider bRockGreen = new SubSlider(bedrockColor, "Green", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider bRockBlue = new SubSlider(bedrockColor, "Blue", 0.0D, 255.0D, 255.0D, 0);
    public static SubSlider bRockAlpha = new SubSlider(bedrockColor, "Alpha", 0.0D, 45.0D, 255.0D, 0);

    public static Slider lineWidth = new Slider("Line Width", 0.0D, 1.5D, 3.0D, 2);
    public static Slider lineAlpha = new Slider("Line Alpha", 0.0D, 144.0D, 255.0D, 0);
    public static Slider range = new Slider("Range", 0.0D, 7.0D, 20.0D, 0);

    @Override
    public void setup() {
        addSetting(main);
        addSetting(outline);
        addSetting(obsidianColor);
        addSetting(bedrockColor);
        addSetting(lineAlpha);
        addSetting(range);
    }
    
    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent renderEvent) {
        renderMain(new Color((int) obbyRed.getValue(), (int) obbyGreen.getValue(), (int) obbyBlue.getValue(), (int) obbyAlpha.getValue()), new Color((int) bRockRed.getValue(), (int) bRockGreen.getValue(), (int) bRockBlue.getValue(), (int) bRockAlpha.getValue()));
        renderOutline(new Color((int) obbyRed.getValue(), (int) obbyGreen.getValue(), (int) obbyBlue.getValue(), (int) lineAlpha.getValue()), new Color((int) bRockRed.getValue(), (int) bRockGreen.getValue(), (int) bRockBlue.getValue(), (int) lineAlpha.getValue()));
    }

    public void renderMain(Color obbyColor, Color bRockColor) {
        findObsidianHoles().stream().filter(hole -> mc.player.getDistanceSq(hole) <= (range.getValue() * range.getValue())).forEach(hole -> {
                switch (main.getValue()) {
                    case 0:
                        RenderUtil.drawBoxBlockPos(hole, mainHeight.getValue() - 1, obbyColor);
                        break;
                    case 1:

                }
            });

        findBedRockHoles().stream().filter(hole -> mc.player.getDistanceSq(hole) <= (range.getValue() * range.getValue())).forEach(hole -> {
                switch (main.getValue()) {
                    case 0:
                        RenderUtil.drawBoxBlockPos(hole, mainHeight.getValue() - 1, bRockColor);
                        break;
                    case 1:

                }
            });
    }

    public void renderOutline(Color obbyColor, Color bRockColor) {
        findObsidianHoles().stream().filter(hole -> mc.player.getDistanceSq(hole) <= (range.getValue() * range.getValue())).forEach(hole -> {
                switch (outline.getValue()) {
                    case 0:
                        GL11.glLineWidth((float) lineWidth.getValue());
                        RenderUtil.drawBoundingBoxBlockPos(hole, outlineHeight.getValue() - 1, obbyColor);
                        break;
                    case 1:
                        break;
                }
            });

            findBedRockHoles().stream().filter(hole -> mc.player.getDistanceSq(hole) <= (range.getValue() * range.getValue())).forEach(hole -> {
                switch (outline.getValue()) {
                    case 0:
                        GL11.glLineWidth((float) lineWidth.getValue());
                        RenderUtil.drawBoundingBoxBlockPos(hole, outlineHeight.getValue() - 1, bRockColor);
                        break;
                    case 1:
                        break;
                }
        });
    }

    private List<BlockPos> findObsidianHoles() {
        NonNullList positions = NonNullList.create();
        positions.addAll(BlockUtil.getSphere(CrystalUtil.getPlayerPos(), (int) range.getValue(), (int) range.getValue(), false, true, 0).stream().filter(HoleUtil::IsObbyHole).collect(Collectors.toList()));
        return positions;
    }

    private List<BlockPos> findBedRockHoles() {
        NonNullList positions = NonNullList.create();
        positions.addAll(BlockUtil.getSphere(CrystalUtil.getPlayerPos(), (int) range.getValue(), (int) range.getValue(), false, true, 0).stream().filter(HoleUtil::IsBRockHole).collect(Collectors.toList()));
        return positions;
    }

    @Override
    public String getHUDData() {
        return " " + main.getMode(main.getValue()) + ", " + outline.getMode(outline.getValue());
    }
}

package me.linus.momentum.module.modules.render;

import me.linus.momentum.event.events.render.Render3DEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.combat.CrystalUtil;
import me.linus.momentum.util.render.GeometryMasks;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.BlockUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Iterator;
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

    private static final Mode mode = new Mode("Mode", "Highlight", "Glow", "Box", "Flat", "WireFrame", "WireFrame-Flat");
    private static final SubCheckbox doubles = new SubCheckbox(mode, "Doubles", false);

    public static Checkbox obsidianColor = new Checkbox("Obsidian Color", true);
    public static SubSlider obbyRed = new SubSlider(obsidianColor, "Red", 0.0D, 93.0D, 255.0D, 0);
    public static SubSlider obbyGreen = new SubSlider(obsidianColor, "Green", 0.0D, 235.0D, 255.0D, 0);
    public static SubSlider obbyBlue = new SubSlider(obsidianColor, "Blue", 0.0D, 240.0D, 255.0D, 0);
    public static SubSlider obbyAlpha = new SubSlider(obsidianColor, "Alpha", 0.0D, 30.0D, 255.0D, 0);

    public static Checkbox bedrockColor = new Checkbox("Bedrock Color", true);
    public static SubSlider bRockRed = new SubSlider(bedrockColor, "Red", 0.0D, 141.0D, 255.0D, 0);
    public static SubSlider bRockGreen = new SubSlider(bedrockColor, "Green", 0.0D, 75.0D, 255.0D, 0);
    public static SubSlider bRockBlue = new SubSlider(bedrockColor, "Blue", 0.0D, 255.0D, 255.0D, 0);
    public static SubSlider bRockAlpha = new SubSlider(bedrockColor, "Alpha", 0.0D, 191.0D, 255.0D, 0);

    public static Slider range = new Slider("Range", 0.0D, 7.0D, 10.0D, 0);
    private static final Slider lineWidth = new Slider("Line Width", 0.0D, 1.3D, 5.0D, 1);
    private static final Slider height = new Slider("Height", 0.0D, 1.0D, 3.0D, 1);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(obsidianColor);
        addSetting(bedrockColor);
        addSetting(range);
        addSetting(lineWidth);
        addSetting(height);
    }

    BlockPos render;

    @Override
    public void onDisable() {
        render = null;
    }

    @Override
    public void onUpdate() {
        List<BlockPos> bRockHoles = findBRockHoles();
        List<BlockPos> obbyHoles = findObbyHoles();
        BlockPos shouldRender = null;
        Iterator<BlockPos> iterator = bRockHoles.iterator();

        while (iterator.hasNext())
            shouldRender = iterator.next();

        iterator = obbyHoles.iterator();

        while (iterator.hasNext())
            shouldRender = iterator.next();

        render = shouldRender;
    }

    @Override
    public void onRender3D(Render3DEvent renderEvent) {
        if (render != null) {
            for (BlockPos hole : findObbyHoles()) {
                switch (mode.getValue()) {
                    case 0: {
                        RenderUtil.drawBoundingBoxBottomBlockPos(hole, (float) lineWidth.getValue(), new Color((int) obbyRed.getValue(), (int) obbyGreen.getValue(), (int) obbyBlue.getValue(), 144));

                        if (CrystalUtil.getPlayerPos().getDistance(hole.x, hole.y, hole.z) <= 0.5) {
                            RenderUtil.enableGLGlow();
                            RenderUtil.drawGlowBox(hole, new Color((int) obbyRed.getValue(), (int) obbyGreen.getValue(), (int) obbyBlue.getValue(), 8), new Color((int) obbyRed.getValue(), (int) obbyGreen.getValue(), (int) obbyBlue.getValue(), 0), new Color((int) obbyRed.getValue(), (int) obbyGreen.getValue(), (int) obbyBlue.getValue(), 125));
                            RenderUtil.disableGLGlow();
                        }

                        break;
                    }

                    case 1: {
                        RenderUtil.drawBoundingBoxBottomBlockPos(hole, (float) lineWidth.getValue(), new Color((int) obbyRed.getValue(), (int) obbyGreen.getValue(), (int) obbyBlue.getValue(), 144));

                        RenderUtil.enableGLGlow();
                        RenderUtil.drawBetterGlowBox(hole, new Color((int) obbyRed.getValue(), (int) obbyGreen.getValue(), (int) obbyBlue.getValue(), 125), new Color((int) obbyRed.getValue(), (int) obbyGreen.getValue(), (int) obbyBlue.getValue(), 0));
                        RenderUtil.disableGLGlow();
                        break;
                    }

                    case 2: {
                        RenderUtil.drawVanillaBoxFromBlockPos(hole, (int) obbyRed.getValue(), (int) obbyGreen.getValue(), (int) obbyBlue.getValue(), (int) obbyAlpha.getValue());
                        RenderUtil.prepareRender(GL11.GL_QUADS);
                        RenderUtil.drawBoundingBoxBlockPos(hole, (float) lineWidth.getValue(), new Color((int) obbyRed.getValue(), (int) obbyGreen.getValue(), (int) obbyBlue.getValue(), 144));
                        RenderUtil.releaseRender();
                        break;
                    }

                    case 3: {
                        RenderUtil.prepareRender(GL11.GL_QUADS);
                        RenderUtil.drawBoxFromBlockPos(hole, new Color((int) obbyRed.getValue(), (int) obbyGreen.getValue(), (int) obbyBlue.getValue(), (int) obbyAlpha.getValue()), GeometryMasks.Quad.DOWN);
                        RenderUtil.releaseRender();
                        break;
                    }

                    case 4: {
                        RenderUtil.prepareRender(GL11.GL_QUADS);
                        RenderUtil.drawBoundingBoxBlockPos(hole, (float) lineWidth.getValue(), new Color((int) obbyRed.getValue(), (int) obbyGreen.getValue(), (int) obbyBlue.getValue(), 144));
                        RenderUtil.releaseRender();
                        break;
                    }

                    case 5: {
                        RenderUtil.prepareRender(GL11.GL_QUADS);
                        RenderUtil.drawBoundingBoxBottomBlockPos(hole, (float) lineWidth.getValue(), new Color((int) obbyRed.getValue(), (int) obbyGreen.getValue(), (int) obbyBlue.getValue(), 144));
                        RenderUtil.releaseRender();
                        break;
                    }
                }
            }

            for (BlockPos hole : findBRockHoles()) {
                switch (mode.getValue()) {
                    case 0: {
                        RenderUtil.drawBoundingBoxBottomBlockPos(hole, (float) lineWidth.getValue(), new Color((int) bRockRed.getValue(), (int) bRockGreen.getValue(), (int) bRockBlue.getValue(), 144));

                        if (CrystalUtil.getPlayerPos().getDistance(hole.x, hole.y, hole.z) <= 0.5) {
                            RenderUtil.enableGLGlow();
                            RenderUtil.drawBetterGlowBox(hole, new Color((int) bRockRed.getValue(), (int) bRockGreen.getValue(), (int) bRockBlue.getValue(), 125), new Color((int) bRockRed.getValue(), (int) bRockGreen.getValue(), (int) bRockBlue.getValue(), 0));
                            RenderUtil.disableGLGlow();
                        }

                        break;
                    }

                    case 1: {
                        RenderUtil.drawBoundingBoxBottomBlockPos(hole, (float) lineWidth.getValue(), new Color((int) bRockRed.getValue(), (int) bRockGreen.getValue(), (int) bRockBlue.getValue(), 144));

                        RenderUtil.enableGLGlow();
                        RenderUtil.drawGradientBoxFromBlockPos(hole, new Color((int) bRockRed.getValue(), (int) bRockGreen.getValue(), (int) bRockBlue.getValue(), 144), new Color((int) bRockRed.getValue(), (int) bRockGreen.getValue(), (int) bRockBlue.getValue(), 8), GeometryMasks.Quad.ALL);
                        RenderUtil.disableGLGlow();
                        break;
                    }

                    case 2: {
                        RenderUtil.drawVanillaBoxFromBlockPos(hole, (int) bRockRed.getValue(), (int) bRockGreen.getValue(), (int) bRockBlue.getValue(), (int) bRockAlpha.getValue());
                        RenderUtil.prepareRender(GL11.GL_QUADS);
                        RenderUtil.drawBoundingBoxBlockPos(hole, (float) lineWidth.getValue(), new Color((int) bRockRed.getValue(), (int) bRockGreen.getValue(), (int) bRockBlue.getValue(), 144));
                        RenderUtil.releaseRender();
                        break;
                    }

                    case 3: {
                        RenderUtil.prepareRender(GL11.GL_QUADS);
                        RenderUtil.drawBoxFromBlockPos(hole, new Color((int) bRockRed.getValue(), (int) bRockGreen.getValue(), (int) bRockBlue.getValue(), (int) bRockAlpha.getValue()), GeometryMasks.Quad.DOWN);
                        RenderUtil.releaseRender();
                        break;
                    }

                    case 4: {
                        RenderUtil.prepareRender(GL11.GL_QUADS);
                        RenderUtil.drawBoundingBoxBlockPos(hole, (float) lineWidth.getValue(), new Color((int) bRockRed.getValue(), (int) bRockGreen.getValue(), (int) bRockBlue.getValue(), 144));
                        RenderUtil.releaseRender();
                        break;
                    }

                    case 5: {
                        RenderUtil.prepareRender(GL11.GL_QUADS);
                        RenderUtil.drawBoundingBoxBottomBlockPos(hole, (float) lineWidth.getValue(), new Color((int) bRockRed.getValue(), (int) bRockGreen.getValue(), (int) bRockBlue.getValue(), 144));
                        RenderUtil.releaseRender();
                        break;
                    }
                }
            }
        }
    }

    private List<BlockPos> findObbyHoles() {
        NonNullList positions = NonNullList.create();
        positions.addAll(BlockUtils.getSphere(CrystalUtil.getPlayerPos(), (int) range.getValue(), (int) range.getValue(), false, true, 0).stream().filter(BlockUtils::IsObbyHole).collect(Collectors.toList()));
        return positions;
    }

    private List<BlockPos> findBRockHoles() {
        NonNullList positions = NonNullList.create();
        positions.addAll(BlockUtils.getSphere(CrystalUtil.getPlayerPos(), (int) range.getValue(), (int) range.getValue(), false, true, 0).stream().filter(BlockUtils::IsBRockHole).collect(Collectors.toList()));
        return positions;
    }

    @Override
    public String getHUDData() {
        String subText = "";
        String postText = "";
        if (mode.getValue() == 1 || mode.getValue() == 2 || mode.getValue() == 4)
            subText = "Filled";

        if (mode.getValue() == 0 || mode.getValue() == 3 || mode.getValue() == 5)
            subText = "WireFrame";

        if (mode.getValue() == 0 || mode.getValue() == 1)
            postText = "Glow";

        if (mode.getValue() == 3 || mode.getValue() == 5)
            postText = "Flat";

        if (mode.getValue() == 2 || mode.getValue() == 4)
            postText = "Box";

        return " " + subText + ", " + postText;
    }
}

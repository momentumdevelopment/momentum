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
import net.minecraft.init.Blocks;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
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

    private static final Mode mode = new Mode("Mode", "Highlight", "Glow", "Box", "Flat", "Wire-Frame", "Wire-Frame Flat");
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

    private BlockPos render;

    @Override
    public void onDisable() {
        this.render = null;
    }

    @Override
    public void onUpdate() {
        BlockPos blockPos;
        List<BlockPos> bRockHoles = this.findBRockHoles();
        List<BlockPos> obbyHoles = this.findObbyHoles();
        BlockPos shouldRender = null;
        Iterator<BlockPos> iterator = bRockHoles.iterator();
        while (iterator.hasNext()) {
            shouldRender = blockPos = iterator.next();
        }

        iterator = obbyHoles.iterator();
        while (iterator.hasNext()) {
            shouldRender = blockPos = iterator.next();
        }

        this.render = shouldRender;
    }


    @Override
    public void onRender3D(Render3DEvent renderEvent) {
        if (this.render != null) {
            for (BlockPos hole : this.findObbyHoles()) {
                if (mode.getValue() == 0) {
                    RenderUtil.drawBoundingBoxBottomBlockPos(hole, (float) lineWidth.getValue(), new Color((int) obbyRed.getValue(),(int)  obbyGreen.getValue(),(int)  obbyBlue.getValue(), 144));

                    if (CrystalUtil.getPlayerPos().getDistance(hole.x, hole.y, hole.z) <= 0.5) {
                        RenderUtil.enableGLGlow();
                        RenderUtil.drawGlowBox(hole, new Color((int) obbyRed.getValue(),(int)  obbyGreen.getValue(),(int)  obbyBlue.getValue(), 8), new Color((int) obbyRed.getValue(),(int)  obbyGreen.getValue(),(int)  obbyBlue.getValue(), 0), new Color((int) obbyRed.getValue(),(int)  obbyGreen.getValue(),(int)  obbyBlue.getValue(), 125));
                        RenderUtil.disableGLGlow();
                    }
                }

                if (mode.getValue() == 1) {
                    RenderUtil.drawBoundingBoxBottomBlockPos(hole, (float) lineWidth.getValue(), new Color((int) obbyRed.getValue(),(int)  obbyGreen.getValue(),(int)  obbyBlue.getValue(), 144));

                    RenderUtil.enableGLGlow();
                    RenderUtil.drawBetterGlowBox(hole, new Color((int) obbyRed.getValue(),(int)  obbyGreen.getValue(),(int)  obbyBlue.getValue(), 125), new Color((int) obbyRed.getValue(),(int)  obbyGreen.getValue(),(int)  obbyBlue.getValue(), 0));
                    RenderUtil.disableGLGlow();
                }

                if (mode.getValue() == 2) {
                    RenderUtil.drawVanillaBoxFromBlockPos(hole, (int) obbyRed.getValue(), (int) obbyGreen.getValue(), (int) obbyBlue.getValue(), (int) obbyAlpha.getValue());
                    RenderUtil.prepareRender(GL11.GL_QUADS);
                    RenderUtil.drawBoundingBoxBlockPos(hole, (float) lineWidth.getValue(), new Color((int) obbyRed.getValue(),(int)  obbyGreen.getValue(),(int)  obbyBlue.getValue(), 144));
                    RenderUtil.releaseRender();
                }

                if (mode.getValue() == 3) {
                    RenderUtil.prepareRender(GL11.GL_QUADS);
                    RenderUtil.drawBoxFromBlockPos(hole, new Color((int) obbyRed.getValue(),(int)  obbyGreen.getValue(),(int)  obbyBlue.getValue(), (int) obbyAlpha.getValue()), GeometryMasks.Quad.DOWN);
                    RenderUtil.releaseRender();
                }

                if (mode.getValue() == 4) {
                    RenderUtil.prepareRender(GL11.GL_QUADS);
                    RenderUtil.drawBoundingBoxBlockPos(hole, (float) lineWidth.getValue(), new Color((int) obbyRed.getValue(),(int)  obbyGreen.getValue(),(int)  obbyBlue.getValue(), 144));
                    RenderUtil.releaseRender();
                }

                if (mode.getValue() == 5) {
                    RenderUtil.prepareRender(GL11.GL_QUADS);
                    RenderUtil.drawBoundingBoxBottomBlockPos(hole, (float) lineWidth.getValue(), new Color((int) obbyRed.getValue(),(int)  obbyGreen.getValue(),(int)  obbyBlue.getValue(), 144));
                    RenderUtil.releaseRender();
                }
            }

            for (BlockPos hole : this.findBRockHoles()) {
                if (mode.getValue() == 0) {
                    RenderUtil.drawBoundingBoxBottomBlockPos(hole, (float) lineWidth.getValue(), new Color((int) bRockRed.getValue(),(int)  bRockGreen.getValue(),(int)  bRockBlue.getValue(), 144));

                    if (CrystalUtil.getPlayerPos().getDistance(hole.x, hole.y, hole.z) <= 0.5) {
                        RenderUtil.enableGLGlow();
                        RenderUtil.drawBetterGlowBox(hole, new Color((int) bRockRed.getValue(),(int)  bRockGreen.getValue(),(int)  bRockBlue.getValue(), 125), new Color((int) bRockRed.getValue(),(int)  bRockGreen.getValue(),(int)  bRockBlue.getValue(), 0));
                        RenderUtil.disableGLGlow();
                    }
                }

                if (mode.getValue() == 1) {
                    RenderUtil.drawBoundingBoxBottomBlockPos(hole, (float) lineWidth.getValue(), new Color((int) bRockRed.getValue(),(int)  bRockGreen.getValue(),(int)  bRockBlue.getValue(), 144));

                    RenderUtil.enableGLGlow();
                    RenderUtil.drawGlowBox(hole, new Color((int) bRockRed.getValue(),(int)  bRockGreen.getValue(),(int)  bRockBlue.getValue(), 8), new Color((int) bRockRed.getValue(),(int)  bRockGreen.getValue(),(int)  bRockBlue.getValue(), 0), new Color((int) bRockRed.getValue(),(int)  bRockGreen.getValue(),(int)  bRockBlue.getValue(), 125));
                    RenderUtil.disableGLGlow();
                }

                if (mode.getValue() == 2) {
                    RenderUtil.drawVanillaBoxFromBlockPos(hole, (int) bRockRed.getValue(), (int) bRockGreen.getValue(), (int) bRockBlue.getValue(), (int) bRockAlpha.getValue());
                    RenderUtil.prepareRender(GL11.GL_QUADS);
                    RenderUtil.drawBoundingBoxBlockPos(hole, (float) lineWidth.getValue(), new Color((int) bRockRed.getValue(),(int)  bRockGreen.getValue(),(int)  bRockBlue.getValue(), 144));
                    RenderUtil.releaseRender();
                }

                if (mode.getValue() == 3) {
                    RenderUtil.prepareRender(GL11.GL_QUADS);
                    RenderUtil.drawBoxFromBlockPos(hole, new Color((int) bRockRed.getValue(),(int)  bRockGreen.getValue(),(int)  bRockBlue.getValue(), (int) bRockAlpha.getValue()), GeometryMasks.Quad.DOWN);
                    RenderUtil.releaseRender();
                }

                if (mode.getValue() == 4) {
                    RenderUtil.prepareRender(GL11.GL_QUADS);
                    RenderUtil.drawBoundingBoxBlockPos(hole, (float) lineWidth.getValue(), new Color((int) bRockRed.getValue(),(int)  bRockGreen.getValue(),(int)  bRockBlue.getValue(), 144));
                    RenderUtil.releaseRender();
                }

                if (mode.getValue() == 5) {
                    RenderUtil.prepareRender(GL11.GL_QUADS);
                    RenderUtil.drawBoundingBoxBottomBlockPos(hole, (float) lineWidth.getValue(), new Color((int) bRockRed.getValue(),(int)  bRockGreen.getValue(),(int)  bRockBlue.getValue(), 144));
                    RenderUtil.releaseRender();
                }
            }
        }
    }


    private List<BlockPos> findObbyHoles() {
        NonNullList positions = NonNullList.create();
        positions.addAll(getSphere(CrystalUtil.getPlayerPos(), (int) range.getValue(), (int) range.getValue(), false, true, 0).stream().filter(this::IsObbyHole).collect(Collectors.toList()));
        return positions;
    }

    private List<BlockPos> findBRockHoles() {
        NonNullList positions = NonNullList.create();
        positions.addAll(getSphere(CrystalUtil.getPlayerPos(), (int) range.getValue(), (int) range.getValue(), false, true, 0).stream().filter(this::IsBRockHole).collect(Collectors.toList()));
        return positions;
    }

    private boolean IsObbyHole(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 0, 0);
        BlockPos boost3 = blockPos.add(0, 0, -1);
        BlockPos boost4 = blockPos.add(1, 0, 0);
        BlockPos boost5 = blockPos.add(-1, 0, 0);
        BlockPos boost6 = blockPos.add(0, 0, 1);
        BlockPos boost7 = blockPos.add(0, 2, 0);
        BlockPos boost8 = blockPos.add(0.5, 0.5, 0.5);
        BlockPos boost9 = blockPos.add(0, -1, 0);
        return !(HoleESP.mc.world.getBlockState(boost).getBlock() != Blocks.AIR || this.IsBRockHole(blockPos) || HoleESP.mc.world.getBlockState(boost2).getBlock() != Blocks.AIR || HoleESP.mc.world.getBlockState(boost7).getBlock() != Blocks.AIR || HoleESP.mc.world.getBlockState(boost3).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(boost3).getBlock() != Blocks.BEDROCK || HoleESP.mc.world.getBlockState(boost4).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(boost4).getBlock() != Blocks.BEDROCK || HoleESP.mc.world.getBlockState(boost5).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(boost5).getBlock() != Blocks.BEDROCK || HoleESP.mc.world.getBlockState(boost6).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(boost6).getBlock() != Blocks.BEDROCK || HoleESP.mc.world.getBlockState(boost8).getBlock() != Blocks.AIR || HoleESP.mc.world.getBlockState(boost9).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(boost9).getBlock() != Blocks.BEDROCK);
    }

    private boolean IsBRockHole(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 0, 0);
        BlockPos boost3 = blockPos.add(0, 0, -1);
        BlockPos boost4 = blockPos.add(1, 0, 0);
        BlockPos boost5 = blockPos.add(-1, 0, 0);
        BlockPos boost6 = blockPos.add(0, 0, 1);
        BlockPos boost7 = blockPos.add(0, 2, 0);
        BlockPos boost8 = blockPos.add(0.5, 0.5, 0.5);
        BlockPos boost9 = blockPos.add(0, -1, 0);

        return HoleESP.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(boost7).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(boost3).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(boost4).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(boost5).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(boost6).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(boost8).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(boost9).getBlock() == Blocks.BEDROCK;
    }

    public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        int x = cx - (int)r;
        while ((float)x <= (float)cx + r) {
            int z = cz - (int)r;
            while ((float)z <= (float)cz + r) {
                int y = sphere ? cy - (int)r : cy;
                do {
                    float f = sphere ? (float)cy + r : (float)(cy + h);
                    if (!((float)y < f)) break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (!(!(dist < (double)(r * r)) || hollow && dist < (double)((r - 1.0f) * (r - 1.0f)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    } ++y;
                } while (true);
                ++z;
            } ++x;
        }

        return circleblocks;
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

package me.linus.momentum.module.modules.combat;

import me.linus.momentum.event.events.render.Render3DEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.EntityUtil;
import me.linus.momentum.util.world.InventoryUtil;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author linustouchtips & olliem5
 * @since 12/11/2020
 */

public class Surround extends Module {
    public Surround() {
        super("Surround", Category.COMBAT, "Surrounds your feet with obsidian");
    }

    private static final Mode mode = new Mode("Mode", "Standard", "Full", "Anti-City");
    private static final Mode disable = new Mode("Disable", "Jump", "Completion", "Never");
    public static Slider blocksPerTick = new Slider("Blocks Per Tick", 0.0D, 1.0D, 6.0D, 0);

    private static final Checkbox timeout = new Checkbox("Timeout", true);
    public static SubSlider timeoutTick = new SubSlider(timeout, "Timeout Ticks", 1.0D, 15.0D, 20.0D, 1);

    private static final Checkbox rotate = new Checkbox("Rotate", true);
    private static final Checkbox centerPlayer = new Checkbox("Center", true);
    private static final Checkbox onlyObsidian = new Checkbox("Only Obsidian", true);
    private static final Checkbox antiChainPop = new Checkbox("Anti-ChainPop", true);
    private static final Checkbox chorusSave = new Checkbox("Chorus Save", false);

    public static Checkbox color = new Checkbox("Color", true);
    public static SubSlider r = new SubSlider(color, "Red", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider g = new SubSlider(color, "Green", 0.0D, 255.0D, 255.0D, 0);
    public static SubSlider b = new SubSlider(color, "Blue", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider a = new SubSlider(color, "Alpha", 0.0D, 5.0D, 255.0D, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(disable);
        addSetting(blocksPerTick);
        addSetting(timeout);
        addSetting(rotate);
        addSetting(centerPlayer);
        addSetting(onlyObsidian);
        addSetting(antiChainPop);
        addSetting(chorusSave);
        addSetting(color);
    }

    boolean hasPlaced;
    Vec3d center = Vec3d.ZERO;
    int blocksPlaced = 0;
    BlockPos northBlockPos;
    BlockPos southBlockPos;
    BlockPos eastBlockPos;
    BlockPos westBlockPos;

    @Override
    public void onEnable() {
        hasPlaced = false;
        center = PlayerUtil.getCenter(mc.player.posX, mc.player.posY, mc.player.posZ);

        if (centerPlayer.getValue()) {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
            mc.player.connection.sendPacket(new CPacketPlayer.Position(center.x, center.y, center.z, true));
            mc.player.setPosition(center.x, center.y, center.z);
        }
    }

    public void onUpdate() {
        if (nullCheck())
            return;

        final Vec3d vec3d = EntityUtil.getInterpolatedPos(mc.player, 0);
        northBlockPos = new BlockPos(vec3d).north();
        southBlockPos = new BlockPos(vec3d).south();
        eastBlockPos = new BlockPos(vec3d).east();
        westBlockPos = new BlockPos(vec3d).west();

        switch (disable.getValue()) {
            case 0:
                if (!mc.player.onGround)
                    this.disable();
                break;
            case 1:
                if (hasPlaced)
                    this.disable();
                break;
            case 2:
                if (timeout.getValue()) {
                    if (mode.getValue() != 2) {
                        if (mc.player.ticksExisted % timeoutTick.getValue() == 0)
                            this.disable();
                    }
                }

                break;
        }

        surroundPlayer();

        if (blocksPlaced == 0)
            hasPlaced = true;
    }

    public void surroundPlayer() {
        for (Vec3d placePositions : getSurround()) {
            BlockPos blockPos = new BlockPos(placePositions.add(mc.player.getPositionVector()));

            if (mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR)) {
                int oldInventorySlot = mc.player.inventory.currentItem;

                if (onlyObsidian.getValue())
                    mc.player.inventory.currentItem = InventoryUtil.getBlockInHotbar(Blocks.OBSIDIAN);
                else
                    mc.player.inventory.currentItem = InventoryUtil.getAnyBlockInHotbar();

                PlayerUtil.placeBlock(blockPos, rotate.getValue());
                mc.player.inventory.currentItem = oldInventorySlot;
                blocksPlaced++;

                if (blocksPlaced == blocksPerTick.getValue() && disable.getValue() != 2)
                    return;
            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent eventRender) {
        if (northBlockPos != null)
            RenderUtil.drawVanillaBoxFromBlockPos(northBlockPos, new Color((int) r.getValue(), (int) g.getValue(),  (int) b.getValue(), (int) a.getValue()));

        if (westBlockPos != null)
            RenderUtil.drawVanillaBoxFromBlockPos(westBlockPos, new Color((int) r.getValue(), (int) g.getValue(),  (int) b.getValue(), (int) a.getValue()));

        if (southBlockPos != null)
            RenderUtil.drawVanillaBoxFromBlockPos(southBlockPos, new Color((int) r.getValue(), (int) g.getValue(),  (int) b.getValue(), (int) a.getValue()));

        if (eastBlockPos != null)
            RenderUtil.drawVanillaBoxFromBlockPos(eastBlockPos, new Color((int) r.getValue(), (int) g.getValue(),  (int) b.getValue(), (int) a.getValue()));
    }

    public List<Vec3d> getSurround() {
        switch (mode.getValue()) {
            case 0:
                return standardSurround;
            case 1:
                return fullSurround;
            case 2:
                return antiCitySurround;
        }

        return standardSurround;
    }

    private final List<Vec3d> standardSurround = new ArrayList<>(Arrays.asList(
            new Vec3d(0, -1, 0),
            new Vec3d(1, 0, 0),
            new Vec3d(-1, 0, 0),
            new Vec3d(0, 0, 1),
            new Vec3d(0, 0, -1)
    ));

    private final List<Vec3d> fullSurround = new ArrayList<>(Arrays.asList(
            new Vec3d(0, -1, 0),
            new Vec3d(1, -1, 0),
            new Vec3d(0, -1, 1),
            new Vec3d(-1, -1, 0),
            new Vec3d(0, -1, -1),
            new Vec3d(1, 0, 0),
            new Vec3d(0, 0, 1),
            new Vec3d(-1, 0, 0),
            new Vec3d(0, 0, -1)
    ));

    private final List<Vec3d> antiCitySurround = new ArrayList<>(Arrays.asList(
            new Vec3d(0, -1, 0),
            new Vec3d(1, 0, 0),
            new Vec3d(-1, 0, 0),
            new Vec3d(0, 0, 1),
            new Vec3d(0, 0, -1),
            new Vec3d(2, 0, 0),
            new Vec3d(-2, 0, 0),
            new Vec3d(0, 0, 2),
            new Vec3d(0, 0, -2),
            new Vec3d(3, 0, 0),
            new Vec3d(-3, 0, 0),
            new Vec3d(0, 0, 3),
            new Vec3d(0, 0, -3)
    ));
}

package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.BlockUtil;
import me.linus.momentum.util.world.EntityUtil;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.player.PlayerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
    private static final Mode centerPlayer = new Mode("Center", "Teleport", "NCP", "None");
    public static Slider blocksPerTick = new Slider("Blocks Per Tick", 0.0D, 1.0D, 6.0D, 0);

    private static final Checkbox timeout = new Checkbox("Timeout", true);
    public static SubSlider timeoutTick = new SubSlider(timeout, "Timeout Ticks", 1.0D, 15.0D, 20.0D, 1);

    private static final Checkbox rotate = new Checkbox("Rotate", true);
    private static final Checkbox onlyObsidian = new Checkbox("Only Obsidian", true);
    private static final Checkbox antiChainPop = new Checkbox("Anti-ChainPop", true);
    private static final Checkbox chorusSave = new Checkbox("Chorus Save", false);

    public static Checkbox renderSurround = new Checkbox("Render", true);
    public static SubSlider r = new SubSlider(renderSurround, "Red", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider g = new SubSlider(renderSurround, "Green", 0.0D, 255.0D, 255.0D, 0);
    public static SubSlider b = new SubSlider(renderSurround, "Blue", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider a = new SubSlider(renderSurround, "Alpha", 0.0D, 40.0D, 255.0D, 0);

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
        addSetting(renderSurround);
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
        if (nullCheck())
            return;

        super.onEnable();

        hasPlaced = false;
        center = PlayerUtil.getCenter(mc.player.posX, mc.player.posY, mc.player.posZ);

        switch (centerPlayer.getValue()) {
            case 0:
                mc.player.motionX = 0;
                mc.player.motionZ = 0;
                mc.player.connection.sendPacket(new CPacketPlayer.Position(center.x, center.y, center.z, true));
                mc.player.setPosition(center.x, center.y, center.z);
                break;
            case 1:
                mc.player.motionX = (center.x - mc.player.posX) / 2;
                mc.player.motionZ = (center.z - mc.player.posZ) / 2;
                break;
            case 2:
                break;
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
                if (timeout.getValue() && mode.getValue() != 2)
                    if (mc.player.ticksExisted % timeoutTick.getValue() == 0)
                            this.disable();
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
                    InventoryUtil.switchToSlot(InventoryUtil.getBlockInHotbar(Blocks.OBSIDIAN));
                else
                    InventoryUtil.switchToSlot(InventoryUtil.getAnyBlockInHotbar());

                BlockUtil.placeBlock(blockPos, rotate.getValue());
                InventoryUtil.switchToSlot(oldInventorySlot);
                blocksPlaced++;

                if (blocksPlaced == blocksPerTick.getValue() && disable.getValue() != 2)
                    return;
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (renderSurround.getValue()) {
            if (northBlockPos != null && (mc.world.getBlockState(southBlockPos).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(southBlockPos).getBlock() == Blocks.BEDROCK))
                RenderUtil.drawBoxBlockPos(northBlockPos, 0, new Color((int) r.getValue(), (int) g.getValue(), (int) b.getValue(), (int) a.getValue()));
            else
                RenderUtil.drawBoxBlockPos(northBlockPos, 0, new Color(255, 0, 0, (int) a.getValue()));

            if (westBlockPos != null && (mc.world.getBlockState(southBlockPos).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(southBlockPos).getBlock() == Blocks.BEDROCK))
                RenderUtil.drawBoxBlockPos(westBlockPos, 0, new Color((int) r.getValue(), (int) g.getValue(), (int) b.getValue(), (int) a.getValue()));
            else
                RenderUtil.drawBoxBlockPos(westBlockPos, 0, new Color(255, 0, 0, (int) a.getValue()));

            if (southBlockPos != null && (mc.world.getBlockState(southBlockPos).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(southBlockPos).getBlock() == Blocks.BEDROCK))
                RenderUtil.drawBoxBlockPos(southBlockPos, 0, new Color((int) r.getValue(), (int) g.getValue(), (int) b.getValue(), (int) a.getValue()));
            else
                RenderUtil.drawBoxBlockPos(southBlockPos, 0, new Color(255, 0, 0, (int) a.getValue()));

            if (eastBlockPos != null && (mc.world.getBlockState(southBlockPos).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(southBlockPos).getBlock() == Blocks.BEDROCK))
                RenderUtil.drawBoxBlockPos(eastBlockPos, 0, new Color((int) r.getValue(), (int) g.getValue(), (int) b.getValue(), (int) a.getValue()));
            else
                RenderUtil.drawBoxBlockPos(eastBlockPos, 0, new Color(255, 0, 0, (int) a.getValue()));
        }
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

    @Override
    public String getHUDData() {
        return " " + centerPlayer.getMode(centerPlayer.getValue());
    }
}

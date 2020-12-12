package me.linus.momentum.module.modules.combat;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.InventoryUtil;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemChorusFruit;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

    private static Mode mode = new Mode("Mode", "Standard", "Full", "Anti-City");
    private static Mode disable = new Mode("Disable", "Jump", "Completion", "Never");
    public static Slider blocksPerTick = new Slider("Blocks Per Tick", 0.0D, 1.0D, 6.0D, 0);

    private static Checkbox timeout = new Checkbox("Timeout", true);
    public static SubSlider timeoutTick = new SubSlider(timeout, "Timeout Ticks", 1.0D, 15.0D, 20.0D, 1);

    private static Checkbox rotate = new Checkbox("Rotate", true);
    private static Checkbox centerPlayer = new Checkbox("Center", true);
    private static Checkbox onlyObsidian = new Checkbox("Only Obsidian", true);
    private static Checkbox antiChainPop = new Checkbox("Anti-ChainPop", true);
    private static Checkbox chorusSave = new Checkbox("Chorus Save", false);

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
    }

    private final List<BlockPos> renderBlocks = new ArrayList<>();
    private boolean hasPlaced;
    private Vec3d center = Vec3d.ZERO;
    int blocksPlaced = 0;
    int holeBlocks;

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

        if (blocksPlaced == 0) {
            hasPlaced = true;
        }
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
                renderBlocks.add(blockPos);
                mc.player.inventory.currentItem = oldInventorySlot;
                blocksPlaced++;

                if (blocksPlaced == blocksPerTick.getValue() && disable.getValue() != 2)
                    return;
            }
        }
    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent renderEvent) {
        for (BlockPos renderBlock : renderBlocks) {
            RenderUtil.drawVanillaBoxFromBlockPos(renderBlock, 0.0f, 1.0f, 0.0f, 0.15f);
        }
    }

    /*
    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketPlayerTryUseItem && mc.player.getHeldItemMainhand().getItem() instanceof ItemChorusFruit) {
            holeBlocks = 0;

            for (Vec3d vec3d : holeOffset) {
                BlockPos offset = new BlockPos(vec3d.x, vec3d.y, vec3d.z);
                if (mc.world.getBlockState(offset).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(offset).getBlock() == Blocks.BEDROCK)
                    ++holeBlocks;

                if (holeBlocks != 5) {
                    if (this.isEnabled()) {
                        surroundPlayer();
                    }
                }
            }
        }
    }
     */

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

    /*
    Vec3d[] holeOffset = {
            mc.player.getPositionVector().addVector(1.0D, 0.0D, 0.0D),
            mc.player.getPositionVector().addVector(-1.0D, 0.0D, 0.0D),
            mc.player.getPositionVector().addVector(0.0D, 0.0D, 1.0D),
            mc.player.getPositionVector().addVector(0.0D, 0.0D, -1.0D),
            mc.player.getPositionVector().addVector(0.0D, -1.0D, 0.0D)
    };
     */
}

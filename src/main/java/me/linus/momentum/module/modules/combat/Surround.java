package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.BlockUtils;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class Surround extends Module {
    public Surround() {
        super("Surround", Category.COMBAT, "Surrounds your feet with obsidian");
    }

    private static Checkbox rotate = new Checkbox("Rotate", true);
    private static Checkbox center = new Checkbox("Center", true);
    private static Checkbox disable = new Checkbox("Disables", false);
    private static Checkbox jumpDisable = new Checkbox("Disables on Jump", true);
    private static Checkbox antiChainPop = new Checkbox("Anti-ChainPop", true);

    BlockPos northFace;
    BlockPos southFace;
    BlockPos westFace;
    BlockPos eastFace;
    boolean placed;

    @Override
    public void setup() {
        addSetting(rotate);
        addSetting(center);
        addSetting(disable);
        addSetting(jumpDisable);
        addSetting(antiChainPop);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null) return;
        if (!mc.player.onGround && jumpDisable.getValue()) {
            toggle();
            return;
        }

        final Vec3d vec3d = EntityUtil.getInterpolatedPos(mc.player, 0);
        BlockPos northBlockPos = new BlockPos(vec3d).north();
        BlockPos southBlockPos = new BlockPos(vec3d).south();
        BlockPos eastBlockPos = new BlockPos(vec3d).east();
        BlockPos westBlockPos = new BlockPos(vec3d).west();
        northFace = northBlockPos;
        southFace = southBlockPos;
        westFace = westBlockPos;
        eastFace = eastBlockPos;

        final int newSlot = findBlockInHotbar();
        if (newSlot == -1)
            return;

        final BlockPos centerPos = mc.player.getPosition();
        double y = centerPos.getY();
        double x = centerPos.getX();
        double z = centerPos.getZ();
        final Vec3d plusPlus = new Vec3d(x + 0.5, y, z + 0.5);
        final Vec3d plusMinus = new Vec3d(x + 0.5, y, z - 0.5);
        final Vec3d minusMinus = new Vec3d(x - 0.5, y, z - 0.5);
        final Vec3d minusPlus = new Vec3d(x - 0.5, y, z + 0.5);

        final int oldSlot = mc.player.inventory.currentItem;
        mc.player.inventory.currentItem = newSlot;

        if (!BlockUtils.hasNeighbour(northBlockPos)) {
            for (EnumFacing side : EnumFacing.values()) {
                BlockPos neighbour = northBlockPos.offset(side);
                if (BlockUtils.hasNeighbour(neighbour)) {
                    northBlockPos = neighbour;
                    break;
                }
            }
        }

        if (!BlockUtils.hasNeighbour(southBlockPos)) {
            for (EnumFacing side : EnumFacing.values()) {
                BlockPos neighbour = southBlockPos.offset(side);
                if (BlockUtils.hasNeighbour(neighbour)) {
                    southBlockPos = neighbour;
                    break;
                }
            }
        }

        if (!BlockUtils.hasNeighbour(eastBlockPos)) {
            for (EnumFacing side : EnumFacing.values()) {
                BlockPos neighbour = eastBlockPos.offset(side);
                if (BlockUtils.hasNeighbour(neighbour)) {
                    eastBlockPos = neighbour;
                    break;
                }
            }
        }

        if (!BlockUtils.hasNeighbour(westBlockPos)) {
            for (EnumFacing side : EnumFacing.values()) {
                BlockPos neighbour = westBlockPos.offset(side);
                if (BlockUtils.hasNeighbour(neighbour)) {
                    westBlockPos = neighbour;
                    break;
                }
            }
        }

        if (mc.world.getBlockState(northBlockPos).getMaterial().isReplaceable() && isEntitiesEmpty(northBlockPos)) {
            if (mc.player.onGround) {
                if (getDistance(plusPlus) < getDistance(plusMinus) && getDistance(plusPlus) < getDistance(minusMinus) && getDistance(plusPlus) < getDistance(minusPlus)) {
                    x = centerPos.getX() + 0.5;
                    z = centerPos.getZ() + 0.5;
                    centerPlayer(x, y, z);
                }

                if (getDistance(plusMinus) < getDistance(plusPlus) && getDistance(plusMinus) < getDistance(minusMinus) && getDistance(plusMinus) < getDistance(minusPlus)) {
                    x = centerPos.getX() + 0.5;
                    z = centerPos.getZ() - 0.5;
                    centerPlayer(x, y, z);
                }

                if (getDistance(minusMinus) < getDistance(plusPlus) && getDistance(minusMinus) < getDistance(plusMinus) && getDistance(minusMinus) < getDistance(minusPlus)) {
                    x = centerPos.getX() - 0.5;
                    z = centerPos.getZ() - 0.5;
                    centerPlayer(x, y, z);
                }

                if (getDistance(minusPlus) < getDistance(plusPlus) && getDistance(minusPlus) < getDistance(plusMinus) && getDistance(minusPlus) < getDistance(minusMinus)) {
                    x = centerPos.getX() - 0.5;
                    z = centerPos.getZ() + 0.5;
                    centerPlayer(x, y, z);
                }
            }

            BlockUtils.placeBlockScaffold(northBlockPos, rotate.getValue());
            placed = true;
        }

        if (mc.world.getBlockState(southBlockPos).getMaterial().isReplaceable() && isEntitiesEmpty(southBlockPos)) {
            if (mc.player.onGround) {
                if (getDistance(plusPlus) < getDistance(plusMinus) && getDistance(plusPlus) < getDistance(minusMinus) && getDistance(plusPlus) < getDistance(minusPlus)) {
                    x = centerPos.getX() + 0.5;
                    z = centerPos.getZ() + 0.5;
                    centerPlayer(x, y, z);
                }

                if (getDistance(plusMinus) < getDistance(plusPlus) && getDistance(plusMinus) < getDistance(minusMinus) && getDistance(plusMinus) < getDistance(minusPlus)) {
                    x = centerPos.getX() + 0.5;
                    z = centerPos.getZ() - 0.5;
                    centerPlayer(x, y, z);
                }

                if (getDistance(minusMinus) < getDistance(plusPlus) && getDistance(minusMinus) < getDistance(plusMinus) && getDistance(minusMinus) < getDistance(minusPlus)) {
                    x = centerPos.getX() - 0.5;
                    z = centerPos.getZ() - 0.5;
                    centerPlayer(x, y, z);
                }

                if (getDistance(minusPlus) < getDistance(plusPlus) && getDistance(minusPlus) < getDistance(plusMinus) && getDistance(minusPlus) < getDistance(minusMinus)) {
                    x = centerPos.getX() - 0.5;
                    z = centerPos.getZ() + 0.5;
                    centerPlayer(x, y, z);
                }
            }

            BlockUtils.placeBlockScaffold(southBlockPos, rotate.getValue());
            placed = true;
        }

        if (mc.world.getBlockState(eastBlockPos).getMaterial().isReplaceable() && isEntitiesEmpty(eastBlockPos)) {
            if (mc.player.onGround) {
                if (getDistance(plusPlus) < getDistance(plusMinus) && getDistance(plusPlus) < getDistance(minusMinus) && getDistance(plusPlus) < getDistance(minusPlus)) {
                    x = centerPos.getX() + 0.5;
                    z = centerPos.getZ() + 0.5;
                    centerPlayer(x, y, z);
                }
                if (getDistance(plusMinus) < getDistance(plusPlus) && getDistance(plusMinus) < getDistance(minusMinus) && getDistance(plusMinus) < getDistance(minusPlus)) {
                    x = centerPos.getX() + 0.5;
                    z = centerPos.getZ() - 0.5;
                    centerPlayer(x, y, z);
                }
                if (getDistance(minusMinus) < getDistance(plusPlus) && getDistance(minusMinus) < getDistance(plusMinus) && getDistance(minusMinus) < getDistance(minusPlus)) {
                    x = centerPos.getX() - 0.5;
                    z = centerPos.getZ() - 0.5;
                    centerPlayer(x, y, z);
                }
                if (getDistance(minusPlus) < getDistance(plusPlus) && getDistance(minusPlus) < getDistance(plusMinus) && getDistance(minusPlus) < getDistance(minusMinus)) {
                    x = centerPos.getX() - 0.5;
                    z = centerPos.getZ() + 0.5;
                    centerPlayer(x, y, z);
                }
            }

            BlockUtils.placeBlockScaffold(eastBlockPos, rotate.getValue());
            placed = true;
        }

        if (mc.world.getBlockState(westBlockPos).getMaterial().isReplaceable() && isEntitiesEmpty(westBlockPos)) {
            if (mc.player.onGround) {
                if (getDistance(plusPlus) < getDistance(plusMinus) && getDistance(plusPlus) < getDistance(minusMinus) && getDistance(plusPlus) < getDistance(minusPlus)) {
                    x = centerPos.getX() + 0.5;
                    z = centerPos.getZ() + 0.5;
                    centerPlayer(x, y, z);
                }

                if (getDistance(plusMinus) < getDistance(plusPlus) && getDistance(plusMinus) < getDistance(minusMinus) && getDistance(plusMinus) < getDistance(minusPlus)) {
                    x = centerPos.getX() + 0.5;
                    z = centerPos.getZ() - 0.5;
                    centerPlayer(x, y, z);
                }

                if (getDistance(minusMinus) < getDistance(plusPlus) && getDistance(minusMinus) < getDistance(plusMinus) && getDistance(minusMinus) < getDistance(minusPlus)) {
                    x = centerPos.getX() - 0.5;
                    z = centerPos.getZ() - 0.5;
                    centerPlayer(x, y, z);
                }

                if (getDistance(minusPlus) < getDistance(plusPlus) && getDistance(minusPlus) < getDistance(plusMinus) && getDistance(minusPlus) < getDistance(minusMinus)) {
                    x = centerPos.getX() - 0.5;
                    z = centerPos.getZ() + 0.5;
                    centerPlayer(x, y, z);
                }
            }

            BlockUtils.placeBlockScaffold(westBlockPos, rotate.getValue());
            placed = true;
        }

        mc.player.inventory.currentItem = oldSlot;
        if ((disable.getValue() || antiChainPop.getValue()) && (mc.world.getBlockState(new BlockPos(vec3d).north()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(new BlockPos(vec3d).north()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(new BlockPos(vec3d).south()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(new BlockPos(vec3d).south()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(new BlockPos(vec3d).west()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(new BlockPos(vec3d).west()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(new BlockPos(vec3d).east()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(new BlockPos(vec3d).east()).getBlock() == Blocks.BEDROCK)) {
            toggle();
        }
    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent eventRender) {
        if (northFace != null)
            RenderUtil.drawVanillaBoxFromBlockPos(northFace, 0, 1, 0, 30 / 255);

        if (southFace != null)
            RenderUtil.drawVanillaBoxFromBlockPos(southFace, 0, 1, 0, 30 / 255);

        if (westFace != null)
            RenderUtil.drawVanillaBoxFromBlockPos(westFace, 0, 1, 0, 30 / 255);

        if (eastFace != null)
            RenderUtil.drawVanillaBoxFromBlockPos(eastFace, 0, 1, 0, 30 / 255);
    }

    private int findBlockInHotbar() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock) stack.getItem()).getBlock();
                if (block instanceof BlockObsidian) {
                    return i;
                }
            }
        }

        return -1;
    }

    private void centerPlayer(double x, double y, double z) {
        if (center.getValue()) {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, true));
            mc.player.setPosition(x, y, z);
        }
    }

    private double getDistance(Vec3d vec) {
        return mc.player.getDistance(vec.x, vec.y, vec.z);
    }

    private boolean isEntitiesEmpty(BlockPos pos) {
        return mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos)).stream().filter(e -> !(e instanceof EntityItem) && !(e instanceof EntityXPOrb)).count() == 0;
    }
}

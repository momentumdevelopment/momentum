package me.linus.momentum.event.events.world;

import me.linus.momentum.event.MomentumEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

/**
 * @author linustouchtips
 * @since 12/21/2020
 */

public class LiquidCollisionEvent extends MomentumEvent {
    AxisAlignedBB boundingBox;
    BlockPos blockPos;

    public LiquidCollisionEvent(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }
}
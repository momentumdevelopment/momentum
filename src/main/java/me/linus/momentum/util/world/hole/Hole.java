package me.linus.momentum.util.world.hole;

import net.minecraft.util.math.BlockPos;

public class Hole {

    BlockPos blockPos;
    Type type;

    public Hole(BlockPos blockPos, Type type) {
        this.blockPos = blockPos;
        this.type = type;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public Type getType() {
        return this.type;
    }

    public enum Type {
        Obsidian,
        Bedrock,
        Mixed
    }
}

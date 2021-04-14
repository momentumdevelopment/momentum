package me.linus.momentum.util.world.hole;

import net.minecraft.util.math.BlockPos;

public class Hole {

    public Type type;
    public Facing facing;
    public BlockPos hole;
    public BlockPos offset;

    public Hole(Type type, Facing facing, BlockPos hole, BlockPos offset) {
        this.type = type;
        this.facing = facing;
        this.hole = hole;
        this.offset = offset;
    }

    public Hole(Type type, Facing facing, BlockPos hole) {
        this.type = type;
        this.facing = facing;
        this.hole = hole;
    }

    public enum Facing {
        West,
        South,
        North,
        East,
        None;
    }

    public Facing opposite() {
        if (this.facing == Facing.West)
            return Facing.East;
        else if (this.facing == Facing.East)
            return Facing.West;
        else if (this.facing == Facing.North)
            return Facing.South;
        else if (this.facing == Facing.South)
            return Facing.North;

        return Facing.None;
    }

    public enum Type {
        Obsidian,
        Bedrock,
        Double
    }
}

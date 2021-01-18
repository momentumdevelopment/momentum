package me.linus.momentum.util.combat.crystal;

import net.minecraft.util.math.BlockPos;

public class CrystalPosition {

    BlockPos crystalPosition;
    double targetDamage;
    double selfDamage;

    public CrystalPosition(BlockPos crystalPosition, double targetDamage, double selfDamage) {
        this.crystalPosition = crystalPosition;
        this.targetDamage = targetDamage;
        this.selfDamage = selfDamage;
    }

    public BlockPos getCrystalPosition() {
        return this.crystalPosition;
    }

    public double getTargetDamage() {
        return this.targetDamage;
    }

    public double getSelfDamage() {
        return this.selfDamage;
    }
}

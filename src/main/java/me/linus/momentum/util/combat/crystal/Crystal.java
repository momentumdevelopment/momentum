package me.linus.momentum.util.combat.crystal;

import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.entity.item.EntityEnderCrystal;

/**
 * @author linustouchtips
 * @since 02/01/2021
 */

public class Crystal implements MixinInterface {

    public EntityEnderCrystal crystal;
    public EntityEnderCrystal lastCrystal;
    double targetDamage;
    double selfDamage;

    public Crystal(EntityEnderCrystal crystal, double targetDamage, double selfDamage) {
        this.crystal = crystal;
        this.targetDamage = targetDamage;
        this.selfDamage = selfDamage;
    }

    public EntityEnderCrystal getCrystal() {
        return this.crystal;
    }

    public EntityEnderCrystal getLastCrystal() {
        return this.crystal;
    }

    public double getTargetDamage() {
        return this.targetDamage;
    }

    public double getSelfDamage() {
        return this.selfDamage;
    }
}

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
    public int swings = 0;

    public Crystal(EntityEnderCrystal crystal) {
        this.crystal = crystal;
    }

    public EntityEnderCrystal getCrystal() {
        return this.crystal;
    }

    public EntityEnderCrystal getLastCrystal() {
        return this.crystal;
    }

    public int getSwings() {
        return this.swings;
    }
}

package me.linus.momentum.module.modules.combat.autocrystal;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

/**
 * @author linustouchtips
 * @since 01/08/2021
 */

public class AutoCrystalAlgorithm implements MixinInterface {

    public EntityPlayer currentTarget = null;
    public EntityEnderCrystal crystal = null;
    public BlockPos placePos = null;
    public double placeDamage = 0;

    public void breakCrystal() {

    }

    public void placeCrystal() {

    }

    public void renderPlacement() {

    }

    public void onToggle() {

    }

    public void onSync(PacketReceiveEvent event) {

    }
}

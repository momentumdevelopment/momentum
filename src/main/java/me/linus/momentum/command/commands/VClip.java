package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.MessageUtil;
import me.linus.momentum.util.player.rotation.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class VClip extends Command implements MixinInterface {
    public VClip() {
        super("vclip");
    }

    @Override
    public void onCommand(String[] args) {
        Vec3d direction = RotationUtil.direction(mc.player.rotationYaw);

        if (direction != null && args.length > 1) {
            Entity entity = mc.player.isRiding() ? mc.player.getRidingEntity() : mc.player;

            entity.setPosition(mc.player.posX, mc.player.posY + Double.valueOf(args[1]), mc.player.posZ);

            MessageUtil.sendClientMessage("Teleported you " + args[1] + " blocks in the vertical direction!");
        }

        else
            MessageUtil.usageException(this, "[height]");
    }

    @Override
    public String getDescription() {
        return "Teleports you in the vertical direction";
    }
}

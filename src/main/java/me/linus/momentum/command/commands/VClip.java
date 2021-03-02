package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.MessageUtil;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class VClip extends Command implements MixinInterface {
    public VClip() {
        super("vclip", "[height]", "Teleports you in the vertical direction");
    }

    @Override
    public void onCommand(String[] args) {
//        Vec3d direction = new Vec3d(Math.cos(MathUtil.degToRad(mc.player.rotationYaw + 90f)), 0, Math.sin(MathUtil.degToRad(mc.player.rotationYaw + 90f)));;

        if (args.length > 1) {
            (mc.player.isRiding() ? mc.player.getRidingEntity() : mc.player).setPosition(mc.player.posX, mc.player.posY + Double.parseDouble(args[1]), mc.player.posZ);

            MessageUtil.addOutput("Teleported you " + args[1] + " blocks in the vertical direction!");
        }

        else
            MessageUtil.usageException(this.getUsage(), this.getUsageException());
    }
}

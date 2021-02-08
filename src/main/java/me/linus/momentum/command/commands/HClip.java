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

public class HClip extends Command implements MixinInterface {
    public HClip() {
        super("hclip");
    }

    @Override
    public void onCommand(String[] args) {
        Vec3d direction = RotationUtil.direction(mc.player.rotationYaw);

        if (direction != null && args.length > 0) {
            (mc.player.isRiding() ? mc.player.getRidingEntity() : mc.player).setPosition(mc.player.posX + direction.x * Double.valueOf(args[1]), mc.player.posY, mc.player.posZ + direction.z * Double.valueOf(args[1]));

            MessageUtil.sendClientMessage("Teleported you " + args[1] + " blocks in the horizontal direction!");
        }

        else
            MessageUtil.usageException(this, "[distance]");
    }

    @Override
    public String getDescription() {
        return "Teleports you in the horizontal direction";
    }

    @Override
    public String getUsageException() {
        return "[distance]";
    }
}

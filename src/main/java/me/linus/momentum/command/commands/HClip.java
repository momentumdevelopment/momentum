package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.MessageUtil;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class HClip extends Command implements MixinInterface {
    public HClip() {
        super("HClip", new String[] {"hclip"});
    }

    @Override
    public void onCommand(String[] args) {
        String[] split = args;

        if (split == null || split.length <= 1) {
            MessageUtil.sendClientMessage("Something went wrong.");
            return;
        }

        final double number = Double.parseDouble(split[1]);

        final Vec3d direction = PlayerUtil.direction(mc.player.rotationYaw);

        if (direction != null) {
            Entity entity = mc.player.isRiding() ? mc.player.getRidingEntity() : mc.player;

            entity.setPosition(mc.player.posX + direction.x * number, mc.player.posY, mc.player.posZ + direction.z * number);

            MessageUtil.sendClientMessage(String.format("Teleported you %s blocks forward", number));
        }
    }
}

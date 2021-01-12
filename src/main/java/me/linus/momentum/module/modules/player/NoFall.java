package me.linus.momentum.module.modules.player;

import me.linus.momentum.module.Module;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class NoFall extends Module {
    public NoFall() {
        super("NoFall", Category.PLAYER, "Prevents fall damage");
    }

    long last = 0;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.fallDistance >= 5 && System.currentTimeMillis() - last > 100) {
            RayTraceResult result = mc.world.rayTraceBlocks(mc.player.getPositionVector(), mc.player.getPositionVector().addVector(0, -13.33f, 0), true, true, false);
            if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + mc.player.motionX, -1000.0, mc.player.posZ + mc.player.motionZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1000, mc.player.posZ, mc.player.onGround));

                last = System.currentTimeMillis();
            }
        }
    }
}

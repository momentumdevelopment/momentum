package me.linus.momentum.module.modules.player;

import me.linus.momentum.event.MomentumEvent;
import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Fancy NoRotate that bypasses Strict NCP configurations.
 *
 * @author ZimTheDestroyer & Mallyx
 * @since 12/8/20 @ 2:20 PM CST
 */

public class NoRotate  extends Module {
    public NoRotate() {
        super("NoRotate", Category.PLAYER, "Prevents the server from rotating you");
    }

    private static final Checkbox strict = new Checkbox("NCP Strict", false);

    @Override
    public void setup() {
        addSetting(strict);
    }

    @SubscribeEvent
    public void onPacketReceiveEvent(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            packet.yaw = mc.player.rotationYaw;
            packet.pitch = mc.player.rotationPitch;
            float lastYaw = packet.yaw;
            float lastPitch = packet.yaw;

            if (strict.getValue()) {
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(packet.yaw, packet.pitch, mc.player.onGround));

                for (int i = 0; i <= 3; i++) {
                    lastYaw = calculateAngle(lastYaw, mc.player.rotationYaw);
                    lastPitch = calculateAngle(lastPitch, mc.player.rotationPitch);
                    mc.player.connection.sendPacket(new CPacketPlayer.Rotation(calculateAngle(lastYaw, mc.player.rotationYaw), calculateAngle(lastPitch, mc.player.rotationPitch), mc.player.onGround));
                }

                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, mc.player.rotationPitch, mc.player.onGround));
            }
        }
    }

    public float calculateAngle(float serverValue, float currentValue) {
        return ((currentValue - serverValue)) / 4;
    }
}

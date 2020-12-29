package me.linus.momentum.module.modules.player;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;

/**
 * @author linustouchtips
 * @since 12/21/2020
 */

public class Rubberband extends Module {
    public Rubberband() {
        super("Rubberband", Category.PLAYER, "Triggers a manual rubberband");
    }

    private static final Mode mode = new Mode("Mode", "Teleport", "Jump", "Packet");
    public static Slider distance = new Slider("Distance", 0.0D, 4.0D, 20.0D, 1);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(distance);
    }

    BlockPos preRubberbandPos;

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        preRubberbandPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.getDistanceSq(preRubberbandPos) > distance.getValue()) {
            switch (mode.getValue()) {
                case 0:
                    mc.player.setPosition(mc.player.prevPosX, mc.player.prevPosY, mc.player.prevPosZ);
                    break;
                case 1:
                    mc.player.jump();
                    mc.player.jump();
                    break;
                case 2:
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.prevPosX, mc.player.prevPosY, mc.player.prevPosZ, true));
                    break;
            }
        }
    }
}

package me.linus.momentum.module.modules.player;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;
import net.minecraft.network.play.client.CPacketPlayer;

/**
 * @author linustouchtips
 * @since 12/21/2020
 */

public class Rubberband extends Module {
    public Rubberband() {
        super("Rubberband", Category.PLAYER, "Triggers a manual rubberband");
    }

    private static final Mode mode = new Mode("Mode", "Teleport", "Jump", "Packet");

    @Override
    public void setup() {
        addSetting(mode);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

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

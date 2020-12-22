package me.linus.momentum.module.modules.player;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;
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

    @Override
    public void setup() {
        addSetting(mode);
    }

    BlockPos originalPos;

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        originalPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        switch (mode.getValue()) {
            case 0:
                mc.player.setPosition(originalPos.x, originalPos.y, originalPos.z);
                break;
            case 1:
                mc.player.jump();
                mc.player.jump();
                break;
            case 2:
                mc.player.connection.sendPacket(new CPacketPlayer.Position(originalPos.x, originalPos.y, originalPos.z, true));
                break;
        }
    }
}

package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.client.notification.Notification;
import me.linus.momentum.util.client.notification.NotificationManager;
import me.linus.momentum.util.world.BlockUtil;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.player.PlayerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * @author yoink & linustouchtips
 * @since 11/29/2020
 */

public class Burrow extends Module {
    public Burrow() {
        super("Burrow", Category.COMBAT, "Rubberbands you into a block");
    }

    public static Mode mode = new Mode("Mode", "Rubberband", "Teleport", "Clip");
    public static Checkbox rotate = new Checkbox("Rotate", true);
    public static Checkbox centerPlayer = new Checkbox("Center", false);
    public static Checkbox onGround = new Checkbox("On Ground", false);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(rotate);
        addSetting(centerPlayer);
        addSetting(onGround);
    }

    BlockPos originalPos;
    Vec3d center = Vec3d.ZERO;

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        originalPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        center = PlayerUtil.getCenter(mc.player.posX, mc.player.posY, mc.player.posZ);

        if (BlockUtil.isCollidedBlocks(originalPos))
            this.disable();

        if (centerPlayer.getValue()) {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
            mc.player.connection.sendPacket(new CPacketPlayer.Position(center.x, center.y, center.z, true));
            mc.player.setPosition(center.x, center.y, center.z);
        }

        NotificationManager.addNotification(new Notification("Attempting to trigger a rubberband!", Notification.Type.Info));
        mc.player.jump();
    }

    @Override
    public void onUpdate() {
        if (mc.player.posY > originalPos.getY() + 1.2) {
            InventoryUtil.switchToSlot(Blocks.OBSIDIAN);

            if (BlockUtil.getBlockResistance(originalPos).equals(BlockUtil.blockResistance.Blank))
                BlockUtil.placeBlock(originalPos, rotate.getValue());

            if (onGround.getValue())
                mc.player.onGround = true;

            switch (mode.getValue()) {
                case 0:
                    mc.player.jump();
                    break;
                case 1:
                    mc.player.setPosition(mc.player.posX, mc.player.posY - 1, mc.player.posZ);
                    break;
                case 2:
                    mc.player.setPosition(mc.player.posX, mc.player.posY + 10, mc.player.posZ);
                    break;
            }

            this.disable();
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
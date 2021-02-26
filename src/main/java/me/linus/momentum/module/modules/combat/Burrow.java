package me.linus.momentum.module.modules.combat;

import me.linus.momentum.managers.notification.Notification;
import me.linus.momentum.managers.notification.Notification.Type;
import me.linus.momentum.managers.notification.NotificationManager;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.player.PlayerUtil;
import me.linus.momentum.util.world.BlockUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * @author yoink & linustouchtips & olliem5 && ciruu
 * @since 11/29/2020
 */

public class Burrow extends Module {
    public Burrow() {
        super("Burrow", Category.COMBAT, "Rubberbands you into a block");
    }

    public static Mode mode = new Mode("Mode", "Rubberband", "Teleport", "Clip");
    public static Checkbox rotate = new Checkbox("Rotate", true);
    public static Checkbox instant = new Checkbox("Instant", true);
    public static Checkbox centerPlayer = new Checkbox("Center", false);
    public static Checkbox silent = new Checkbox("Silent", false);
    public static Checkbox onGround = new Checkbox("On Ground", false);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(rotate);
        addSetting(instant);
        addSetting(centerPlayer);
        addSetting(silent);
        addSetting(onGround);
    }

    BlockPos originalPos;
    Vec3d center = null;
    float oldTickLength = mc.timer.tickLength;

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        oldTickLength = mc.timer.tickLength;

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

        NotificationManager.addNotification(new Notification("Attempting to trigger a rubberband!", Type.Info));

        if (silent.getValue())
            mc.timer.tickLength = 1f;

        if (instant.getValue()) {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698D, mc.player.posZ, true));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805211997D, mc.player.posZ, true));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214D, mc.player.posZ, true));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821D, mc.player.posZ, true));
        }

        else
            mc.player.jump();
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.posY > originalPos.getY() + 1.2) {
            InventoryUtil.switchToSlot(Blocks.OBSIDIAN);

            BlockUtil.placeBlock(originalPos, rotate.getValue(), false, false, false, true, false);

            mc.player.onGround = onGround.getValue();

            if (instant.getValue()) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 7, mc.player.posZ, false));
                this.disable();
                return;
            }

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
    public void onDisable() {
        mc.timer.tickLength = oldTickLength;
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
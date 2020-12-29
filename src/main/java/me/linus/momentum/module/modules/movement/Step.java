package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.world.MotionUtil;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class Step extends Module {
    public Step() {
        super("Step", Category.MOVEMENT, "Increases player step height");
    }

    private static final Mode mode = new Mode("Mode", "Normal", "Spider", "Vanilla");
    public static Slider height = new Slider("Height", 0.0D, 2.0D, 5.0D, 1);

    public static Checkbox useTimer = new Checkbox("Use Timer", false);
    private static final SubSlider timerTicks = new SubSlider(useTimer, "Timer Speed", 0.1D, 1.1D, 2.0D, 2);

    public static Checkbox pause = new Checkbox("Pause", true);
    public static SubCheckbox sneakPause = new SubCheckbox(pause, "When Sneaking", true);
    public static SubCheckbox waterPause = new SubCheckbox(pause, "When in Liquid", true);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(height);
        addSetting(useTimer);
        addSetting(pause);
    }

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        if (mode.getValue() == 2)
            mc.player.stepHeight = (float) height.getValue();
    }

    @Override
    public void onDisable() {
        mc.player.stepHeight = 0.5f;
    }

    @Override
    public void onUpdate() {
        if (mode.getValue() == 0) {
            if (nullCheck())
                return;

            if (!mc.player.collidedHorizontally)
                return;

            if (!mc.player.onGround || mc.player.isOnLadder() || (mc.player.isInWater() && waterPause.getValue()) || (mc.player.isInLava() && waterPause.getValue()) || mc.player.movementInput.jump || mc.player.noClip)
                return;

            if (MotionUtil.isMoving())
                return;

            if (mc.player.isSneaking() && sneakPause.getValue())
                return;

            double stepNormal = getCollision();
            if (stepNormal < 0.0 || stepNormal > 2.0)
                return;

            if (useTimer.getValue())
                mc.timer.tickLength = (float) (50.0f / timerTicks.getValue());

            if (stepNormal == 2.0) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.42, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.78, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.63, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.51, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.9, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.21, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.45, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.43, mc.player.posZ, mc.player.onGround));
                mc.player.setPosition(mc.player.posX, mc.player.posY + 2.0, mc.player.posZ);
            }

            if (stepNormal == 1.5) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805212, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.24918707874468, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.1707870772188, mc.player.posZ, mc.player.onGround));
                mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0, mc.player.posZ);
            }

            if (stepNormal == 1.0) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805212, mc.player.posZ, mc.player.onGround));
                mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0, mc.player.posZ);
            }

        } else if (mc.player.collidedHorizontally) {
            mc.player.motionY = 0.2;
            float baseHeight = 0.15F;

            if (mc.player.motionX < (double) -baseHeight)
                mc.player.motionX = -baseHeight;

            if (mc.player.motionX > (double) baseHeight)
                mc.player.motionX = baseHeight;

            if (mc.player.motionZ < (double) (-baseHeight))
                mc.player.motionZ = (-baseHeight);

            if (mc.player.motionZ > (double) baseHeight)
                mc.player.motionZ = baseHeight;

            mc.player.fallDistance = 0;

            if (mc.player.motionY < -0.15D)
                mc.player.motionY = (-0.15D);
        }
    }

    public double getCollision() {
        mc.player.stepHeight = 0.5f;
        double maxY = -1.0;
        AxisAlignedBB grow = mc.player.getEntityBoundingBox().offset(0.0, 0.05, 0.0).grow(0.05);
        
        if (!mc.world.getCollisionBoxes(mc.player, grow.offset(0.0, 2.0, 0.0)).isEmpty()) 
            return 100.0;

        for (final AxisAlignedBB aabb : mc.world.getCollisionBoxes(mc.player, grow)) {
            if (aabb.maxY > maxY)
                maxY = aabb.maxY;
        }

        return maxY - mc.player.posY;
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}

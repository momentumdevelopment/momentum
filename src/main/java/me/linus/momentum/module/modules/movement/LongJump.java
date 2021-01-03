package me.linus.momentum.module.modules.movement;

import me.linus.momentum.event.events.player.MoveEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.player.MotionUtil;
import me.linus.momentum.util.player.rotation.RotationUtil;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author yoink & linustouchtips
 * @since 11/30/2020
 */

// TODO: rewrite this
public class LongJump extends Module {
    public LongJump() {
        super("LongJump", Category.MOVEMENT, "Increases player jump distance");
    }

    private boolean jumped = false;
    private boolean boostable = false;
    double yVel = 0;

    private static final Mode mode = new Mode("Mode", "ByPass", "Glide", "Deer");
    public static Slider speed = new Slider("Speed", 0.0D, 4.0D, 10.0D, 0);
    private static final Checkbox packet = new Checkbox("Packet", true);
    private static final Checkbox knockback = new Checkbox("Knockback", true);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(speed);
        addSetting(packet);
        addSetting(knockback);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && knockback.getValue())
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(0, -90, true));

        if (jumped) {
            if (mc.player.onGround || mc.player.capabilities.isFlying) {
                jumped = false;

                mc.player.motionX = 0.0;
                mc.player.motionZ = 0.0;

                if (packet.getValue()) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + mc.player.motionX, 0.0, mc.player.posZ + mc.player.motionZ, mc.player.onGround));
                }

                return;
            }

            if (!(mc.player.movementInput.moveForward != 0f || mc.player.movementInput.moveStrafe != 0f))
                return;

            double yaw = getDirection();
            mc.player.motionX = -Math.sin(yaw) * (((float) Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ)) * (boostable ? (speed.getValue() * 10 / 10f) : 1f));
            mc.player.motionZ = Math.cos(yaw) * (((float) Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ)) * (boostable ? (speed.getValue() * 10 / 10f) : 1f));
            boostable = false;
        }

        if (mode.getValue() == 1) {
            if (mc.player.motionY <= 0 && !mc.player.onGround) {
                yVel -= 100 / 1.5;
                mc.player.motionY = yVel;
            }

            else
                yVel = 0;
        }

        if (mode.getValue() == 2) {
            if ((MotionUtil.isMoving()) && (mc.player.fallDistance < 1.0F)) {
                float x = (float) -Math.sin(RotationUtil.getDirection());
                float z = (float) Math.cos(RotationUtil.getDirection());

                if (mc.player.collidedVertically && mc.gameSettings.keyBindJump.isPressed()) {
                    mc.player.motionX = (x * 0.29F);
                    mc.player.motionZ = (z * 0.29F);
                }

                if (mc.player.motionY == 0.33319999363422365D) {
                    mc.player.motionX = (x * 1.261D);
                    mc.player.motionZ = (z * 1.261D);
                }
            }
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (nullCheck())
            return;

        if (!(mc.player.movementInput.moveForward != 0f || mc.player.movementInput.moveStrafe != 0f) && jumped) {
            mc.player.motionX = 0.0;
            mc.player.motionZ = 0.0;
            event.setX(0);
            event.setY(0);
        }
    }


    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event) {
        if ((mc.player != null && mc.world != null) && event.getEntity() == mc.player && (mc.player.movementInput.moveForward != 0f || mc.player.movementInput.moveStrafe != 0f)) {
            jumped = true;
            boostable = true;
        }
    }

    private double getDirection() {
        float rotationYaw = mc.player.rotationYaw;

        if (mc.player.moveForward < 0f)
            rotationYaw += 180f;

        float forward = 1f;

        if (mc.player.moveForward < 0f)
            forward = -0.5f;

        else if (mc.player.moveForward > 0f)
            forward = 0.5f;

        if (mc.player.moveStrafing > 0f)
            rotationYaw -= 90f * forward;

        if (mc.player.moveStrafing < 0f)
            rotationYaw += 90f * forward;

        return Math.toRadians(rotationYaw);
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}

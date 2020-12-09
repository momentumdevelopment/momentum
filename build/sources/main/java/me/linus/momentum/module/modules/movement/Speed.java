package me.linus.momentum.module.modules.movement;

import me.linus.momentum.event.events.player.MoveEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.Timer;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.init.MobEffects;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class Speed extends Module {
    public Speed() {
        super("Speed", Category.MOVEMENT, "Allows you to go faster");
    }

    private static Mode mode = new Mode("Mode", "Strafe", "OnGround", "SmoothHop", "LowHop");
    private static SubCheckbox strict = new SubCheckbox(mode, "Strict", false);

    public static Slider multiplier = new Slider("Multiplier", 0.0D, 0.1D, 0.3D, 3);

    private static Checkbox useTimer = new Checkbox("Use Timer", false);
    public static SubSlider timerTicks = new SubSlider(useTimer, "Timer Speed", 0.0D, 1.12D, 2.0D, 2);

    private static Checkbox overRide = new Checkbox("Override", false);
    public static SubSlider speed = new SubSlider(overRide, "Speed", 0.0D, 0.61D, 1.0D, 2);

    private static Checkbox jump = new Checkbox("Jump", true);
    public static SubSlider jumpDelay = new SubSlider(jump, "Jump Delay", 0.0D, 300.0D, 500.0D, 0);

    private static Checkbox disable = new Checkbox("Disable", true);
    private static SubCheckbox inWater = new SubCheckbox(disable, "Disable in Liquid", true);
    private static SubCheckbox rubberband = new SubCheckbox(disable, "Disable on Rubberband", false);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(multiplier);
        addSetting(useTimer);
        addSetting(overRide);
        addSetting(jump);
        addSetting(disable);
    }
    
    double newSpeed;
    boolean jumped;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if ((mc.player.isInWater() || mc.player.isInLava() && inWater.getValue())) {
            this.disable();
            return;
        }

        if (useTimer.getValue())
            mc.timer.tickLength = (float) (50.0f / timerTicks.getValue());

        if (mc.player.onGround && jump.getValue() && PlayerUtil.isMoving())
            mc.player.jump();
    }

    @Override
    public void onDisable() {
        mc.timer.tickLength = 50;
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (nullCheck())
            return;

        if (PlayerUtil.constrainedCheck())
            return;

        double[] baseSpeed = PlayerUtil.baseMotion(mc.player);

        mc.player.setSprinting(true);

        newSpeed = (Math.sqrt(event.getX() * event.getX() + event.getZ() * event.getZ()) > 0.2873d ? Math.sqrt(event.getX() * event.getX() + event.getZ() * event.getZ()) + (Math.sqrt(event.getX() * event.getX() + event.getZ() * event.getZ()) >= 0.34D ? speed.getValue() / 1000d : 0.0d) : 0.2873d);

        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            final int amplifier = mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();

            newSpeed *= (1.0 + 0.2 * (amplifier + 1));
        }

        if (baseSpeed[2] == 0.0 && baseSpeed[3] == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        }

        else {
            if (mode.getValue() == 1) {
                if (mc.gameSettings.keyBindJump.isKeyDown() && mc.player.onGround)
                    jumped = true;

            } else if (mode.getValue() == 0) {
                mc.player.setSprinting(true);
            }

                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    jumped = true;
                }
            }

            if (jumped) {
                double jump = 0.40123128;

                if (mode.getValue() == 3)
                    jump = 0.20061564;

                if (mc.player.onGround) {
                    if (mode.getValue() == 2)
                        newSpeed = 0.6174077;

                    if (overRide.getValue())
                        newSpeed = speed.getValue();

                    if (mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                        jump += ((mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f);
                    }

                    event.setY(mc.player.motionY = jump);
                }

                jumped = false;
            }

            event.setX((baseSpeed[2] * newSpeed) * Math.cos(Math.toRadians((baseSpeed[0] + 90.0f))) + (baseSpeed[3] * newSpeed) * Math.sin(Math.toRadians((baseSpeed[0] + 90.0f))));
            event.setZ((baseSpeed[2] * newSpeed) * Math.sin(Math.toRadians((baseSpeed[0] + 90.0f))) - (baseSpeed[3] * newSpeed) * Math.cos(Math.toRadians((baseSpeed[0] + 90.0f))));
        }

    @Override
    public String getHUDData() {
        return mode.getMode(mode.getValue());
    }
}




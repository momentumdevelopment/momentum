package me.linus.momentum.module.modules.player;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.client.system.Timer;

/**
 * @author linustouchtips
 * @since 12/19/2020
 */

// TODO: spoof rotation system, this is actually so retarded rn
public class AntiAim extends Module {
    public AntiAim() {
        super("AntiAim", Category.PLAYER, "Makes you a harder target");
    }

    private static final Mode mode = new Mode("Mode", "Spin", "HeadSpin");
    public static Slider speed = new Slider("Speed", 0.0D, 20.0D, 50.0D, 1);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(speed);
    }

    Timer spinTimer = new Timer();

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        switch (mode.getValue()) {
            case 0:
                spinPlayer();
                break;
            case 1:
                spinHead();
                break;
        }
    }

    public void spinPlayer() {
        if (spinTimer.passed((long) speed.getValue())) {
            mc.player.rotationYaw += 5;
        }

        spinTimer.reset();
    }

    public void spinHead() {
        if (spinTimer.passed((long) speed.getValue())) {
            mc.player.rotationYawHead += 5;
        }

        spinTimer.reset();
    }
}

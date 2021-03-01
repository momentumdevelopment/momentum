package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.world.HoleUtil;
import net.minecraft.util.math.BlockPos;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class Anchor extends Module {
    public Anchor() {
        super("Anchor", Category.MOVEMENT, "Stops all movement above a hole");
    }

    public static Slider height = new Slider("Height", 1.0D, 3.0D, 5.0D, 0);
    public static Slider pitch = new Slider("Pitch", 0.0D, 75.0D, 90.0D, 0);

    public static Checkbox pull = new Checkbox("Pull", false);
    public static SubSlider speed = new SubSlider(pull, "Pull Speed", 0.0D, 3.0D, 10.0D, 1);

    @Override
    public void setup() {
        addSetting(height);
        addSetting(pitch);
        addSetting(pull);
    }

    boolean anchored;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.rotationPitch <= 90 - pitch.getValue())
            return;

        for (int i = 0; i < mc.player.posY - height.getValue(); i++) {
            BlockPos belowPos = new BlockPos(mc.player.posX, i, mc.player.posZ);

            if (HoleUtil.isHole(belowPos))
                anchored = true;
        }

        if (anchored) {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;

            if (pull.getValue())
                mc.player.motionY -= speed.getValue();
        }
    }
}

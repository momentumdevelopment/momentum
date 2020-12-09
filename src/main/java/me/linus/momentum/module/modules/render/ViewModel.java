package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.slider.Slider;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class ViewModel extends Module {
    public ViewModel() {
        super("ViewModel", Category.RENDER, "Changes the arm position");
    }

    public static Slider itemFOV = new Slider("Item FOV", 0.0D, 120.0D, 250.0D, 0);
    public static Slider armPitch = new Slider("Arm Pitch", 0.0D, 90.0D, 360.0D, 0);
    public static Slider armYaw = new Slider("Arm Yaw", 0.0D, 220.0D, 360.0D, 0);

    @Override
    public void setup() {
        addSetting(itemFOV);
        addSetting(armPitch);
        addSetting(armYaw);
    }

    public void onUpdate() {
        if (nullCheck())
            return;

        mc.player.renderArmPitch = (float) armPitch.getValue();
        mc.player.renderArmYaw = (float) armYaw.getValue();
    }

    @SubscribeEvent
    public void FOVEvent(EntityViewRenderEvent.FOVModifier event) {
        event.setFOV((float) itemFOV.getValue());
    }
}

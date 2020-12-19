package me.linus.momentum.module.modules.render;

import me.linus.momentum.event.events.render.Render3DEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.render.RenderUtil;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class BreakESP extends Module {
    public BreakESP() {
        super("BreakESP", Category.RENDER, "Highlights blocks being broken");
    }

    public static Checkbox color = new Checkbox("Color", true);
    public static SubSlider r = new SubSlider(color, "Red", 0.0D, 250.0D, 255.0D, 0);
    public static SubSlider g = new SubSlider(color, "Green", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider b = new SubSlider(color, "Blue", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider a = new SubSlider(color, "Alpha", 0.0D, 30.0D, 255.0D, 0);

    public static Checkbox showDamage = new Checkbox("Show Damage", true);
    public static Slider range = new Slider("Range", 0.0D, 7.0D, 10.0D, 0);

    @Override
    public void setup() {
        addSetting(color);
        addSetting(range);
    }

    @Override
    public void onRender3D(Render3DEvent eventRender) {
        mc.renderGlobal.damagedBlocks.forEach((integer, destroyBlockProgress) -> {
            if (destroyBlockProgress != null && destroyBlockProgress.getPosition().getDistance((int) mc.player.posX,(int)  mc.player.posY,(int)  mc.player.posZ) <= range.getValue()) {
                RenderUtil.drawVanillaBoxFromBlockPos(destroyBlockProgress.getPosition(), (float) r.getValue() / 255f, (float) g.getValue() / 255f, (float) b.getValue() / 255f, (float) a.getValue() / 255f);

                if (showDamage.getValue())
                    RenderUtil.drawNametagFromBlockPos(destroyBlockProgress.getPosition(), mc.world.getEntityByID(integer).getName() + " " + (destroyBlockProgress.getPartialBlockDamage() * 10) + "%");
            }
        });
    }
}

package me.linus.momentum.module.modules.render;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.system.Timer;
import me.linus.momentum.util.render.RenderUtil;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class CrossHairs extends Module {
    public CrossHairs() {
        super("CrossHairs", Category.RENDER, "Displays hitmarkers");
    }

    public static Checkbox color = new Checkbox("Color", true);
    public static SubSlider r = new SubSlider(color, "Red", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider g = new SubSlider(color, "Green", 0.0D, 255.0D, 255.0D, 0);
    public static SubSlider b = new SubSlider(color, "Blue", 0.0D, 0.0D, 255.0D, 0);

    @Override
    public void setup() {
        addSetting(color);
    }

    Timer hitTimer = new Timer();

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketUseEntity && ((CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK)
            hitTimer.reset();
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (hitTimer.toReach(30))
            RenderUtil.drawHitMarkers(new Color((int) r.getValue(),(int) g.getValue(),(int)  b.getValue()));
    }
}

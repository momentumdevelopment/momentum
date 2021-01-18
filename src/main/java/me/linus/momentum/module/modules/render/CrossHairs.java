package me.linus.momentum.module.modules.render;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.color.SubColor;
import me.linus.momentum.util.render.Render2DUtil;
import me.linus.momentum.util.world.Timer;
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
    public static SubColor colorPicker = new SubColor(color, new Color(0, 255,  0, 255));

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
        if (hitTimer.reach(300, Timer.Format.System))
            Render2DUtil.drawHitMarkers(colorPicker.getColor());
    }
}
package me.linus.momentum.gui.hud.components;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.gui.hud.AnchorPoint;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.main.hud.HUD;
import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.module.modules.client.HUDEditor;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.world.Timer;
import me.linus.momentum.util.render.FontUtil;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;

/**
 * @author linustouchtips
 * @since 12/24/2020
 */

public class LagNotifier extends HUDComponent {
    public LagNotifier() {
        super("LagNotifier", 300, 2, AnchorPoint.None);

        width = (int) FontUtil.getStringWidth("Server has stopped responding for X.X seconds!");
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static Slider threshHold = new Slider("Threshold", 0.0D, 3.0D, 10.0D, 0);

    @Override
    public void setup() {
        addSetting(threshHold);
    }

    Timer lagTimer = new Timer();

    @Override
    public void renderComponent() {
        float seconds = ((System.currentTimeMillis() - lagTimer.time) / 1000.0f) % 60.0f;

        if (mc.currentScreen instanceof HUD)
            FontUtil.drawString("Server has stopped responding for X seconds!", this.x, this.y, HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : -1);

        if (seconds < threshHold.getValue())
            return;

        if (!(mc.currentScreen instanceof HUD))
            FontUtil.drawString("Server has stopped responding for " + new DecimalFormat("#.#").format(seconds) + " seconds!", this.x, this.y, -1);
    }

    @SubscribeEvent
    public void onPacketRecieve(PacketReceiveEvent event) {
        lagTimer.reset();
    }
}
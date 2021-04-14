package me.linus.momentum.gui.hud.element.elements;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.gui.hud.element.AnchorPoint;
import me.linus.momentum.gui.main.HUDScreen;
import me.linus.momentum.util.client.color.ThemeColor;
import me.linus.momentum.module.modules.client.HUD;
import me.linus.momentum.gui.hud.element.HUDElement;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.world.Timer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;

/**
 * @author linustouchtips
 * @since 12/24/2020
 */

public class LagNotifier extends HUDElement {
    public LagNotifier() {
        super("LagNotifier", 300, 2, Category.MISC, AnchorPoint.None);
    }

    public static Slider threshHold = new Slider("Threshold", 0.0D, 3.0D, 10.0D, 0);

    @Override
    public void setup() {
        addSetting(threshHold);
    }

    Timer lagTimer = new Timer();

    @Override
    public void renderElement() {
        float seconds = ((System.currentTimeMillis() - lagTimer.time) / 1000.0f) % 60.0f;
        width = (int) FontUtil.getStringWidth("Server has stopped responding for X.X seconds!");

        if (mc.currentScreen instanceof HUDScreen)
            FontUtil.drawString("Server has stopped responding for X seconds!", this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : -1);

        if (seconds < threshHold.getValue())
            return;

        if (!(mc.currentScreen instanceof HUDScreen))
            FontUtil.drawString("Server has stopped responding for " + new DecimalFormat("#.#").format(seconds) + " seconds!", this.x, this.y, -1);
    }

    @SubscribeEvent
    public void onPacketRecieve(PacketReceiveEvent event) {
        lagTimer.reset();
    }
}
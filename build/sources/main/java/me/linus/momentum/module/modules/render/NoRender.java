package me.linus.momentum.module.modules.render;

import me.linus.momentum.event.events.world.BossBarEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class NoRender extends Module {
    public NoRender() {
        super("NoRender", Category.RENDER, "Prevents certain things from rendering");
    }

    public static Checkbox hurtCamera = new Checkbox("Hurt Camera", true);
    private static Checkbox fire = new Checkbox("Fire", true);
    public static Checkbox armor = new Checkbox("Armor", false);
    private static Checkbox bossBar = new Checkbox("Boss Bars", true);

    @Override
    public void setup() {
        addSetting(hurtCamera);
        addSetting(fire);
        addSetting(armor);
        addSetting(bossBar);
    }

    @SubscribeEvent
    public void onRenderBlockOverlay (RenderBlockOverlayEvent event) {
        if (fire.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE)
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderBlockOverlayEvent event) {
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onRenderBossBar(BossBarEvent event) {
        if (bossBar.getValue())
            event.setCanceled(true);
    }
}

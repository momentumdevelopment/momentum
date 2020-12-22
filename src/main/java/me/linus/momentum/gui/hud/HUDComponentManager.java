package me.linus.momentum.gui.hud;

import com.google.common.collect.Lists;
import me.linus.momentum.gui.hud.components.*;
import me.linus.momentum.gui.main.GUI;
import me.linus.momentum.gui.main.HUD;
import me.linus.momentum.mixin.MixinInterface;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class HUDComponentManager implements MixinInterface {
    private final List<HUDComponent> components;

    public HUDComponentManager() {
        components = Lists.newArrayList(
            new WaterMark(),
            new ActiveModules(),
            new FPS(),
            new Welcomer(),
            new Inventory(),
            new PlayerViewer(),
            new Time(),
            new Ping(),
            new Armor(),
            new Crystal(),
            new Totem(),
            new Server(),
            new TargetHUD(),
            new Coordinates(),
            new TPS(),
            new Speed(),
            new Direction(),
            new Notifications()
        );

        MinecraftForge.EVENT_BUS.register(this);
    }

    public List<HUDComponent> getComponents(){
        return components;
    }

    public HUDComponent getComponentByName(String name) {
        for (HUDComponent component : components) {
            if (component.getName().equalsIgnoreCase(name))
                return component;
        }

        return null;
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Text event) {
        if (!(mc.currentScreen instanceof HUD) && !(mc.currentScreen instanceof GUI)) {
            for (HUDComponent comp : components) {
                if (comp.isEnabled()) {
                    // TODO: figure out why some of these get npe's

                    try {
                        comp.render();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

package me.linus.momentum.gui.hud;

import com.google.common.collect.Lists;
import me.linus.momentum.gui.hud.components.*;
import me.linus.momentum.gui.main.gui.GUI;
import me.linus.momentum.gui.main.hud.HUD;
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
    public HUDComponentManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static final List<HUDComponent> components = Lists.newArrayList(
            new ActiveModules(),
            new Armor(),
            new Coordinates(),
            new Crystal(),
            new Direction(),
            new FPS(),
            new Inventory(),
            new Notifications(),
            new Ping(),
            new PlayerViewer(),
            new Server(),
            new Speed(),
            new TargetHUD(),
            new Time(),
            new Totem(),
            new TPS(),
            new WaterMark(),
            new Welcomer()
    );

    public static List<HUDComponent> getComponents(){
        return components;
    }

    public static HUDComponent getComponentByName(String name) {
        for (HUDComponent component : components) {
            if (component.getName().equalsIgnoreCase(name))
                return component;
        }

        return null;
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Text event) {
        if (!(mc.currentScreen instanceof HUD) && !(mc.currentScreen instanceof GUI)) {
            for (HUDComponent component : components) {
                if (component.isEnabled()) {
                    // TODO: figure out why some of these get npe's

                    try {
                        component.renderComponent();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
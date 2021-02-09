package me.linus.momentum.managers;

import com.google.common.collect.Lists;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.hud.components.*;
import me.linus.momentum.gui.main.console.Console;
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

    static final List<HUDComponent> components = Lists.newArrayList(
            new ActiveModules(),
            new Armor(),
            new CombatInfo(),
            new Coordinates(),
            new Crystal(),
            new Direction(),
            new FPS(),
            new Inventory(),
            new LagNotifier(),
            new Notifications(),
            new Ping(),
            new PlayerViewer(),
            new PotionEffects(),
            new Server(),
            new Speed(),
            new TabGUI(),
            new TargetHUD(),
            new TextRadar(),
            new Time(),
            new Totem(),
            new TPS(),
            new WaterMark(),
            new Welcomer(),
            new XP()
    );

    public static List<HUDComponent> getComponents(){
        return components;
    }

    public static HUDComponent getComponentByName(String name) {
        return components.stream().filter(component -> component.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static HUDComponent getComponentByClass(Class<?> clazz) {
        return components.stream().filter(component -> component.getClass().equals(clazz)).findFirst().orElse(null);
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Text event) {
        if (!(mc.currentScreen instanceof HUD) && !(mc.currentScreen instanceof GUI) && !(mc.currentScreen instanceof Console)) {
            for (HUDComponent component : components) {
                if (component.isEnabled()) {
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
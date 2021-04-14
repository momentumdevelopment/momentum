package me.linus.momentum.managers;

import com.google.common.collect.Lists;
import me.linus.momentum.gui.hud.element.HUDElement;
import me.linus.momentum.gui.hud.element.elements.*;
import me.linus.momentum.gui.main.GUIScreen;
import me.linus.momentum.gui.main.HUDScreen;
import me.linus.momentum.gui.main.WindowScreen;
import me.linus.momentum.mixin.MixinInterface;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class HUDElementManager implements MixinInterface {
    public HUDElementManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    static List<HUDElement> components = Lists.newArrayList(
            new ActiveModules(),
            new Armor(),
            new CombatInfo(),
            new Coordinates(),
            new Crystal(),
            new Direction(),
            new EXP(),
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
            new TextRadar(),
            new Time(),
            new Totem(),
            new TPS(),
            new WaterMark(),
            new Welcomer()
    );

    public static List<HUDElement> getComponents(){
        return components;
    }

    public static HUDElement getComponentByName(String name) {
        return components.stream().filter(component -> component.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static HUDElement getComponentByClass(Class<?> clazz) {
        return components.stream().filter(component -> component.getClass().equals(clazz)).findFirst().orElse(null);
    }

    public static List<HUDElement> getElementsInCategory(HUDElement.Category cat) {
        List<HUDElement> module = new ArrayList<>();

        for (HUDElement m : components) {
            if (m.category.equals(cat))
                module.add(m);
        }

        return module;
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Text event) {
        if (!(mc.currentScreen instanceof HUDScreen) && !(mc.currentScreen instanceof GUIScreen) && !(mc.currentScreen instanceof WindowScreen)) {
            for (HUDElement component : components) {
                if (component.isDrawn()) {
                    try {
                        component.renderElement();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
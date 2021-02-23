package me.linus.momentum.managers;

import java.util.List;
import java.awt.*;
import java.util.HashMap;

/**
 * @author linustouchtips
 * @since 02/21/2021
 */

public class ColorManager {

    public HashMap<Class<?>, Color> abstractColorRegistry = new HashMap<>();
    public HashMap<String, Color> colorRegistry = new HashMap<>();

    public void registerAbstractColor(Class<?> clazz, Color color) {
        abstractColorRegistry.put(clazz, color);
    }

    public void registerAbstractColorList(List<Class<?>> clazz, Color color) {
        for (int i = 0; i < clazz.size(); i++) {
            abstractColorRegistry.put(clazz.get(i), color);
        }
    }

    public void registerColor(String identifier, Color color) {
        colorRegistry.put(identifier, color);
    }

    public void registerColorList(List<String> identifiers, Color color) {
        for (int i = 0; i < identifiers.size(); i++) {
            colorRegistry.put(identifiers.get(i), color);
        }
    }
}

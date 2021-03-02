package me.linus.momentum.managers;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

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
        for (Class<?> aClass : clazz) {
            abstractColorRegistry.put(aClass, color);
        }
    }

    public void registerColor(String identifier, Color color) {
        colorRegistry.put(identifier, color);
    }

    public void registerColorList(List<String> identifiers, Color color) {
        for (String identifier : identifiers) {
            colorRegistry.put(identifier, color);
        }
    }
}

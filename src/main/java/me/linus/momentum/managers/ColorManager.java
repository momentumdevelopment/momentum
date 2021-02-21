package me.linus.momentum.managers;

import java.util.List;
import java.awt.*;
import java.util.HashMap;

public class ColorManager {

    public HashMap<Class<?>, Color> colorRegistry = new HashMap<>();

    public void registerColor(Class<?> clazz, Color color) {
        colorRegistry.put(clazz, color);
    }

    public void registerColorList(List<Class<?>> clazz, Color color) {
        for (int i = 0; i < clazz.size(); i++) {
            colorRegistry.put(clazz.get(i), color);
        }
    }
}

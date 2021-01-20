package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.module.modules.render.esp.ESPMode;
import me.linus.momentum.module.modules.render.esp.modes.*;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 11/26/2020
 * @updated 01/06/2021
 */

public class ESP extends Module {
    public ESP() {
        super("ESP", Category.RENDER, "Highlights entities");
    }

    public static Mode mode = new Mode("Mode", "Outline", "Glow", "2D", "Wire-Frame", "CS:GO", "Normal", "Textured", "Box");

    public static Checkbox players = new Checkbox("Players", true);
    public static ColorPicker playerPicker = new ColorPicker(players, new Color(215, 46, 46));

    public static Checkbox animals = new Checkbox("Animals", true);
    public static ColorPicker animalPicker = new ColorPicker(animals, new Color(0, 200, 0));

    public static Checkbox mobs = new Checkbox("Mobs", true);
    public static ColorPicker mobsPicker = new ColorPicker(mobs, new Color(131, 19, 199));

    public static Checkbox items = new Checkbox("Items", true);
    public static ColorPicker itemsPicker = new ColorPicker(items, new Color(199, 196, 19));

    public static Checkbox vehicles = new Checkbox("Vehicles", true);
    public static ColorPicker vehiclePicker = new ColorPicker(vehicles, new Color(199, 103, 19));

    public static Checkbox crystals = new Checkbox("Crystals", true);
    public static ColorPicker crystalPicker = new ColorPicker(crystals, new Color(199, 19, 139));

    public static Slider lineWidth = new Slider("Line Width", 0.0D, 2.5D, 5.0D, 1);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(players);
        addSetting(animals);
        addSetting(mobs);
        addSetting(vehicles);
        addSetting(crystals);
        addSetting(lineWidth);
    }

    public static ESPMode espMode;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        mc.gameSettings.fancyGraphics = true;

        switch (mode.getValue()) {
            case 0:
                espMode = new Outline();
                break;
            case 1:
                espMode = new Glow();
                break;
            case 2:
                espMode = new TwoD();
                break;
            case 3:
                espMode = new WireFrame();
                break;
            case 4:
                espMode = new CSGO();
                break;
            case 5:
                espMode = new Normal();
                break;
            case 6:
                espMode = new Textured();
                break;
            case 7:
                espMode = new Box();
                break;
        }
    }

    @Override
    public void onDisable() {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity.isGlowing())
                entity.setGlowing(false);
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (nullCheck())
            return;

        try {
            espMode.drawESP();
        } catch (Exception e) {

        }

        for (Entity entity : mc.world.loadedEntityList) {
            if (entity.isGlowing() && !(mode.getValue() == 1))
                entity.setGlowing(false);
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
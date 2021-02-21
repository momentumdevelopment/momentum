package me.linus.momentum.module.modules.render;

import me.linus.momentum.managers.ColorManager;
import me.linus.momentum.module.Module;
import me.linus.momentum.module.modules.render.esp.ESPMode;
import me.linus.momentum.module.modules.render.esp.modes.*;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
    public static ColorPicker playerPicker = new ColorPicker(players, "Player Picker", new Color(215, 46, 46));

    public static Checkbox animals = new Checkbox("Animals", true);
    public static ColorPicker animalPicker = new ColorPicker(animals, "Animal Picker", new Color(0, 200, 0));

    public static Checkbox mobs = new Checkbox("Mobs", true);
    public static ColorPicker mobsPicker = new ColorPicker(mobs, "Mob Picker", new Color(131, 19, 199));

    public static Checkbox items = new Checkbox("Items", true);
    public static ColorPicker itemsPicker = new ColorPicker(items, "Item Picker", new Color(199, 196, 19));

    public static Checkbox vehicles = new Checkbox("Vehicles", true);
    public static ColorPicker vehiclePicker = new ColorPicker(vehicles, "Vehicle Picker", new Color(199, 103, 19));

    public static Checkbox crystals = new Checkbox("Crystals", true);
    public static ColorPicker crystalPicker = new ColorPicker(crystals, "Crystal Picker", new Color(199, 19, 139, 30));

    public static Slider lineWidth = new Slider("Line Width", 0.0D, 2.5D, 5.0D, 1);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(players);
        addSetting(animals);
        addSetting(mobs);
        addSetting(items);
        addSetting(vehicles);
        addSetting(crystals);
        addSetting(lineWidth);
    }

    public static ESPMode espMode;
    public static ColorManager colorManager = new ColorManager();

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        mc.gameSettings.fancyGraphics = true;

        colorManager.registerColor(EntityOtherPlayerMP.class, playerPicker.getColor());
        colorManager.registerColorList(EntityUtil.getPassives(), animalPicker.getColor());
        colorManager.registerColorList(EntityUtil.getHostiles(), mobsPicker.getColor());
        colorManager.registerColor(EntityItem.class, itemsPicker.getColor());
        colorManager.registerColorList(EntityUtil.getVehicles(), vehiclePicker.getColor());
        colorManager.registerColor(EntityEnderCrystal.class, crystalPicker.getColor());

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

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (nullCheck())
            return;

        try {
            espMode.drawESP();
        } catch (Exception e) {

        }

        for (Entity entity : mc.world.loadedEntityList) {
            if (entity.isGlowing() && (mode.getValue() != 1 || !this.isEnabled()))
                entity.setGlowing(false);
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
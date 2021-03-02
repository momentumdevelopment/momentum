package me.linus.momentum.module.modules.render;

import me.linus.momentum.managers.ColorManager;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.render.builder.RenderBuilder.RenderMode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/23/2020
 */

public class StorageESP extends Module {
    public StorageESP() {
        super("StorageESP", Category.RENDER, "Highlights containers");
    }

    public static Mode mode = new Mode("Mode", "Claw", "Outline", "Fill", "Both");

    public static Checkbox chests = new Checkbox("Chests", true);
    public static ColorPicker chestPicker = new ColorPicker(chests, "Chest Picker", new Color(46, 83, 215));

    public static Checkbox enderChests = new Checkbox("Ender Chests", true);
    public static ColorPicker enderPicker = new ColorPicker(enderChests, "Ender Chest Picker", new Color(156, 46, 215));

    public static Checkbox shulkers = new Checkbox("Shulkers", true);
    public static ColorPicker shulkerPicker = new ColorPicker(shulkers, "Shulker Picker", new Color(215, 46, 198));

    public static Checkbox hoppers = new Checkbox("Hoppers", true);
    public static ColorPicker hopperPicker = new ColorPicker(hoppers, "Hopper Picker", new Color(106, 106, 114));

    public static Checkbox droppers = new Checkbox("Droppers", true);
    public static ColorPicker dropperPicker = new ColorPicker(droppers, "Dropper Picker", new Color(106, 106, 114));

    public static Checkbox furnaces = new Checkbox("Furnaces", true);
    public static ColorPicker furnacePicker = new ColorPicker(furnaces, "Furnace Picker", new Color(106, 106, 114));

    public static Checkbox beds = new Checkbox("Beds", true);
    public static ColorPicker bedPicker = new ColorPicker(beds, "Bed Picker", new Color(208, 40, 60));

    public static Slider range = new Slider("Range", 0.0D, 30.0D, 100.0D, 0);
    public static Slider lineWidth = new Slider("Line Width", 0.0D, 2.5D, 4.0D, 1);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(chests);
        addSetting(enderChests);
        addSetting(shulkers);
        addSetting(hoppers);
        addSetting(droppers);
        addSetting(furnaces);
        addSetting(beds);
        addSetting(range);
        addSetting(lineWidth);
    }

    ColorManager colorManager = new ColorManager();

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        colorManager.registerAbstractColor(TileEntityChest.class, chestPicker.getColor());
        colorManager.registerAbstractColor(TileEntityEnderChest.class, enderPicker.getColor());
        colorManager.registerAbstractColor(TileEntityShulkerBox.class, shulkerPicker.getColor());
        colorManager.registerAbstractColor(TileEntityHopper.class, hopperPicker.getColor());
        colorManager.registerAbstractColor(TileEntityDropper.class, dropperPicker.getColor());
        colorManager.registerAbstractColor(TileEntityFurnace.class, furnacePicker.getColor());
        colorManager.registerAbstractColor(TileEntityBed.class, bedPicker.getColor());
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (nullCheck())
            return;

        renderTileEntities();
    }

    public void renderTileEntities() {
        mc.world.loadedTileEntityList.stream().filter(tileEntity -> mc.player.getDistanceSq(tileEntity.getPos()) <= Math.pow(range.getValue(), 2)).forEach(tileEntity -> {
            if (colorManager.abstractColorRegistry.containsKey(tileEntity)) {
                switch (mode.getValue()) {
                    case 0:
                        GlStateManager.glLineWidth((float) lineWidth.getValue());
                        RenderUtil.drawBoxBlockPos(tileEntity.getPos(), 0, colorManager.abstractColorRegistry.get(tileEntity), RenderMode.Claw);
                        break;
                    case 1:
                        GlStateManager.glLineWidth((float) lineWidth.getValue());
                        RenderUtil.drawBoxBlockPos(tileEntity.getPos(), 0, colorManager.abstractColorRegistry.get(tileEntity), RenderMode.Outline);
                        break;
                    case 2:
                        RenderUtil.drawBoxBlockPos(tileEntity.getPos(), 0, colorManager.abstractColorRegistry.get(tileEntity), RenderMode.Fill);
                        break;
                    case 3:
                        GlStateManager.glLineWidth((float) lineWidth.getValue());
                        RenderUtil.drawBoxBlockPos(tileEntity.getPos(), 0, colorManager.abstractColorRegistry.get(tileEntity), RenderMode.Both);
                        break;
                }
            }
        });
    }
}
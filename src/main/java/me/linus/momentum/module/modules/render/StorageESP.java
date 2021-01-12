package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.color.SubColor;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.client.ColorUtil;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.render.RenderUtil;
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

    public static Mode mode = new Mode("Mode", "Outline", "Fill", "Both");

    public static final Checkbox chests = new Checkbox("Chests", true);
    public static SubColor chestPicker = new SubColor(chests, new Color(46, 83, 215));

    public static final Checkbox enderChests = new Checkbox("Ender Chests", true);
    public static SubColor enderPicker = new SubColor(enderChests, new Color(156, 46, 215));

    public static final Checkbox shulkers = new Checkbox("Shulkers", true);
    public static SubColor shulkerPicker = new SubColor(shulkers, new Color(215, 46, 198));

    public static final Checkbox hoppers = new Checkbox("Hoppers", true);
    public static SubColor hopperPicker = new SubColor(hoppers, new Color(106, 106, 114));

    public static final Checkbox droppers = new Checkbox("Droppers", true);
    public static SubColor dropperPicker = new SubColor(droppers, new Color(106, 106, 114));

    public static final Checkbox furnaces = new Checkbox("Furnaces", true);
    public static SubColor furnacePicker = new SubColor(furnaces, new Color(106, 106, 114));

    public static final Checkbox beds = new Checkbox("Beds", true);
    public static SubColor bedPicker = new SubColor(beds, new Color(208, 40, 60));

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

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (nullCheck())
            return;

        renderTileEntities();
    }

    public void renderTileEntities() {
        mc.world.loadedTileEntityList.stream().filter(tileEntity -> mc.player.getDistanceSq(tileEntity.getPos()) <= MathUtil.square(range.getValue())).forEach(tileEntity -> {
            if (tileEntity instanceof TileEntityChest && chests.getValue() || (tileEntity instanceof TileEntityEnderChest && enderChests.getValue()) || (tileEntity instanceof TileEntityHopper && hoppers.getValue()) || (tileEntity instanceof TileEntityFurnace && furnaces.getValue()) || (tileEntity instanceof TileEntityBed && beds.getValue() || (tileEntity instanceof TileEntityDropper && droppers.getValue()) || (tileEntity instanceof TileEntityShulkerBox && shulkers.getValue()))) {
                switch (mode.getValue()) {
                    case 0:
                        GlStateManager.glLineWidth((float) lineWidth.getValue());
                        RenderUtil.drawBoundingBoxBlockPos(tileEntity.getPos(), 0, ColorUtil.getStorageColor(tileEntity));
                        break;
                    case 1:
                        RenderUtil.drawBoxBlockPos(tileEntity.getPos(), 0, ColorUtil.getStorageColor(tileEntity));
                        break;
                    case 2:
                        GlStateManager.glLineWidth((float) lineWidth.getValue());
                        RenderUtil.drawBoundingBoxBlockPos(tileEntity.getPos(), 0, ColorUtil.getStorageColor(tileEntity));
                        RenderUtil.drawBoxBlockPos(tileEntity.getPos(), 0, ColorUtil.getStorageColor(tileEntity));
                        break;
                }
            }
        });
    }
}

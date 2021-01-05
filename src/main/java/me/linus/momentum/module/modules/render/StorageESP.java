package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.client.ColorUtil;
import me.linus.momentum.util.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 12/23/2020
 */

public class StorageESP extends Module {
    public StorageESP() {
        super("StorageESP", Category.RENDER, "Highlights containers");
    }

    public static Mode mode = new Mode("Mode", "Outline", "Fill", "Both");
    private static final Checkbox chests = new Checkbox("Chests", true);
    private static final Checkbox enderChests = new Checkbox("Ender Chests", true);
    private static final Checkbox shulkers = new Checkbox("Shulkers", true);
    private static final Checkbox hoppers = new Checkbox("Hoppers", true);
    private static final Checkbox droppers = new Checkbox("Droppers", true);
    private static final Checkbox furnaces = new Checkbox("Furnaces", true);
    public static final Checkbox beds = new Checkbox("Beds", true);

    public static Slider range = new Slider("Range", 0.0D, 30.0D, 100.0D, 0);
    public static Slider lineWidth = new Slider("Line Width", 0.0D, 2.5D, 4.0D, 1);
    public static Slider alpha = new Slider("Alpha", 0.0D, 80.0D, 255.0D, 0);

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
        addSetting(lineWidth);
        addSetting(alpha);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (nullCheck())
            return;

        renderTileEntities();
    }

    public void renderTileEntities() {
        mc.world.loadedTileEntityList.stream().filter(tileEntity -> mc.player.getDistanceSq(tileEntity.getPos()) <= range.getValue()).forEach(tileEntity -> {
            if (tileEntity instanceof TileEntityChest && chests.getValue() || (tileEntity instanceof TileEntityEnderChest && enderChests.getValue()) || (tileEntity instanceof TileEntityHopper && hoppers.getValue()) || (tileEntity instanceof TileEntityFurnace && furnaces.getValue()) || (tileEntity instanceof TileEntityBed && beds.getValue() || (tileEntity instanceof TileEntityDropper && droppers.getValue()) || (tileEntity instanceof TileEntityShulkerBox && shulkers.getValue()))) {
                switch (mode.getValue()) {
                    case 0:
                        GlStateManager.glLineWidth((float) lineWidth.getValue());
                        RenderUtil.drawBoundingBoxBlockPos(tileEntity.getPos(), 0, ColorUtil.getStorageColor(tileEntity, 144));
                        break;
                    case 1:
                        RenderUtil.drawBoxBlockPos(tileEntity.getPos(), 0, ColorUtil.getStorageColor(tileEntity, (int) alpha.getValue()));
                        break;
                    case 2:
                        GlStateManager.glLineWidth((float) lineWidth.getValue());
                        RenderUtil.drawBoundingBoxBlockPos(tileEntity.getPos(), 0, ColorUtil.getStorageColor(tileEntity, 144));
                        RenderUtil.drawBoxBlockPos(tileEntity.getPos(), 0, ColorUtil.getStorageColor(tileEntity, (int) alpha.getValue()));
                        break;
                }
            }
        });
    }
}

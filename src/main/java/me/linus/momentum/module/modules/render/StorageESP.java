package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.color.SubColor;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.client.ColorUtil;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.render.ESPUtil;
import me.linus.momentum.util.render.builder.RenderBuilder;
import me.linus.momentum.util.render.builder.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.AxisAlignedBB;
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

    public static Checkbox chests = new Checkbox("Chests", true);
    public static SubColor chestPicker = new SubColor(chests, new Color(46, 83, 215));

    public static Checkbox enderChests = new Checkbox("Ender Chests", true);
    public static SubColor enderPicker = new SubColor(enderChests, new Color(156, 46, 215));

    public static Checkbox shulkers = new Checkbox("Shulkers", true);
    public static SubColor shulkerPicker = new SubColor(shulkers, new Color(215, 46, 198));

    public static Checkbox hoppers = new Checkbox("Hoppers", true);
    public static SubColor hopperPicker = new SubColor(hoppers, new Color(106, 106, 114));

    public static Checkbox droppers = new Checkbox("Droppers", true);
    public static SubColor dropperPicker = new SubColor(droppers, new Color(106, 106, 114));

    public static Checkbox furnaces = new Checkbox("Furnaces", true);
    public static SubColor furnacePicker = new SubColor(furnaces, new Color(106, 106, 114));

    public static Checkbox beds = new Checkbox("Beds", true);
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

    public static void renderShaderTileEntities(TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer, TileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage, float renderOffset) {
        AxisAlignedBB bb = new AxisAlignedBB(tileEntityIn.getPos().getX() - mc.getRenderManager().viewerPosX, tileEntityIn.getPos().getY() - mc.getRenderManager().viewerPosY, tileEntityIn.getPos().getZ() - mc.getRenderManager().viewerPosZ, tileEntityIn.getPos().getX() + 1 - mc.getRenderManager().viewerPosX, tileEntityIn.getPos().getY() + 1 - mc.getRenderManager().viewerPosY, tileEntityIn.getPos().getZ() + 1 - mc.getRenderManager().viewerPosZ);

        RenderUtil.camera.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);

        if (!RenderUtil.camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + mc.getRenderManager().viewerPosX, bb.minY + mc.getRenderManager().viewerPosY, bb.minZ + mc.getRenderManager().viewerPosZ, bb.maxX + mc.getRenderManager().viewerPosX, bb.maxY + mc.getRenderManager().viewerPosY, bb.maxZ + mc.getRenderManager().viewerPosZ)))
            return;

        if ((tileEntityIn instanceof TileEntityChest && chests.getValue()) || (tileEntityIn instanceof TileEntityShulkerBox && shulkers.getValue()) || (tileEntityIn instanceof TileEntityEnderChest && enderChests.getValue()) || (tileEntityIn instanceof TileEntityChest && chests.getValue()) || (tileEntityIn instanceof TileEntityDropper && droppers.getValue()) || (tileEntityIn instanceof TileEntityHopper && hoppers.getValue()) || (tileEntityIn instanceof TileEntityFurnace && furnaces.getValue() || (tileEntityIn instanceof TileEntityBed && beds.getValue()))) {
            GlStateManager.pushMatrix();
            ESPUtil.setColor(ColorUtil.getStorageColor(tileEntityIn));
            tileentityspecialrenderer.render(tileEntityIn, x, y, z, partialTicks, destroyStage, renderOffset);
            ESPUtil.renderOne((float) lineWidth.getValue());
            tileentityspecialrenderer.render(tileEntityIn, x, y, z, partialTicks, destroyStage, renderOffset);
            ESPUtil.renderTwo();
            tileentityspecialrenderer.render(tileEntityIn, x, y, z, partialTicks, destroyStage, renderOffset);
            ESPUtil.renderThree();
            ESPUtil.renderFour();
            ESPUtil.setColor(ColorUtil.getStorageColor(tileEntityIn));
            tileentityspecialrenderer.render(tileEntityIn, x, y, z, partialTicks, destroyStage, renderOffset);
            ESPUtil.renderFive();
            ESPUtil.setColor(Color.WHITE);
            GlStateManager.popMatrix();
        }
    }

    public void renderTileEntities() {
        mc.world.loadedTileEntityList.stream().filter(tileEntity -> mc.player.getDistanceSq(tileEntity.getPos()) <= MathUtil.square(range.getValue())).forEach(tileEntity -> {
            if (tileEntity instanceof TileEntityChest && chests.getValue() || (tileEntity instanceof TileEntityEnderChest && enderChests.getValue()) || (tileEntity instanceof TileEntityHopper && hoppers.getValue()) || (tileEntity instanceof TileEntityFurnace && furnaces.getValue()) || (tileEntity instanceof TileEntityBed && beds.getValue() || (tileEntity instanceof TileEntityDropper && droppers.getValue()) || (tileEntity instanceof TileEntityShulkerBox && shulkers.getValue()))) {
                switch (mode.getValue()) {
                    case 0:
                        GlStateManager.glLineWidth((float) lineWidth.getValue());
                        RenderUtil.drawBoxBlockPos(tileEntity.getPos(), 0, ColorUtil.getStorageColor(tileEntity), RenderBuilder.renderMode.Outline);
                        break;
                    case 1:
                        RenderUtil.drawBoxBlockPos(tileEntity.getPos(), 0, ColorUtil.getStorageColor(tileEntity), RenderBuilder.renderMode.Fill);
                        break;
                    case 2:
                        GlStateManager.glLineWidth((float) lineWidth.getValue());
                        RenderUtil.drawBoxBlockPos(tileEntity.getPos(), 0, ColorUtil.getStorageColor(tileEntity), RenderBuilder.renderMode.Both);
                        break;
                }
            }
        });
    }
}
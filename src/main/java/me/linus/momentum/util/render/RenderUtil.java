package me.linus.momentum.util.render;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.modules.render.NameTags;
import me.linus.momentum.util.client.ColorUtil;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.render.builder.RenderBuilder;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.*;


/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class RenderUtil implements MixinInterface {

    public static ICamera camera = new Frustum();
    public static Tessellator tessellator = Tessellator.getInstance();
    public static BufferBuilder bufferbuilder = tessellator.getBuffer();

    public static void drawBoxBlockPos(BlockPos blockPos, double height, Color color, RenderBuilder.RenderMode renderMode) {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPos.getX() - mc.getRenderManager().viewerPosX, blockPos.getY() - mc.getRenderManager().viewerPosY, blockPos.getZ() - mc.getRenderManager().viewerPosZ, blockPos.getX() + 1 - mc.getRenderManager().viewerPosX, blockPos.getY() + 1 - mc.getRenderManager().viewerPosY, blockPos.getZ() + 1 - mc.getRenderManager().viewerPosZ);

        RenderBuilder.glSetup();
        switch (renderMode) {
            case Fill:
                drawSelectionBox(axisAlignedBB, height, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
                break;
            case Outline:
                drawSelectionBoundingBox(axisAlignedBB, height, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 144 / 255f);
                break;
            case Both:
                drawSelectionBox(axisAlignedBB, height, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
                drawSelectionBoundingBox(axisAlignedBB, height, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 144 / 255f);
                break;
            case Glow:
                RenderBuilder.glPrepare();
                drawSelectionGlowFilledBox(axisAlignedBB, height, color, new Color(color.getRed(), color.getGreen(), color.getBlue(), 0));
                RenderBuilder.glRestore();
                break;
        }

        RenderBuilder.glRelease();
    }

    public static void drawBox(AxisAlignedBB axisAlignedBB, double height, Color color, RenderBuilder.RenderMode renderMode) {
        RenderBuilder.glSetup();
        switch (renderMode) {
            case Fill:
                drawSelectionBox(axisAlignedBB, height, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
                break;
            case Outline:
                drawSelectionBoundingBox(axisAlignedBB, height, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 144 / 255f);
                break;
            case Both:
                drawSelectionBox(axisAlignedBB, height, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
                drawSelectionBoundingBox(axisAlignedBB, height, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 144 / 255f);
                break;
            case Glow:
                RenderBuilder.glPrepare();
                drawSelectionGlowFilledBox(axisAlignedBB, height, color, new Color(color.getRed(), color.getGreen(), color.getBlue(), 0));
                RenderBuilder.glRestore();
                break;
        }

        RenderBuilder.glRelease();
    }

    public static void drawSelectionBox(AxisAlignedBB axisAlignedBB, double height, float red, float green, float blue, float alpha) {
        bufferbuilder.begin(GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        addChainedFilledBoxVertices(bufferbuilder, axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.maxY + height, axisAlignedBB.maxZ, red, green, blue, alpha);
        tessellator.draw();
    }

    public static void addChainedFilledBoxVertices(BufferBuilder builder, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
        builder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB axisAlignedBB, double height, float red, float green, float blue, float alpha) {
        bufferbuilder.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        addChainedBoundingBoxVertices(bufferbuilder, axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.maxY + height, axisAlignedBB.maxZ, red, green, blue, alpha);
        tessellator.draw();
    }

    public static void addChainedBoundingBoxVertices(BufferBuilder buffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
        buffer.pos(minX, minY, minZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, maxZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, maxZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, minZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, minY, minZ).color(red, green, blue, 0.0F).endVertex();
    }

    public static void drawSelectionGlowFilledBox(AxisAlignedBB axisAlignedBB, double height, Color startColor, Color endColor) {
        bufferbuilder.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        addChainedGlowBoxVertices(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.maxY + height, axisAlignedBB.maxZ, startColor, endColor);
        tessellator.draw();
    }

    public static void addChainedGlowBoxVertices(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color startColor, Color endColor) {
        bufferbuilder.pos(minX, minY, minZ).color(startColor.getRed() / 255.0f, startColor.getGreen() / 255.0f, startColor.getBlue() / 255.0f, startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, minY, minZ).color(startColor.getRed() / 255.0f, startColor.getGreen() / 255.0f, startColor.getBlue() / 255.0f, startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, minY, maxZ).color(startColor.getRed() / 255.0f, startColor.getGreen() / 255.0f, startColor.getBlue() / 255.0f, startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, minY, maxZ).color(startColor.getRed() / 255.0f, startColor.getGreen() / 255.0f, startColor.getBlue() / 255.0f, startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, maxY, minZ).color(endColor.getRed() / 255.0f, endColor.getGreen() / 255.0f, endColor.getBlue() / 255.0f, endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, maxY, maxZ).color(endColor.getRed() / 255.0f, endColor.getGreen() / 255.0f, endColor.getBlue() / 255.0f, endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, maxY, maxZ).color(endColor.getRed() / 255.0f, endColor.getGreen() / 255.0f, endColor.getBlue() / 255.0f, endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, maxY, minZ).color(endColor.getRed() / 255.0f, endColor.getGreen() / 255.0f, endColor.getBlue() / 255.0f, endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, minY, minZ).color(startColor.getRed() / 255.0f, startColor.getGreen() / 255.0f, startColor.getBlue() / 255.0f, startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, maxY, minZ).color(endColor.getRed() / 255.0f, endColor.getGreen() / 255.0f, endColor.getBlue() / 255.0f, endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, maxY, minZ).color(endColor.getRed() / 255.0f, endColor.getGreen() / 255.0f, endColor.getBlue() / 255.0f, endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, minY, minZ).color(startColor.getRed() / 255.0f, startColor.getGreen() / 255.0f, startColor.getBlue() / 255.0f, startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, minY, minZ).color(startColor.getRed() / 255.0f, startColor.getGreen() / 255.0f, startColor.getBlue() / 255.0f, startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, maxY, minZ).color(endColor.getRed() / 255.0f, endColor.getGreen() / 255.0f, endColor.getBlue() / 255.0f, endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, maxY, maxZ).color(endColor.getRed() / 255.0f, endColor.getGreen() / 255.0f, endColor.getBlue() / 255.0f, endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, minY, maxZ).color(startColor.getRed() / 255.0f, startColor.getGreen() / 255.0f, startColor.getBlue() / 255.0f, startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, minY, maxZ).color(startColor.getRed() / 255.0f, startColor.getGreen() / 255.0f, startColor.getBlue() / 255.0f, startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, minY, maxZ).color(startColor.getRed() / 255.0f, startColor.getGreen() / 255.0f, startColor.getBlue() / 255.0f, startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, maxY, maxZ).color(endColor.getRed() / 255.0f, endColor.getGreen() / 255.0f, endColor.getBlue() / 255.0f, endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, maxY, maxZ).color(endColor.getRed() / 255.0f, endColor.getGreen() / 255.0f, endColor.getBlue() / 255.0f, endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, minY, minZ).color(startColor.getRed() / 255.0f, startColor.getGreen() / 255.0f, startColor.getBlue() / 255.0f, startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, minY, maxZ).color(startColor.getRed() / 255.0f, startColor.getGreen() / 255.0f, startColor.getBlue() / 255.0f, startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, maxY, maxZ).color(endColor.getRed() / 255.0f, endColor.getGreen() / 255.0f, endColor.getBlue() / 255.0f, endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, maxY, minZ).color(endColor.getRed() / 255.0f, endColor.getGreen() / 255.0f, endColor.getBlue() / 255.0f, endColor.getAlpha() / 255.0f).endVertex();
    }

    /**
     * nametag rendering
     */

    public static void drawNametag(String text, double x, double y, double z, float width, float height, double distanceScale, boolean background) {
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float) x, (float) y + 1.4f, (float) z);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, (mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, (float) 0);
        GlStateManager.scale(-(distanceScale / 100), -(distanceScale / 100), (distanceScale / 100));
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();

        if (background)
            GuiScreen.drawRect((int) -width - 1, (int) -(height - 1), (int) width + 2, 3, ColorUtil.toRGBA(NameTags.colorPicker.getColor().getRed(), NameTags.colorPicker.getColor().getGreen(), NameTags.colorPicker.getColor().getBlue(), NameTags.colorPicker.getColor().getAlpha()));

        GlStateManager.disableBlend();
        FontUtil.drawString(text, -width + 1, -height + 3, -1);
    }

    public static void drawNametagFromBlockPos(BlockPos pos, float height, String text) {
        GlStateManager.pushMatrix();
        glBillboardDistanceScaled(pos.getX() + 0.5f, pos.getY() + height, pos.getZ() + 0.5f, mc.player, 1.0f);
        GlStateManager.disableDepth();
        GlStateManager.translate(-(mc.fontRenderer.getStringWidth(text) / 2.0), 0.0, 0.0);
        FontUtil.drawString(text, 0, 0, -1);
        GlStateManager.popMatrix();
    }

    public static void glBillboardDistanceScaled(float x, float y, float z, EntityPlayer player, float scale) {
        glBillboard(x, y, z);
        int distance = (int) player.getDistance(x, y, z);
        float scaleDistance = distance / 2.0f / (2.0f + (2.0f - scale));

        if (scaleDistance < 1.0f)
            scaleDistance = 1.0f;

        GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance);
    }

    public static void glBillboard(float x, float y, float z) {
        float scale = 0.02666667f;

        GlStateManager.translate(x - mc.getRenderManager().renderPosX, y - mc.getRenderManager().renderPosY, z - mc.getRenderManager().renderPosZ);
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-mc.player.rotationYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(mc.player.rotationPitch, (mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
    }

    /**
     * line rendering
     */

    public static void drawLine3D(float x, float y, float z, float minX, float minY, float minZ, float thickness, Color color) {
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ZERO, GL_ONE);
        GlStateManager.shadeModel(GL_SMOOTH);
        glLineWidth(thickness);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        GlStateManager.disableDepth();
        glEnable(GL_DEPTH_CLAMP);
        bufferbuilder.begin(GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y, z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferbuilder.pos(minX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(GL_FLAT);
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.enableDepth();
        glDisable(GL_DEPTH_CLAMP);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}
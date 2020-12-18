package me.linus.momentum.util.render;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.color.ColorUtil;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class RenderUtil extends Tessellator implements MixinInterface {
    public RenderUtil() {
        super(0x200000);
    }

    public static RenderUtil INSTANCE = new RenderUtil();
    public static ICamera camera = new Frustum();

    public static void prepareRender(int mode) {
        prepareGL();
    }

    public static void releaseRender() {
        releaseGL();
    }

    public static void prepareGL() {
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(1.5F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.color(1, 1, 1);
    }

    public static void releaseGL() {
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        GlStateManager.color(1, 1, 1);
        GL11.glColor4f(1, 1, 1, 1);
    }

    public static void prepareProfiler() {
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL32.GL_DEPTH_CLAMP);
    }

    public static void releaseProfiler() {
        GL11.glDisable(GL32.GL_DEPTH_CLAMP);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.glLineWidth(1.0f);
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
    }


    /**
     * 3D Rendering
     */

    // box rendering

    public static void drawVanillaBox(AxisAlignedBB box, float red, float green, float blue, float alpha) {
        try {
            glSetup();
            RenderGlobal.renderFilledBox(box, red, green, blue, alpha);
            glCleanup();
        } catch (Exception ignored) {

        }
    }

    public static void drawVanillaBoxFromBlockPos(BlockPos blockPos, float red, float green, float blue, float alpha) {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPos.getX() - mc.getRenderManager().viewerPosX, blockPos.getY() - mc.getRenderManager().viewerPosY, blockPos.getZ() - mc.getRenderManager().viewerPosZ, blockPos.getX() + 1 - mc.getRenderManager().viewerPosX, blockPos.getY() + 1 - mc.getRenderManager().viewerPosY, blockPos.getZ() + 1 - mc.getRenderManager().viewerPosZ);
        drawVanillaBox(axisAlignedBB, red, green, blue, alpha);
    }

    public static void drawBoxFromBlockPos(BlockPos blockPos, Color color, int sides) {
        drawBox(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, 1, 1, color, sides);
    }

    public static void drawPrismFromBlockPos(BlockPos blockPos, Color color, double height, int sides) {
        drawBox(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, height, 1, color, sides);
    }

    public static void drawBox(double x, double y, double z, double w, double h, double d, Color color, int sides) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GL11.glColor4d(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        bufferbuilder.begin(GL_QUADS, DefaultVertexFormats.POSITION);
        if ((sides & GeometryMasks.Quad.DOWN) != 0) {
            vertexStatic(x + w, y, z, bufferbuilder);
            vertexStatic(x + w, y, z + d, bufferbuilder);
            vertexStatic(x, y, z + d, bufferbuilder);
            vertexStatic(x, y, z, bufferbuilder);
        } if ((sides & GeometryMasks.Quad.UP) != 0) {
            vertexStatic(x + w,y + h, z, bufferbuilder);
            vertexStatic(x,y + h, z, bufferbuilder);
            vertexStatic(x,y + h, z + d, bufferbuilder);
            vertexStatic(x + w, y + h, z + d, bufferbuilder);
        } if ((sides & GeometryMasks.Quad.NORTH) != 0) {
            vertexStatic(x + w, y, z, bufferbuilder);
            vertexStatic(x, y, z, bufferbuilder);
            vertexStatic(x,y + h, z, bufferbuilder);
            vertexStatic(x + w, y + h, z, bufferbuilder);
        } if ((sides & GeometryMasks.Quad.SOUTH) != 0) {
            vertexStatic(x, y,z + d, bufferbuilder);
            vertexStatic(x + w, y,z + d, bufferbuilder);
            vertexStatic(x + w,y + h, z + d, bufferbuilder);
            vertexStatic(x,y + h,z + d, bufferbuilder);
        } if ((sides & GeometryMasks.Quad.WEST) != 0) {
            vertexStatic(x, y, z, bufferbuilder);
            vertexStatic(x, y,z + d, bufferbuilder);
            vertexStatic(x,y + h,z + d, bufferbuilder);
            vertexStatic(x,y + h, z, bufferbuilder);
        } if ((sides & GeometryMasks.Quad.EAST) != 0) {
            vertexStatic(x + w, y,z + d, bufferbuilder);
            vertexStatic(x + w, y, z, bufferbuilder);
            vertexStatic(x + w,y + h,z, bufferbuilder);
            vertexStatic(x + w,y + h,z + d, bufferbuilder);
        }

        tessellator.draw();
    }

    public static void drawGradientBoxFromBlockPos(BlockPos blockPos, Color startColor, Color endColor, int sides) {
        drawGradientBox(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, 1, 1, startColor, endColor, sides);
    }

    public static void drawGradientBox(double x, double y, double z, double w, double h, double d, Color startColor, Color endColor, int sides) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL_QUADS, DefaultVertexFormats.POSITION);
        if ((sides & GeometryMasks.Quad.DOWN) != 0)
        if ((sides & GeometryMasks.Quad.UP) != 0)

        if ((sides & GeometryMasks.Quad.NORTH) != 0) {
            vertexColor(x + w, y, z, bufferbuilder, startColor);
            vertexColor(x, y, z, bufferbuilder, startColor);
            vertexColor(x,y + h, z, bufferbuilder, endColor);
            vertexColor(x + w, y + h, z, bufferbuilder, endColor);
        } if ((sides & GeometryMasks.Quad.SOUTH) != 0) {
            vertexColor(x, y,z + d, bufferbuilder, startColor);
            vertexColor(x + w, y,z + d, bufferbuilder, startColor);
            vertexColor(x + w,y + h, z + d, bufferbuilder, endColor);
            vertexColor(x,y + h,z + d, bufferbuilder, endColor);
        } if ((sides & GeometryMasks.Quad.WEST) != 0) {
            vertexColor(x, y, z, bufferbuilder, startColor);
            vertexColor(x, y,z + d, bufferbuilder, startColor);
            vertexColor(x,y + h,z + d, bufferbuilder, endColor);
            vertexColor(x,y + h, z, bufferbuilder, endColor);
        } if ((sides & GeometryMasks.Quad.EAST) != 0) {
            vertexColor(x + w, y,z + d, bufferbuilder, startColor);
            vertexColor(x + w, y, z, bufferbuilder, startColor);
            vertexColor(x + w,y + h,z, bufferbuilder, endColor);
            vertexColor(x + w,y + h,z + d, bufferbuilder, endColor);
        }

        tessellator.draw();
    }

    public static void drawGlowBox(final BlockPos pos, final Color startColor, final Color midColor, final Color endColor) {
        final float red = startColor.getRed() / 255.0f;
        final float green = startColor.getGreen() / 255.0f;
        final float blue = startColor.getBlue() / 255.0f;
        final float alpha = startColor.getAlpha() / 255.0f;
        final float red2 = endColor.getRed() / 255.0f;
        final float green2 = endColor.getGreen() / 255.0f;
        final float blue2 = endColor.getBlue() / 255.0f;
        final float alpha2 = endColor.getAlpha() / 255.0f;
        final float red3 = midColor.getRed() / 255.0f;
        final float green3 = midColor.getGreen() / 255.0f;
        final float blue3 = midColor.getBlue() / 255.0f;
        final float alpha3 = midColor.getAlpha() / 255.0f;
        final AxisAlignedBB bb = new AxisAlignedBB(pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, pos.getX() + 1.0 - RenderUtil.mc.getRenderManager().viewerPosX, pos.getY() + 1.0 - RenderUtil.mc.getRenderManager().viewerPosY, pos.getZ() + 1.0 - RenderUtil.mc.getRenderManager().viewerPosZ);
        final double offset = (bb.maxY - bb.minY) / 2.0;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder builder = tessellator.getBuffer();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        builder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        //north
        builder.pos(bb.maxX, bb.minY, bb.minZ).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos(bb.maxX, bb.minY, bb.minZ).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        //west
        builder.pos(bb.minX, bb.minY, bb.minZ).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos(bb.minX, bb.minY, bb.maxZ).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        //east
        builder.pos(bb.maxX, bb.minY, bb.maxZ).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos(bb.maxX, bb.minY, bb.minZ).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        //south
        builder.pos(bb.maxX, bb.minY, bb.maxZ).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos(bb.maxX, bb.minY, bb.maxZ).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        //up + down
        builder.pos(bb.minX, bb.maxY, bb.minZ).color(red3, green3, blue3, alpha3).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.minZ).color(red3, green3, blue3, alpha3).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.maxZ).color(red3, green3, blue3, alpha3).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.minZ).color(red3, green3, blue3, alpha3).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red3, green3, blue3, alpha3).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red3, green3, blue3, alpha3).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red3, green3, blue3, alpha3).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawBetterGlowBox(final BlockPos pos, final Color startColor, final Color endColor) {
        drawBetterGlowBox(pos.getX(), pos.getY(), pos.getY(), 1, 1, 1, startColor, endColor);
    }

    public static void drawBetterGlowBox(int x, int y, int z, int w, int h, int d, Color firstColor, Color endColor) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        bufferbuilder.begin(5, DefaultVertexFormats.POSITION);
        vertexColor(x + w, y, z, bufferbuilder, firstColor);
        vertexColor(x, y, z, bufferbuilder, firstColor);
        vertexColor(x,y + h, z, bufferbuilder, endColor);
        vertexColor(x + w,y + h, z, bufferbuilder, endColor);
        vertexColor(x, y,z + d, bufferbuilder, firstColor);
        vertexColor(x + w, y,z + d, bufferbuilder, firstColor);
        vertexColor(x + w,y + h,z + d, bufferbuilder, endColor);
        vertexColor(x,y + h,z + d, bufferbuilder, endColor);
        vertexColor(x, y, z, bufferbuilder, firstColor);
        vertexColor(x, y,z + d, bufferbuilder, firstColor);
        vertexColor(x,y + h,z + d, bufferbuilder, endColor);
        vertexColor(x,y + h, z, bufferbuilder, endColor);
        vertexColor(x + w, y,z+d, bufferbuilder, firstColor);
        vertexColor(x + w, y, z, bufferbuilder, firstColor);
        vertexColor(x + w,y + h, z, bufferbuilder, endColor);
        vertexColor(x + w,y + h,z + d, bufferbuilder, endColor);
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    /**
     * bounding box render functions from kami, will rewrite later
     */

    public static void drawBoundingBoxBlockPos(BlockPos bp, float width, Color color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(width);
        double x = (double) bp.getX() - mc.getRenderManager().viewerPosX;
        double y = (double) bp.getY() - mc.getRenderManager().viewerPosY;
        double z = (double) bp.getZ() - mc.getRenderManager().viewerPosZ;
        AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int alpha = color.getAlpha();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
        tessellator.draw();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();
        tessellator.draw();
        bufferbuilder.begin(1, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawBoundingBoxBottomBlockPos(BlockPos bp, float width, Color color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(width);
        double x = (double) bp.getX() - mc.getRenderManager().viewerPosX;
        double y = (double) bp.getY() - mc.getRenderManager().viewerPosY;
        double z = (double) bp.getZ() - mc.getRenderManager().viewerPosZ;
        AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int alpha = color.getAlpha();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    // nametags
    public static void drawNametag (Entity entity, String[] text, Color color, int type) {
        Vec3d pos = EntityUtil.getInterpolatedPos(entity, mc.getRenderPartialTicks());
        drawNametag(pos.x, pos.y + entity.height, pos.z, text, color, type);
    }

    public static void drawNametag (double x, double y, double z, String[] text, Color color, int type) {
        double dist = mc.player.getDistance(x, y, z);
        double scale = 1, offset = 0;
        int start = 0;
        switch (type) {
            case 0:
                scale = dist / 20 * Math.pow(1.2589254,0.1 / (dist < 25 ? 0.5 : 2));
                scale = Math.min(Math.max(scale, 0.5), 5);
                offset = scale > 2 ? scale / 2 : scale;
                scale /= 40;
                start = 10;
                break;
            case 1:
                scale =- ((int) dist) / 6.0;

                if (scale < 1)
                    scale = 1;

                scale *= 2.0 / 75.0;
                break;
            case 2:
                scale = 0.0018 + 0.003 * dist;

                if (dist <= 8.0)
                    scale = 0.0245;

                start =- 8;
                break;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x - mc.getRenderManager().viewerPosX,y + offset-mc.getRenderManager().viewerPosY,z - mc.getRenderManager().viewerPosZ);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0, 1, 0);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, mc.gameSettings.thirdPersonView == 2? -1 : 1, 0, 0);
        GlStateManager.scale(-scale, -scale, scale);

        if (type == 2) {
            double width = 0;
            for (int i = 0; i<text.length; i++) {
                double w = FontUtil.getStringWidth(text[i]) / 2;
                if (w > width)
                    width = w;
            }

            drawBorderedRect(-width - 1, -mc.fontRenderer.FONT_HEIGHT,width + 2,1, new Color(0, 0, 0, 90));
        }

        GlStateManager.enableTexture2D();
        for (int i = 0; i < text.length; i++)
            FontUtil.drawStringWithShadow(text[i], -FontUtil.getStringWidth(text[i]) / 2,i * (mc.fontRenderer.FONT_HEIGHT + 1) + start, color.getRGB());

        GlStateManager.disableTexture2D();

        if (type!=2)
            GlStateManager.popMatrix();
    }

    private static void drawBorderedRect (double x, double y, double x1, double y1, Color color) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(x,y1,0).endVertex();
        bufferbuilder.pos(x1,y1,0).endVertex();
        bufferbuilder.pos(x1,y,0).endVertex();
        bufferbuilder.pos(x,y,0).endVertex();
        tessellator.draw();
    }

    public static void drawNametagFromBlockPos(final BlockPos pos, final String text) {
        GlStateManager.pushMatrix();
        glBillboardDistanceScaled(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, mc.player, 1.0f);
        GlStateManager.disableDepth();
        GlStateManager.translate(-(mc.fontRenderer.getStringWidth(text) / 2.0), 0.0, 0.0);
        FontUtil.drawStringWithShadow(text, 0, 0, new Color(255, 255, 255).getRGB());
        GlStateManager.popMatrix();
    }

    public static void glBillboardDistanceScaled(final float x, final float y, final float z, final EntityPlayer player, final float scale) {
        glBillboard(x, y, z);
        final int distance = (int) player.getDistance(x, y, z);
        float scaleDistance = distance / 2.0f / (2.0f + (2.0f - scale));
        if (scaleDistance < 1.0f) {
            scaleDistance = 1.0f;
        }

        GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance);
    }

    public static void glBillboard(final float x, final float y, final float z) {
        final float scale = 0.02666667f;
        GlStateManager.translate(x - mc.getRenderManager().renderPosX, y - mc.getRenderManager().renderPosY, z - mc.getRenderManager().renderPosZ);
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-mc.player.rotationYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(mc.player.rotationPitch, (mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
    }

    // GL
    public static void enableGL3D(float lineWidth) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        mc.entityRenderer.disableLightmap();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(lineWidth);
    }

    public static void disableGL3D() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void enableGLGlow() {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        mc.entityRenderer.disableLightmap();
        GL11.glEnable(2848);
        GL11.glEnable(GL_CULL_FACE);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableDepth();
        GlStateManager.glLineWidth(1.0f);
    }

    public static void disableGLGlow() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glDisable(GL_CULL_FACE);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
        GlStateManager.glLineWidth(1.0f);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
    }

    public static void glSetup() {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glLineWidth(1.5f);
    }

    public static void glCleanup() {
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    // vertices
    private static void vertexStatic(double x, double y, double z, BufferBuilder bufferbuilder) {
        bufferbuilder.pos(x - mc.getRenderManager().viewerPosX,y - mc.getRenderManager().viewerPosY,z - mc.getRenderManager().viewerPosZ).endVertex();
    }

    private static void vertexColor(double x, double y, double z, BufferBuilder bufferbuilder, Color color) {
        bufferbuilder.pos(x - mc.getRenderManager().viewerPosX,y - mc.getRenderManager().viewerPosY,z - mc.getRenderManager().viewerPosZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
    }

    // lines

    public static void drawLine3D(float x, float y, float z, float x1, float y1, float z1, float thickness, int hex) {
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        float alpha = (hex >> 24 & 0xFF) / 255.0F;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(GL_SMOOTH);
        glLineWidth(thickness);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        GlStateManager.disableDepth();
        glEnable(GL32.GL_DEPTH_CLAMP);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y, z).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(GL_FLAT);
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.enableDepth();
        glDisable(GL32.GL_DEPTH_CLAMP);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public static boolean shouldRenderTracer(Entity e, boolean player, boolean mobs, boolean animals, boolean items) {
        if (e == mc.player)
            return false;

        if (e instanceof EntityPlayer)
            return player;

        if ((EntityUtil.isHostileMob(e) || EntityUtil.isNeutralMob(e)))
            return mobs;

        if (EntityUtil.isPassive(e))
            return animals;

        if (e instanceof EntityItem)
            return items;

        else
            return false;
    }

    /**
     * 2D Rendering
     */

    private static final FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
    private static final IntBuffer viewport = BufferUtils.createIntBuffer(16);
    private static final FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
    private static final FloatBuffer projection = BufferUtils.createFloatBuffer(16);

    public static Vec3d to2D(double x, double y, double z) {
        GL11.glGetFloat(2982);
        GL11.glGetFloat(2983);
        GL11.glGetInteger(2978);
        boolean result = GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, screenCoords);
        if (result)
            return new Vec3d(screenCoords.get(0), (Display.getHeight() - screenCoords.get(1)), screenCoords.get(2));
        return null;
    }

    public static boolean isOnScreen(Vec3d pos) {
        if (pos.x > -1.0D && pos.y < 1.0D) {
            final boolean b = (pos.x / (mc.gameSettings.guiScale) >= 0.0D && pos.x / (mc.gameSettings.guiScale) <= Display.getWidth() && pos.y / (mc.gameSettings.guiScale) >= 0.0D && pos.y / (mc.gameSettings.guiScale) <= Display.getHeight());
            return b;
        }
        return false;
    }

    public static boolean is2DValid(EntityPlayer entity) {
        return (entity != mc.player && (!entity.isInvisible() && entity.isEntityAlive()));
    }

    public static void drawHitMarkers(Color color) {
        ScaledResolution resolution = new ScaledResolution(mc);
        drawLine(resolution.getScaledWidth() / 2.0F - 4.0F, resolution.getScaledHeight() / 2.0F - 4.0F, resolution.getScaledWidth() / 2.0F - 8.0F, resolution.getScaledHeight() / 2.0F - 8.0F, 0.5F, ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), 255));
        drawLine(resolution.getScaledWidth() / 2.0F + 4.0F, resolution.getScaledHeight() / 2.0F - 4.0F, resolution.getScaledWidth() / 2.0F + 8.0F, resolution.getScaledHeight() / 2.0F - 8.0F, 0.5F, ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), 255));
        drawLine(resolution.getScaledWidth() / 2.0F - 4.0F, resolution.getScaledHeight() / 2.0F + 4.0F, resolution.getScaledWidth() / 2.0F - 8.0F, resolution.getScaledHeight() / 2.0F + 8.0F, 0.5F, ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), 255));
        drawLine(resolution.getScaledWidth() / 2.0F + 4.0F, resolution.getScaledHeight() / 2.0F + 4.0F, resolution.getScaledWidth() / 2.0F + 8.0F, resolution.getScaledHeight() / 2.0F + 8.0F, 0.5F, ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), 255));
    }

    public static void drawTracerPointer(float x, float y, float size, float widthDiv, float heightDiv, boolean outline, float outlineWidth, int color) {
        boolean blend = GL11.glIsEnabled(3042);
        float alpha = (color >> 24 & 0xFF) / 255.0F;

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        ColorUtil.hexColor(color);
        GL11.glBegin(7);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d((x - size / widthDiv), (y + size));
        GL11.glVertex2d(x, (y + size / heightDiv));
        GL11.glVertex2d((x + size / widthDiv), (y + size));
        GL11.glVertex2d(x, y);
        GL11.glEnd();

        if (outline) {
            GL11.glLineWidth(outlineWidth);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, alpha);
            GL11.glBegin(2);
            GL11.glVertex2d(x, y);
            GL11.glVertex2d((x - size / widthDiv), (y + size));
            GL11.glVertex2d(x, (y + size / heightDiv));
            GL11.glVertex2d((x + size / widthDiv), (y + size));
            GL11.glVertex2d(x, y);
            GL11.glEnd();
        }

        GL11.glPopMatrix();
        GL11.glEnable(3553);

        if (!blend)
            GL11.glDisable(3042);

        GL11.glDisable(2848);
    }

    public static void drawLine(float x, float y, float x1, float y1, float thickness, int hex) {
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GL11.glLineWidth(thickness);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(x1, y1, 0.0D).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GL11.glDisable(2848);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

}

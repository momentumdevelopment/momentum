package me.linus.momentum.util.render;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.ColorUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class RenderUtil implements MixinInterface {
    public static ICamera camera = new Frustum();

    /**
     * rendering setup
     */

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

    public static void glRelease() {
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

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

    /**
     * prism rendering
     */

    public static void drawBoxBlockPos(BlockPos blockPos, double height, Color color) {
        glSetup();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPos.getX() - mc.getRenderManager().viewerPosX, blockPos.getY() - mc.getRenderManager().viewerPosY, blockPos.getZ() - mc.getRenderManager().viewerPosZ, blockPos.getX() + 1 - mc.getRenderManager().viewerPosX, blockPos.getY() + 1 - mc.getRenderManager().viewerPosY, blockPos.getZ() + 1 - mc.getRenderManager().viewerPosZ);
        drawBox(axisAlignedBB, height, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        glRelease();
    }

    public static void drawBox(AxisAlignedBB aabb, double height, float red, float green, float blue, float alpha) {
        glSetup();
        renderFilledBox(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, height, red, green, blue, alpha);
        glRelease();
    }

    public static void renderFilledBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, double height, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        addChainedFilledBoxVertices(bufferbuilder, minX, minY, minZ, maxX, maxY + height, maxZ, red, green, blue, alpha);
        tessellator.draw();
    }


    public static void addChainedFilledBoxVertices(BufferBuilder builder, double x1, double y1, double z1, double x2, double y2, double z2, float red, float green, float blue, float alpha) {
        builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
    }

    public static void drawBoundingBoxBlockPos(BlockPos blockPos, double height, Color color) {
        glSetup();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPos.getX() - mc.getRenderManager().viewerPosX, blockPos.getY() - mc.getRenderManager().viewerPosY, blockPos.getZ() - mc.getRenderManager().viewerPosZ, blockPos.getX() + 1 - mc.getRenderManager().viewerPosX, blockPos.getY() + 1 - mc.getRenderManager().viewerPosY, blockPos.getZ() + 1 - mc.getRenderManager().viewerPosZ);
        drawSelectionBoundingBox(axisAlignedBB, height, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        glRelease();
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB box, double height, float red, float green, float blue, float alpha) {
        drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, height, red, green, blue, alpha);
    }

    public static void drawBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, double height, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        drawBoundingBox(bufferbuilder, minX, minY, minZ, maxX, maxY + height, maxZ, red, green, blue, alpha);
        tessellator.draw();
    }

    public static void drawBoundingBox(BufferBuilder buffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
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

    /**
     * nametag rendering
     */

    public static void drawNametagFromBlockPos(final BlockPos pos, final String text) {
        GlStateManager.pushMatrix();
        glBillboardDistanceScaled(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, mc.player, 1.0f);
        GlStateManager.disableDepth();
        GlStateManager.translate(-(mc.fontRenderer.getStringWidth(text) / 2.0), 0.0, 0.0);
        FontUtil.drawString(text, 0, 0, -1);
        GlStateManager.popMatrix();
    }

    public static void glBillboardDistanceScaled(final float x, final float y, final float z, final EntityPlayer player, final float scale) {
        glBillboard(x, y, z);
        int distance = (int) player.getDistance(x, y, z);
        float scaleDistance = distance / 2.0f / (2.0f + (2.0f - scale));

        if (scaleDistance < 1.0f)
            scaleDistance = 1.0f;

        GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance);
    }

    public static void glBillboard(final float x, final float y, final float z) {
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
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
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

    /**
     * 2D rendering
     */

    public static void drawHitMarkers(Color color) {
        ScaledResolution resolution = new ScaledResolution(mc);
        drawLine(resolution.getScaledWidth() / 2.0F - 4.0F, resolution.getScaledHeight() / 2.0F - 4.0F, resolution.getScaledWidth() / 2.0F - 8.0F, resolution.getScaledHeight() / 2.0F - 8.0F, 0.5F, ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), 255));
        drawLine(resolution.getScaledWidth() / 2.0F + 4.0F, resolution.getScaledHeight() / 2.0F - 4.0F, resolution.getScaledWidth() / 2.0F + 8.0F, resolution.getScaledHeight() / 2.0F - 8.0F, 0.5F, ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), 255));
        drawLine(resolution.getScaledWidth() / 2.0F - 4.0F, resolution.getScaledHeight() / 2.0F + 4.0F, resolution.getScaledWidth() / 2.0F - 8.0F, resolution.getScaledHeight() / 2.0F + 8.0F, 0.5F, ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), 255));
        drawLine(resolution.getScaledWidth() / 2.0F + 4.0F, resolution.getScaledHeight() / 2.0F + 4.0F, resolution.getScaledWidth() / 2.0F + 8.0F, resolution.getScaledHeight() / 2.0F + 8.0F, 0.5F, ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), 255));
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
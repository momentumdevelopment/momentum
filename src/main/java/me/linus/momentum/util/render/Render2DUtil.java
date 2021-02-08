package me.linus.momentum.util.render;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.ColorUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author linustouchtips
 * @since 01/12/2021
 */

public class Render2DUtil implements MixinInterface {

    public static void drawCircle(int x, int y, double radius, int color) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glColor4f(((color >> 16) & 0xff) / 255F, ((color >> 8) & 0xff) / 255F, (color & 0xff) / 255F, ((color >> 24) & 0xff) / 255F);
        glBegin(GL_LINE_LOOP);

        for (int i = 0; i <= 360; i++)
            glVertex2d( x + Math.sin(((i * Math.PI) / 180)) * radius, y + Math.cos(((i * Math.PI) / 180)) * radius);

        glEnd();
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    public static void drawFilledCircle(int x, int y, double radius, int color) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glColor4f(((color >> 16) & 0xff) / 255F, ((color >> 8) & 0xff) / 255F, (color & 0xff) / 255F, ((color >> 24) & 0xff) / 255F);
        glBegin(GL_TRIANGLE_FAN);

        for (int i = 0; i <= 360; i++)
            glVertex2d( x + Math.sin(((i * Math.PI) / 180)) * radius, y + Math.cos(((i * Math.PI) / 180)) * radius);

        glEnd();
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    public static void drawTriangle(Entity e, double x, double y, int color) {
        glPushMatrix();
        glScaled(0.5, 0.5, 0.5);
        glTranslated(x, y, 0);

        if (e instanceof EntityPlayerSP)
            glRotatef(e.rotationYaw, 0F, 0F, 1.0F);
        else
            glRotatef(-e.rotationYaw, 0F, 0F, 1.0F);

        glColor4f(((color >> 16) & 0xff) / 255F, ((color >> 8) & 0xff) / 255F, (color & 0xff) / 255F, ((color >> 24) & 0xff) / 255F);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glBegin(GL_TRIANGLES);

        glVertex2d(0, 0 + 6);
        glVertex2d(0 + 3, 0 - 2);
        glVertex2d(0 - 3, 0 - 2);

        glEnd();
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_TEXTURE_2D);
        glRotatef(e.rotationYaw, 0F, 0F, 1.0F);

        glPopMatrix();
    }

    public static void drawHitMarkers(Color color) {
        ScaledResolution resolution = new ScaledResolution(mc);
        drawLine(resolution.getScaledWidth() / 2.0F - 4.0F, resolution.getScaledHeight() / 2.0F - 4.0F, resolution.getScaledWidth() / 2.0F - 8.0F, resolution.getScaledHeight() / 2.0F - 8.0F, 0.5F, ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), 255));
        drawLine(resolution.getScaledWidth() / 2.0F + 4.0F, resolution.getScaledHeight() / 2.0F - 4.0F, resolution.getScaledWidth() / 2.0F + 8.0F, resolution.getScaledHeight() / 2.0F - 8.0F, 0.5F, ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), 255));
        drawLine(resolution.getScaledWidth() / 2.0F - 4.0F, resolution.getScaledHeight() / 2.0F + 4.0F, resolution.getScaledWidth() / 2.0F - 8.0F, resolution.getScaledHeight() / 2.0F + 8.0F, 0.5F, ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), 255));
        drawLine(resolution.getScaledWidth() / 2.0F + 4.0F, resolution.getScaledHeight() / 2.0F + 4.0F, resolution.getScaledWidth() / 2.0F + 8.0F, resolution.getScaledHeight() / 2.0F + 8.0F, 0.5F, ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), 255));
    }

    public static void drawLine(float x, float y, float x1, float y1, float thickness, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ZERO, GL_ONE);
        GlStateManager.shadeModel(GL_SMOOTH);
        glLineWidth(thickness);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        RenderUtil.bufferbuilder.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        RenderUtil.bufferbuilder.pos(x, y, 0.0D).color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, (color >> 24 & 0xFF) / 255.0F).endVertex();
        RenderUtil.bufferbuilder.pos(x1, y1, 0.0D).color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, (color >> 24 & 0xFF) / 255.0F).endVertex();
        RenderUtil.tessellator.draw();
        GlStateManager.shadeModel(GL_FLAT);
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) posX, (float) posY, 50.0F);
        GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float) Math.atan(mouseY / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        mc.getRenderManager().setPlayerViewY(180.0F);
        mc.getRenderManager().setRenderShadow(false);
        mc.getRenderManager().renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        mc.getRenderManager().setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static void drawPickerBase(int pickerX, int pickerY, int pickerWidth, int pickerHeight, float red, float green, float blue, float alpha) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(GL_SMOOTH);
        glBegin(GL_POLYGON);
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glVertex2f(pickerX, pickerY);
        glVertex2f(pickerX, pickerY + pickerHeight);
        glColor4f(red, green, blue, alpha);
        glVertex2f(pickerX + pickerWidth, pickerY + pickerHeight);
        glVertex2f(pickerX + pickerWidth, pickerY);
        glEnd();
        glDisable(GL_ALPHA_TEST);
        glBegin(GL_POLYGON);
        glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        glVertex2f(pickerX, pickerY);
        glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        glVertex2f(pickerX, pickerY + pickerHeight);
        glVertex2f(pickerX + pickerWidth, pickerY + pickerHeight);
        glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        glVertex2f(pickerX + pickerWidth, pickerY);
        glEnd();
        glEnable(GL_ALPHA_TEST);
        glShadeModel(GL_FLAT);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    public static void gradient(int minX, int minY, int maxX, int maxY, int startColor, int endColor, boolean left) {
        if (left) {
            glEnable(GL_BLEND);
            glDisable(GL_TEXTURE_2D);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glShadeModel(GL_SMOOTH);
            glBegin(GL_POLYGON);
            glColor4f((startColor >> 16 & 0xFF) / 255.0f, (startColor >> 8 & 0xFF) / 255.0f, (startColor & 0xFF) / 255.0f, (startColor >> 24 & 0xFF) / 255.0f);
            glVertex2f(minX, minY);
            glVertex2f(minX, maxY);
            glColor4f((endColor >> 16 & 0xFF) / 255.0f, (endColor >> 8 & 0xFF) / 255.0f, (endColor & 0xFF) / 255.0f, (endColor >> 24 & 0xFF) / 255.0f);
            glVertex2f(maxX, maxY);
            glVertex2f(maxX, minY);
            glEnd();
            glShadeModel(GL_FLAT);
            glEnable(GL_TEXTURE_2D);
            glDisable(GL_BLEND);
        } 
        
        else 
            drawGradientRect(minX, minY, maxX, maxY, startColor, endColor);
    }

    public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL_SMOOTH);
        RenderUtil.bufferbuilder.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        RenderUtil.bufferbuilder.pos(right, top, 0).color((float) (startColor >> 16 & 255) / 255.0F, (float) (startColor >> 8 & 255) / 255.0F, (float) (startColor & 255) / 255.0F, (float) (startColor >> 24 & 255) / 255.0F).endVertex();
        RenderUtil.bufferbuilder.pos(left, top, 0).color((float) (startColor >> 16 & 255) / 255.0F, (float) (startColor >> 8 & 255) / 255.0F, (float) (startColor & 255) / 255.0F, (float) (startColor >> 24 & 255) / 255.0F).endVertex();
        RenderUtil.bufferbuilder.pos(left, bottom, 0).color((float) (endColor >> 16 & 255) / 255.0F, (float) (endColor >> 8 & 255) / 255.0F, (float) (endColor & 255) / 255.0F, (float) (endColor >> 24 & 255) / 255.0F).endVertex();
        RenderUtil.bufferbuilder.pos(right, bottom, 0).color((float) (endColor >> 16 & 255) / 255.0F, (float) (endColor >> 8 & 255) / 255.0F, (float) (endColor & 255) / 255.0F, (float) (endColor >> 24 & 255) / 255.0F).endVertex();
        RenderUtil.tessellator.draw();
        GlStateManager.shadeModel(GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawLeftGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL_SMOOTH);
        RenderUtil.bufferbuilder.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        RenderUtil.bufferbuilder.pos(right, top, 0).color((float) (endColor >> 24 & 255) / 255.0F, (float) (endColor >> 16 & 255) / 255.0F, (float) (endColor >> 8 & 255) / 255.0F, (float) (endColor >> 24 & 255) / 255.0F).endVertex();
        RenderUtil.bufferbuilder.pos(left, top, 0).color((float) (startColor >> 16 & 255) / 255.0F, (float) (startColor >> 8 & 255) / 255.0F, (float) (startColor & 255) / 255.0F, (float) (startColor >> 24 & 255) / 255.0F).endVertex();
        RenderUtil.bufferbuilder.pos(left, bottom, 0).color((float) (startColor >> 16 & 255) / 255.0F, (float) (startColor >> 8 & 255) / 255.0F, (float) (startColor & 255) / 255.0F, (float) (startColor >> 24 & 255) / 255.0F).endVertex();
        RenderUtil.bufferbuilder.pos(right, bottom, 0).color((float) (endColor >> 24 & 255) / 255.0F, (float) (endColor >> 16 & 255) / 255.0F, (float) (endColor >> 8 & 255) / 255.0F, (float) (endColor >> 24 & 255) / 255.0F).endVertex();
        RenderUtil.tessellator.draw();
        GlStateManager.shadeModel(GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}
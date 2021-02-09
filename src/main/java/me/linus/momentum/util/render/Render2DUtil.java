package me.linus.momentum.util.render;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.ColorUtil;
import me.linus.momentum.util.render.builder.Render2DBuilder;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author linustouchtips
 * @since 01/12/2021
 */

public class Render2DUtil implements MixinInterface {

    public static void drawRect(int left, int top, int right, int bottom, int borderWidth, int insideColor, int borderColor, boolean scissor, Render2DBuilder.Render2DMode render2DMode) {
        if (scissor) {
            Render2DBuilder.prepareScissor(left, top, right, bottom);
            GL11.glEnable(GL_SCISSOR_TEST);
        }

        switch (render2DMode) {
            case Normal:
                drawRectBase(left, top, right, bottom, insideColor);
                break;
            case Border:
                drawBorder(left, top, right, bottom, borderWidth, insideColor);
                break;
            case Both:
                drawBorderedRect(left, top, right, bottom, borderWidth, insideColor, borderColor);
                break;
        }

        if (scissor)
            GL11.glDisable(GL_SCISSOR_TEST);
    }

    public static void drawBorderedRect(int left, int top, int right, int bottom, int borderWidth, int insideColor, int borderColor) {
        drawRectBase(left + borderWidth, top + borderWidth, right - borderWidth, bottom - borderWidth, insideColor);

        drawRectBase(left, top + borderWidth, left + borderWidth, bottom - borderWidth, borderColor);
        drawRectBase(right - borderWidth, top + borderWidth, right, bottom - borderWidth, borderColor);
        drawRectBase(left, top, right, top + borderWidth, borderColor);
        drawRectBase(left, bottom - borderWidth, right, bottom, borderColor);
    }

    public static void drawBorder(int left, int top, int right, int bottom, int borderWidth, int color) {
        drawRectBase(left, top + borderWidth, left + borderWidth, bottom - borderWidth, color);
        drawRectBase(right - borderWidth, top + borderWidth, right, bottom - borderWidth, color);
        drawRectBase(left, top, right, top + borderWidth, color);
        drawRectBase(left, bottom - borderWidth, right, bottom, color);
    }
    
    public static void drawRectBase(int left, int top, int right, int bottom, int color) {
        int side;

        if (left < right) {
            side = left;
            left = right;
            right = side;
        }

        if (top < bottom) {
            side = top;
            top = bottom;
            bottom = side;
        }

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color((float) (color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F, (float) (color >> 24 & 255) / 255.0F);
        RenderUtil.bufferbuilder.begin(GL_QUADS, DefaultVertexFormats.POSITION);
        RenderUtil.bufferbuilder.pos(left, bottom, 0.0D).endVertex();
        RenderUtil.bufferbuilder.pos(right, bottom, 0.0D).endVertex();
        RenderUtil.bufferbuilder.pos(right, top, 0.0D).endVertex();
        RenderUtil.bufferbuilder.pos(left, top, 0.0D).endVertex();
        RenderUtil.tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

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

    public static void drawTriangle(double x, double y, float rotation, int color) {
        glPushMatrix();
        glScaled(0.5, 0.5, 0.5);
        glTranslated(x, y, 0);
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
        glRotatef(rotation, 0F, 0F, 1.0F);

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
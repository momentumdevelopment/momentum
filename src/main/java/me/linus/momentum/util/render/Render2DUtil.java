package me.linus.momentum.util.render;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.ColorUtil;
import me.linus.momentum.util.render.builder.RenderUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 01/12/2021
 */

public class Render2DUtil implements MixinInterface {

    public static void drawCircle(int x, int y, double radius, int color) {
        GL11.glEnable(3042 /* GL_BLEND */);
        GL11.glDisable(3553 /* GL_TEXTURE_2D */);
        GL11.glEnable(2848 /* GL_LINE_SMOOTH */);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(((color >> 16) & 0xff) / 255F, ((color >> 8) & 0xff) / 255F, (color & 0xff) / 255F, ((color >> 24) & 0xff) / 255F);
        GL11.glBegin(2 /* GL_TRIANGLE_LOOP */);

        for (int i = 0; i <= 360; i++)
            GL11.glVertex2d( x + Math.sin(((i * Math.PI) / 180)) * radius, y + Math.cos(((i * Math.PI) / 180)) * radius);

        GL11.glEnd();
        GL11.glDisable(2848 /* GL_LINE_SMOOTH */);
        GL11.glEnable(3553 /* GL_TEXTURE_2D */);
        GL11.glDisable(3042 /* GL_BLEND */);
    }

    public static void drawFilledCircle(int x, int y, double radius, int color) {
        GL11.glEnable(3042 /* GL_BLEND */);
        GL11.glDisable(3553 /* GL_TEXTURE_2D */);
        GL11.glEnable(2848 /* GL_LINE_SMOOTH */);
        GL11.glBlendFunc(770, 771 );
        GL11.glColor4f(((color >> 16) & 0xff) / 255F, ((color >> 8) & 0xff) / 255F, (color & 0xff) / 255F, ((color >> 24) & 0xff) / 255F);
        GL11.glBegin( 6 /* GL_TRIANGLE_FAN */);

        for (int i = 0; i <= 360; i++)
            GL11.glVertex2d( x + Math.sin(((i * Math.PI) / 180)) * radius, y + Math.cos(((i * Math.PI) / 180)) * radius);

        GL11.glEnd();
        GL11.glDisable(2848 /* GL_LINE_SMOOTH */);
        GL11.glEnable(3553 /* GL_TEXTURE_2D */);
        GL11.glDisable(3042 /* GL_BLEND */);
    }

    public static void drawTriangle(Entity e, double x, double y, int color) {
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        GL11.glTranslated(x, y, 0);

        if (e instanceof EntityPlayerSP)
            GL11.glRotatef( e.rotationYaw, 0F, 0F, 1.0F );
        else
            GL11.glRotatef( -e.rotationYaw, 0F, 0F, 1.0F );

        GL11.glColor4f(((color >> 16) & 0xff) / 255F, ((color >> 8) & 0xff) / 255F, (color & 0xff) / 255F, ((color >> 24) & 0xff) / 255F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glBegin(GL11.GL_TRIANGLES);

        GL11.glVertex2d(0, 0 + 6);
        GL11.glVertex2d(0 + 3, 0 - 2);
        GL11.glVertex2d(0 - 3, 0 - 2);

        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glRotatef(e.rotationYaw, 0F, 0F, 1.0F);

        GL11.glPopMatrix();
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
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GL11.glLineWidth(thickness);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        RenderUtil.bufferbuilder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        RenderUtil.bufferbuilder.pos(x, y, 0.0D).color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, (color >> 24 & 0xFF) / 255.0F).endVertex();
        RenderUtil.bufferbuilder.pos(x1, y1, 0.0D).color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, (color >> 24 & 0xFF) / 255.0F).endVertex();
        RenderUtil.tessellator.draw();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
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
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glVertex2f(pickerX, pickerY);
        GL11.glVertex2f(pickerX, pickerY + pickerHeight);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glVertex2f(pickerX + pickerWidth, pickerY + pickerHeight);
        GL11.glVertex2f(pickerX + pickerWidth, pickerY);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glVertex2f(pickerX, pickerY);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glVertex2f(pickerX, pickerY + pickerHeight);
        GL11.glVertex2f(pickerX + pickerWidth, pickerY + pickerHeight);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glVertex2f(pickerX + pickerWidth, pickerY);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void gradient(int minX, int minY, int maxX, int maxY, int startColor, int endColor, boolean left) {
        if (left) {
            float startA = (startColor >> 24 & 0xFF) / 255.0f;
            float startR = (startColor >> 16 & 0xFF) / 255.0f;
            float startG = (startColor >> 8 & 0xFF) / 255.0f;
            float startB = (startColor & 0xFF) / 255.0f;
            float endA = (endColor >> 24 & 0xFF) / 255.0f;
            float endR = (endColor >> 16 & 0xFF) / 255.0f;
            float endG = (endColor >> 8 & 0xFF) / 255.0f;
            float endB = (endColor & 0xFF) / 255.0f;

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glBegin(GL11.GL_POLYGON);
            GL11.glColor4f(startR, startG, startB, startA);
            GL11.glVertex2f(minX, minY);
            GL11.glVertex2f(minX, maxY);
            GL11.glColor4f(endR, endG, endB, endA);
            GL11.glVertex2f(maxX, maxY);
            GL11.glVertex2f(maxX, minY);
            GL11.glEnd();
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
        } else {
            drawGradientRect(minX, minY, maxX, maxY, startColor, endColor);
        }
    }

    public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);

        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, top, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, bottom, 0).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(right, bottom, 0).color(f5, f6, f7, f4).endVertex();

        tessellator.draw();

        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawLeftGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);

        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 0).color(f4, f5, f6, f4).endVertex();
        bufferbuilder.pos(left, top, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, bottom, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(right, bottom, 0).color(f4, f5, f6, f4).endVertex();

        tessellator.draw();

        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}
package me.linus.momentum.util.render;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.color.ColorUtil;
import me.linus.momentum.util.combat.EnemyUtil;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.EXTPackedDepthStencil;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author unknown, but many other clients use this for outline
 * @since 11/26/2020
 */

public class ESPUtil implements MixinInterface {

    public static void renderOne(float width) {
        checkSetupFBO();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(width);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glClearStencil(0xF);
        GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xF);
        GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
    }

    public static void renderTwo() {
        GL11.glStencilFunc(GL11.GL_NEVER, 0, 0xF);
        GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
    }

    public static void renderThree() {
        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
    }

    public static void renderFour() {
        setColor(new Color(255, 255, 255));
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE);
        GL11.glPolygonOffset(1.0F, -2000000F);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
    }

    public static void renderFive() {
        GL11.glPolygonOffset(1.0F, 2000000F);
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_LINE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glPopAttrib();
    }
    
    public static void draw2D(Entity drawEntity) {
        GL11.glBegin(2);
        GL11.glVertex2d(-drawEntity.width, 0);
        GL11.glVertex2d(-drawEntity.width, drawEntity.height);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex2d(-drawEntity.width, 0);
        GL11.glVertex2d(drawEntity.width, 0);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex2d(drawEntity.width, 0);
        GL11.glVertex2d(drawEntity.width, drawEntity.height);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex2d(-drawEntity.width, drawEntity.height);
        GL11.glVertex2d(drawEntity.width, drawEntity.height);
        GL11.glEnd();
        if (drawEntity instanceof EntityPlayer) {
            GL11.glBegin(GL11.GL_POLYGON);
            setColor(ColorUtil.getHealthColor(EnemyUtil.getHealth((EntityPlayer) drawEntity)));
            GL11.glVertex2d(-drawEntity.width - 0.05, 0);
            GL11.glVertex2d(-drawEntity.width - 0.075, 0);
            GL11.glVertex2d(-drawEntity.width - 0.075, (drawEntity.height / 20) * ((EntityPlayer) drawEntity).getHealth());
            GL11.glVertex2d(-drawEntity.width - 0.05, (drawEntity.height / 20) * ((EntityPlayer) drawEntity).getHealth());
            GL11.glEnd();

            GL11.glBegin(GL11.GL_POLYGON);
            setColor(new Color(232, 188, 65));
            GL11.glVertex2d(-drawEntity.width - 0.1, 0);
            GL11.glVertex2d(-drawEntity.width - 0.125, 0);
            GL11.glVertex2d(-drawEntity.width - 0.125, (drawEntity.height / 16) * ((EntityPlayer) drawEntity).getAbsorptionAmount());
            GL11.glVertex2d(-drawEntity.width - 0.1, (drawEntity.height / 16) * ((EntityPlayer) drawEntity).getAbsorptionAmount());
            GL11.glEnd();
        }
    }

    public static void setColor(Color c) {
        GL11.glColor4d(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
    }

    public static void checkSetupFBO() {
        Framebuffer fbo = mc.getFramebuffer();

        if (fbo != null) {
            if (fbo.depthBuffer > -1) {
                setupFBO(fbo);
                fbo.depthBuffer = -1;
            }
        }
    }

    public static void setupFBO(Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
        int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
        EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, EXTPackedDepthStencil.GL_DEPTH_STENCIL_EXT, mc.displayWidth, mc.displayHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
    }
}

package me.linus.momentum.util.render;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.combat.EnemyUtil;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.EXTPackedDepthStencil;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class ESPUtil implements MixinInterface {

    /**
     * outline rendering
     */

    public static void renderOne(float width) {
        checkSetupFBO();
        glPushAttrib(GL_ALL_ATTRIB_BITS);
        glDisable(GL_ALPHA_TEST);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glLineWidth(width);
        glEnable(GL_LINE_SMOOTH);
        glEnable(GL_STENCIL_TEST);
        glClear(GL_STENCIL_BUFFER_BIT);
        glClearStencil(0xF);
        glStencilFunc(GL_NEVER, 1, 0xF);
        glStencilOp(GL_REPLACE, GL_REPLACE, GL_REPLACE);
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    }

    public static void renderTwo() {
        glStencilFunc(GL_NEVER, 0, 0xF);
        glStencilOp(GL_REPLACE, GL_REPLACE, GL_REPLACE);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }

    public static void renderThree() {
        glStencilFunc(GL_EQUAL, 1, 0xF);
        glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    }

    public static void renderFour() {
        setColor(new Color(255, 255, 255));
        glDepthMask(false);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_POLYGON_OFFSET_LINE);
        glPolygonOffset(1.0F, -2000000F);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
    }

    public static void renderFive() {
        glPolygonOffset(1.0F, 2000000F);
        glDisable(GL_POLYGON_OFFSET_LINE);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDisable(GL_STENCIL_TEST);
        glDisable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        glEnable(GL_BLEND);
        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_ALPHA_TEST);
        glPopAttrib();
    }

    /**
     * flat 2D rendering
     */
    
    public static void draw2D(Entity drawEntity) {
        glBegin(2);
        glVertex2d(-drawEntity.width, 0);
        glVertex2d(-drawEntity.width, drawEntity.height);
        glEnd();
        glBegin(2);
        glVertex2d(-drawEntity.width, 0);
        glVertex2d(drawEntity.width, 0);
        glEnd();
        glBegin(2);
        glVertex2d(drawEntity.width, 0);
        glVertex2d(drawEntity.width, drawEntity.height);
        glEnd();
        glBegin(2);
        glVertex2d(-drawEntity.width, drawEntity.height);
        glVertex2d(drawEntity.width, drawEntity.height);
        glEnd();
        if (drawEntity instanceof EntityPlayer) {
            glBegin(GL_POLYGON);
            setColor(getHealthColor(EnemyUtil.getHealth((EntityPlayer) drawEntity)));
            glVertex2d(-drawEntity.width - 0.05, 0);
            glVertex2d(-drawEntity.width - 0.075, 0);
            glVertex2d(-drawEntity.width - 0.075, (drawEntity.height / 20) * ((EntityPlayer) drawEntity).getHealth());
            glVertex2d(-drawEntity.width - 0.05, (drawEntity.height / 20) * ((EntityPlayer) drawEntity).getHealth());
            glEnd();

            glBegin(GL_POLYGON);
            setColor(new Color(232, 188, 65));
            glVertex2d(-drawEntity.width - 0.1, 0);
            glVertex2d(-drawEntity.width - 0.125, 0);
            glVertex2d(-drawEntity.width - 0.125, (drawEntity.height / 16) * ((EntityPlayer) drawEntity).getAbsorptionAmount());
            glVertex2d(-drawEntity.width - 0.1, (drawEntity.height / 16) * ((EntityPlayer) drawEntity).getAbsorptionAmount());
            glEnd();
        }
    }

    /**
     * color rendering
     */

    public static void setColor(Color c) {
        glColor4d(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
    }

    /**
     * FBO checks
     */

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

    public static Color getHealthColor(float health) {
        if (health <= 4)
            return new Color(200, 0, 0);
        else if (health <= 8)
            return new Color(231, 143, 85);
        else if (health <= 12)
            return new Color(219, 201, 106);
        else if (health <= 16)
            return new Color(117, 231, 85);
        else
            return new Color(44, 186, 19);
    }
}

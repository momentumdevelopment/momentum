package me.linus.momentum.util.render;

import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

/**
 * @author linustouchtips
 * @since 01/12/2021
 */

public class Render2DUtil implements MixinInterface {

    public static void drawCircle(int x, int y, double radius, int color) {
        GL11.glEnable(3042 /* GL_BLEND */);
        GL11.glDisable(3553 /* GL_TEXTURE_2D */);
        GL11.glEnable(2848 /* GL_LINE_SMOOTH */);
        GL11.glBlendFunc(770, 771 );
        GL11.glColor4f(((color >> 16) & 0xff) / 255F, ((color >> 8) & 0xff) / 255F, (color & 0xff) / 255F, ((color >> 24) & 0xff) / 255F);
        GL11.glBegin( 2 /* GL_TRIANGLE_LOOP */);

        for (int i = 0; i <= 360; i++)
            GL11.glVertex2d( x + Math.sin(((i * 3.141526D) / 180)) * radius, y + Math.cos(((i * 3.141526D) / 180)) * radius);

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
            GL11.glVertex2d( x + Math.sin(((i * 3.141526D) / 180)) * radius, y + Math.cos(((i * 3.141526D) / 180)) * radius);

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
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc( 770, 771 );
        GL11.glBegin(GL11.GL_TRIANGLES);

        GL11.glVertex2d(0, 0 + 6);
        GL11.glVertex2d(0 + 3, 0 - 2);
        GL11.glVertex2d(0 - 3, 0 - 2);

        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glRotatef(e.rotationYaw, 0F, 0F, 1.0F);

        GL11.glPopMatrix( );
    }
}

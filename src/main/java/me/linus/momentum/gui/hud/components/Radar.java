package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.util.client.ColorUtil;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.render.Render2DUtil;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 01/12/2021
 */

public class Radar extends HUDComponent {
    public Radar() {
        super("Radar", 300, 200);
        width = 50;
        height = 50;
    }

    @Override
    public void renderComponent() {
        Render2DUtil.drawFilledCircle(this.x, this.y, 50, new Color(100, 100, 100, 90).getRGB());
        Render2DUtil.drawFilledCircle(this.x, this.y, 2, -1);

        mc.world.loadedEntityList.stream().filter(entity -> mc.player != entity).forEach(entity -> {
            double xDistance = mc.player.posX - entity.posX;
            double zDistance = mc.player.posZ - entity.posZ;
            double totalDistance = Math.sqrt((MathUtil.square(xDistance)) + (MathUtil.square(zDistance)));

            double angleDifference = MathHelper.wrapDegrees(mc.player.rotationYaw - ((Math.atan2(zDistance, xDistance) * 180.0D) / Math.PI));
            double finalX = Math.cos(Math.toRadians(angleDifference)) * totalDistance;
            double finalY = -Math.sin(Math.toRadians(angleDifference)) * totalDistance;

            GL11.glPushMatrix();
            GL11.glTranslatef(this.x, this.y, 0);

            if (totalDistance <= 100)
                Render2DUtil.drawTriangle(entity, finalX, finalY, ColorUtil.getEntityColor(entity).getRGB());

            GL11.glPopMatrix();
        });
    }

    @Override
    public boolean getBackground() {
        return false;
    }
}

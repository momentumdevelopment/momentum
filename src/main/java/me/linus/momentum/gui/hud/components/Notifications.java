package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.main.hud.HUD;
import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.module.modules.client.HUDEditor;
import me.linus.momentum.util.render.AnimationUtil;
import me.linus.momentum.util.client.notification.Notification;
import me.linus.momentum.util.client.notification.NotificationManager;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Iterator;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Notifications extends HUDComponent {
    public Notifications() {
        super("Notifications", 450, 400);
        this.toggle();
    }

    private static final ResourceLocation widget = new ResourceLocation("momentum:widget_icon.png");
    private static final ResourceLocation totem = new ResourceLocation("momentum:totem_icon.png");

    @Override
    public void renderComponent() {
        if (mc.currentScreen instanceof HUD) {
            if (NotificationManager.notifications.isEmpty()) {
                width = (int) FontUtil.getStringWidth("This is an example notification!");
                height = mc.fontRenderer.FONT_HEIGHT;

                GuiScreen.drawRect(this.x - 22, this.y - 5, (int) (this.x + FontUtil.getStringWidth("This is an example notification!") + 3), this.y + mc.fontRenderer.FONT_HEIGHT + 5, new Color(0, 0, 0, 70).getRGB());

                GlStateManager.enableAlpha();
                mc.getTextureManager().bindTexture(widget);
                GlStateManager.color(1, 1, 1, 1);
                GL11.glPushMatrix();
                GuiScreen.drawScaledCustomSizeModalRect(this.x - 20, this.y - 4, 0,0,512,512,16,16,512,512);
                GL11.glPopMatrix();
                GlStateManager.disableAlpha();
                GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);

                FontUtil.drawString("This is an example notification!", this.x, this.y - 1, HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : -1);
                return;
            }
        }

        Iterator<Notification> notifications = NotificationManager.notifications.iterator();
        float maxWidth = 0f;

        while (notifications.hasNext()) {
            Notification continuedNotification = notifications.next();

            if (continuedNotification.isComplete())
                NotificationManager.notifications.remove(continuedNotification);

            float width = FontUtil.getStringWidth(continuedNotification.getMessage());
            int currY = this.y;

            if (continuedNotification.remainingAnimation < width && !continuedNotification.isComplete())
                continuedNotification.remainingAnimation = AnimationUtil.moveTowards(continuedNotification.remainingAnimation, width + 1, (float) (0.01f + ClickGUI.speed.getValue() / 30), 0.1f);

            else if (continuedNotification.remainingAnimation > 1.5f && continuedNotification.isComplete())
                continuedNotification.remainingAnimation = AnimationUtil.moveTowards(continuedNotification.remainingAnimation, -1.5f, (float) (0.01f + ClickGUI.speed.getValue() / 30), 0.1f);

            else if (continuedNotification.remainingAnimation <= 1.5f && continuedNotification.isComplete())
                continuedNotification.remainingAnimation = -1f;

            if (continuedNotification.remainingAnimation > width && !continuedNotification.isComplete())
                continuedNotification.remainingAnimation = width;

            GuiScreen.drawRect((int) (this.x - continuedNotification.remainingAnimation) + 63, currY - 5 - (13 * NotificationManager.notifications.size()), (int) (this.x + width + 111 - continuedNotification.remainingAnimation), currY + mc.fontRenderer.FONT_HEIGHT + 5  - (13 * NotificationManager.notifications.size()), new Color(0, 0, 0, 70).getRGB());

            GlStateManager.pushMatrix();
            GlStateManager.enableAlpha();
            mc.getTextureManager().bindTexture(widget);
            GlStateManager.color(1, 1, 1, 1);
            GuiScreen.drawScaledCustomSizeModalRect((int) (this.x - continuedNotification.remainingAnimation) + 65, this.y - 4 - (13 * NotificationManager.notifications.size()), 0,0,512,512,16,16,512,512);
            GlStateManager.disableAlpha();
            GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
            GlStateManager.popMatrix();

            FontUtil.drawString(continuedNotification.getMessage(), this.x + 98 - continuedNotification.remainingAnimation, currY - (13 * NotificationManager.notifications.size()), HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : -1);

            currY -= 13;

            if (width > maxWidth)
                maxWidth = width;
        }

        height = mc.fontRenderer.FONT_HEIGHT;
        width = (int) maxWidth;
    }

    @Override
    public boolean getBackground() {
        return false;
    }
}

package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.main.HUD;
import me.linus.momentum.module.modules.client.ClickGui;
import me.linus.momentum.module.modules.client.Colors;
import me.linus.momentum.module.modules.hud.NotificationModule;
import me.linus.momentum.util.client.Animation2D;
import me.linus.momentum.util.client.notification.Notification;
import me.linus.momentum.util.client.notification.NotificationManager;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Iterator;

public class Notifications extends HUDComponent<NotificationModule> {
    public Notifications() {
        super("Notifications", 400, 400, NotificationModule.INSTANCE);
        this.toggle();
    }

    private static final ResourceLocation widget = new ResourceLocation("momentum:widget_icon.png");
    private static final ResourceLocation totem = new ResourceLocation("momentum:totem_icon.png");

    @Override
    public void render() {
        if (mc.currentScreen instanceof HUD) {
            if (NotificationManager.notifications.isEmpty()) {
                width = (int) FontUtil.getStringWidth("This is an example notification!");
                height = mc.fontRenderer.FONT_HEIGHT;

                GuiScreen.drawRect(Momentum.componentManager.getComponentByName("Notifications").getX() - 22, Momentum.componentManager.getComponentByName("Notifications").getY() - 5, (int) (Momentum.componentManager.getComponentByName("Notifications").getX() + FontUtil.getStringWidth("This is an example notification!") + 3), Momentum.componentManager.getComponentByName("Notifications").getY() + mc.fontRenderer.FONT_HEIGHT + 5, new Color(0, 0, 0, 70).getRGB());

                GlStateManager.enableAlpha();
                mc.getTextureManager().bindTexture(widget);
                GlStateManager.color((float) Colors.r.getValue(), (float) Colors.g.getValue(), (float) Colors.b.getValue(), 1);
                GL11.glPushMatrix();
                GuiScreen.drawScaledCustomSizeModalRect(Momentum.componentManager.getComponentByName("Notifications").getX() - 20, Momentum.componentManager.getComponentByName("Notifications").getY() - 4, 0,0,512,512,16,16,512,512);
                GL11.glPopMatrix();
                GlStateManager.disableAlpha();
                GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);

                FontUtil.drawStringWithShadow("This is an example notification!", Momentum.componentManager.getComponentByName("Notifications").getX(), Momentum.componentManager.getComponentByName("Notifications").getY() - 1, new Color(255, 255, 255).getRGB());
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
            int currY = Momentum.componentManager.getComponentByName("Notifications").getY();

            if (continuedNotification.remainingAnimation < width && !continuedNotification.isComplete())
                continuedNotification.remainingAnimation = Animation2D.moveTowards(continuedNotification.remainingAnimation, width + 1, (float) (0.01f + ClickGui.speed.getValue() / 30), 0.1f);

            else if (continuedNotification.remainingAnimation > 1.5f && continuedNotification.isComplete())
                continuedNotification.remainingAnimation = Animation2D.moveTowards(continuedNotification.remainingAnimation, -1.5f, (float) (0.01f + ClickGui.speed.getValue() / 30), 0.1f);

            else if (continuedNotification.remainingAnimation <= 1.5f && continuedNotification.isComplete())
                continuedNotification.remainingAnimation = -1f;

            if (continuedNotification.remainingAnimation > width && !continuedNotification.isComplete())
                continuedNotification.remainingAnimation = width;
            
            GuiScreen.drawRect((int) (Momentum.componentManager.getComponentByName("Notifications").getX() - continuedNotification.remainingAnimation) + 63, currY - 5 - (13 * NotificationManager.notifications.size()), (int) (Momentum.componentManager.getComponentByName("Notifications").getX() + width + 111 - continuedNotification.remainingAnimation), currY + mc.fontRenderer.FONT_HEIGHT + 5  - (13 * NotificationManager.notifications.size()), new Color(0, 0, 0, 70).getRGB());

            GlStateManager.enableAlpha();
            mc.getTextureManager().bindTexture(widget);
            GlStateManager.color((float) Colors.r.getValue(), (float) Colors.g.getValue(), (float) Colors.b.getValue(), 1);
            GL11.glPushMatrix();
            GuiScreen.drawScaledCustomSizeModalRect((int) (Momentum.componentManager.getComponentByName("Notifications").getX() - continuedNotification.remainingAnimation) + 65, Momentum.componentManager.getComponentByName("Notifications").getY() - 4 - (13 * NotificationManager.notifications.size()), 0,0,512,512,16,16,512,512);
            GL11.glPopMatrix();
            GlStateManager.disableAlpha();
            GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);

            FontUtil.drawStringWithShadow(continuedNotification.getMessage(), Momentum.componentManager.getComponentByName("Notifications").getX() + 98 - continuedNotification.remainingAnimation, currY - (13 * NotificationManager.notifications.size()), new Color(255, 255, 255).getRGB());

            currY -= 13;

            if (width > maxWidth)
                maxWidth = width;
        }

        height = mc.fontRenderer.FONT_HEIGHT;
        width = (int) maxWidth;
    }
}

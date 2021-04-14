package me.linus.momentum.gui.hud.element.elements;

import me.linus.momentum.gui.hud.element.AnchorPoint;
import me.linus.momentum.gui.main.HUDScreen;
import me.linus.momentum.managers.notification.Notification;
import me.linus.momentum.managers.notification.NotificationManager;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.gui.hud.element.HUDElement;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.render.AnimationUtil;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Notifications extends HUDElement {
    public Notifications() {
        super("Notifications", 43, 360, Category.MISC, AnchorPoint.None);
        this.toggleElement();
    }

    public static Slider stayTime = new Slider("Stay Time", 0.0D, 2.5D, 10.0D, 1);

    @Override
    public void setup() {
        addSetting(stayTime);
        
        removeBackground();
    }

    ResourceLocation infoIcon = new ResourceLocation("momentum:info-icon.png");
    ResourceLocation warningIcon = new ResourceLocation("momentum:warning-icon.png");

    int count;

    @Override
    public void renderElement() {
        count = 0;
        if (mc.currentScreen instanceof HUDScreen) {
            GuiScreen.drawRect(this.x - 33, this.y - 18, (int) (this.x + FontUtil.getStringWidth("This is an example notification!")) + 2, (int) (this.y + FontUtil.getFontHeight()) - (count * 37) + 5, new Color(0, 0, 0, 120).getRGB());
            GuiScreen.drawRect(this.x - 33, (int) (this.y + FontUtil.getFontHeight()) - (count * 37) + 3, (int) (this.x + FontUtil.getStringWidth("This is an example notification!")) + 2, (int) (this.y + FontUtil.getFontHeight()) - (count * 37) + 5, Notification.Type.Info.getColor());
            FontUtil.drawString("Info", this.x, this.y - (count * 37) - 14, -1);
            FontUtil.drawString("This is an example notification!", this.x, this.y - (count * 37) - 1, -1);

            GlStateManager.enableAlpha();
            mc.getTextureManager().bindTexture(infoIcon);
            GlStateManager.color(0, 0.768f, 1, 0.47f);
            GL11.glPushMatrix();
            GuiScreen.drawScaledCustomSizeModalRect(this.x - 31, (int) (this.y + FontUtil.getFontHeight()) - (count * 37) - 27, 0, 0, 256,256,26,26,256,256);
            GL11.glPopMatrix();
            GlStateManager.disableAlpha();
            GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);

            width = (int) FontUtil.getStringWidth("This is an example notification!");
            height = (int) FontUtil.getFontHeight();
        }

        else {
            NotificationManager.notifications.forEach(notification -> {
                if (this.x > this.x - FontUtil.getStringWidth(notification.getMessage()))
                    AnimationUtil.moveTowards(this.x, this.x - FontUtil.getStringWidth(notification.getMessage()), (float) (0.01f + 3.5 / 30), 0.1f);

                GuiScreen.drawRect(this.x - 33, this.y - 18 - (count * 37), (int) (this.x + FontUtil.getStringWidth(notification.getMessage())) + 2, (int) (this.y + FontUtil.getFontHeight()) - (count * 37) + 5, new Color(0, 0, 0, 120).getRGB());
                GuiScreen.drawRect(this.x - 33, (int) (this.y + FontUtil.getFontHeight()) - (count * 37) + 3, (int) (this.x + ((FontUtil.getStringWidth(notification.getMessage()) / (stayTime.getValue() * 1000)) * (int) notification.stayTimer.getMS(System.nanoTime() - notification.stayTimer.time))), (int) (this.y + FontUtil.getFontHeight()) - (count * 37) + 5, notification.type.getColor());
                FontUtil.drawString(notification.type.name(), this.x, this.y - (count * 37) - 14, -1);
                FontUtil.drawString(notification.getMessage(), this.x, this.y - (count * 37) - 1, -1);

                switch (notification.type) {
                    case Info:
                        GlStateManager.enableAlpha();
                        mc.getTextureManager().bindTexture(infoIcon);
                        GlStateManager.color(0, 0.768f, 1, 0.47f);
                        GL11.glPushMatrix();
                        GuiScreen.drawScaledCustomSizeModalRect(this.x - 31, (int) (this.y + FontUtil.getFontHeight()) - (count * 37) - 27, 0, 0, 256,256,26,26,256,256);
                        GL11.glPopMatrix();
                        GlStateManager.disableAlpha();
                        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
                        break;
                    case Warning:
                        GlStateManager.enableAlpha();
                        mc.getTextureManager().bindTexture(warningIcon);
                        GlStateManager.color(1, 1, 1, 0.47f);
                        GL11.glPushMatrix();
                        GuiScreen.drawScaledCustomSizeModalRect(this.x - 33, (int) (this.y + FontUtil.getFontHeight()) - (count * 37) - 28, 0, 0, 256,256,28,28,256,256);
                        GL11.glPopMatrix();
                        GlStateManager.disableAlpha();
                        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
                        break;
                }

                NotificationManager.notifications.removeIf(Notification::isComplete);

                count++;
            });
        }
    }
}

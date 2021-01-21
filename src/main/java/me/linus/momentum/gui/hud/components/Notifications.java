package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.main.hud.HUD;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.util.render.AnimationUtil;
import me.linus.momentum.util.client.notification.NotificationManager;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Notifications extends HUDComponent {
    public Notifications() {
        super("Notifications", 450, 400);
        this.toggle();
    }

    int count;

    @Override
    public void renderComponent() {
        count = 0;
        if (mc.currentScreen instanceof HUD) {
            GuiScreen.drawRect(this.x - 2, this.y - 2, (int) (this.x + FontUtil.getStringWidth("This is an example notification!")), (int) (this.y + FontUtil.getFontHeight()), new Color(0, 0, 0, 120).getRGB());
            FontUtil.drawString("This is an example notification!", this.x, this.y, -1);
            width = (int) FontUtil.getStringWidth("This is an example notification!");
            height = (int) FontUtil.getFontHeight();
        }

        else {
            NotificationManager.getNotification().stream().forEach(notification -> {
                NotificationManager.getNotification().removeIf(continuedNotification -> continuedNotification.isComplete());

                if (notification.remainingAnimation < FontUtil.getStringWidth(notification.getMessage()) && !notification.isComplete())
                    notification.remainingAnimation = AnimationUtil.moveTowards(notification.remainingAnimation, FontUtil.getStringWidth(notification.getMessage()) + 1, (float) (0.01f + ClickGUI.speed.getValue() / 30), 0.1f);

                else if (notification.remainingAnimation > 1.5f && notification.isComplete())
                    notification.remainingAnimation = AnimationUtil.moveTowards(notification.remainingAnimation, -1.5f, (float) (0.01f + ClickGUI.speed.getValue() / 30), 0.1f);

                else if (notification.remainingAnimation <= 1.5f && notification.isComplete())
                    notification.remainingAnimation = -1f;

                if (notification.remainingAnimation > FontUtil.getStringWidth(notification.getMessage()) && !notification.isComplete())
                    notification.remainingAnimation = FontUtil.getStringWidth(notification.getMessage());

                GuiScreen.drawRect(this.x - 2, this.y - 2 - (count * 14), (int) (this.x + FontUtil.getStringWidth(notification.getMessage())), (int) (this.y + FontUtil.getFontHeight()) - (count * 14), new Color(0, 0, 0, 120).getRGB());
                FontUtil.drawString(notification.getMessage(), this.x, this.y - (count * 14), -1);

                count++;
            });
        }
    }

    @Override
    public boolean getBackground() {
        return false;
    }
}

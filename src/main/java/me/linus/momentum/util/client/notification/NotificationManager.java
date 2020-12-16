package me.linus.momentum.util.client.notification;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.Module;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author linustouchtips
 * @since 12/06/2020
 */

public class NotificationManager implements MixinInterface {

    public static List<Notification> notifications = new CopyOnWriteArrayList<>();

    public static void sendNotification(Module module, String message) {
        float width = FontUtil.getStringWidth(message);
        int x = new ScaledResolution(mc).getScaledWidth();
        int y = new ScaledResolution(mc).getScaledHeight();

        FontUtil.drawStringWithShadow(message, x - width - 10, y - 20, new Color(255, 255, 255).getRGB());
        GuiScreen.drawRect((int) (x - width - 12), y - 19 + mc.fontRenderer.FONT_HEIGHT, x - 10, y - 19, new Color(0, 0, 0, 70).getRGB());
    }
}

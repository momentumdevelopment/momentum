package me.linus.momentum.util.client.notification;

import me.linus.momentum.mixin.MixinInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips
 * @since 12/06/2020
 */

public class NotificationManager implements MixinInterface {

    public static List<Notification> notifications = new ArrayList<>();

    public static void addNotification(Notification notification) {
        if (notifications.size() < 3)
            notifications.add(notification);
    }
}

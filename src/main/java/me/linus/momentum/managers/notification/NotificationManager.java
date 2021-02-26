package me.linus.momentum.managers.notification;

import me.linus.momentum.mixin.MixinInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips
 * @since 12/06/2020
 */

public class NotificationManager implements MixinInterface {

    public static List<Notification> notifications = new ArrayList<>();

    public static void addNotification(Notification queueNotification) {
        boolean duplicate = false;

        for (Notification notification : notifications) {
            if (notification.getMessage().equals(queueNotification.getMessage()))
                duplicate = true;
        }

        if (!duplicate)
            notifications.add(queueNotification);
    }
}

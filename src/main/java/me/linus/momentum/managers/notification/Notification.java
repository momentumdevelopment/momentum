package me.linus.momentum.managers.notification;

import me.linus.momentum.gui.hud.components.Notifications;
import me.linus.momentum.util.world.Timer;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/07/2020
 */

public class Notification {

    public String message;
    public Timer stayTimer = new Timer();
    public Type type;

    public Notification(String message, Type type) {
        this.message = message;
        this.type = type;

        stayTimer.reset();
    }

    public boolean isComplete() {
        return stayTimer.passed((long) (Notifications.stayTime.getValue() * 1000), Timer.Format.System);
    }

    public String getMessage() {
        return this.message;
    }

    public enum Type {
        Info((new Color(0, 196, 255, 120)).getRGB()),
        Warning((new Color(255, 174, 2, 120)).getRGB()),
        Tip((new Color(255, 255, 255, 120)).getRGB());

        int color;

        Type(int color) {
            this.color = color;
        }

        public int getColor() {
            return this.color;
        }
    }
}

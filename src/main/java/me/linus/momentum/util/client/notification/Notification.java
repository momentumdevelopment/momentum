package me.linus.momentum.util.client.notification;

import me.linus.momentum.util.world.Timer;

/**
 * @author linustouchtips
 * @since 12/07/2020
 */

public class Notification {

    public String message;
    public float remainingAnimation = -1.0f;

    Timer beginTimer = new Timer();
    Timer completionTimer = new Timer();

    public Notification(String message) {
        this.message = message;

        completionTimer.reset();
        beginTimer.reset();
    }

    public boolean isComplete() {
        return completionTimer.passed(2500, Timer.Format.System);
    }

    public String getMessage() {
        return this.message;
    }
}

package me.linus.momentum.util.client.notification;

import me.linus.momentum.util.world.Timer;

/**
 * @author linustouchtips
 * @since 12/07/2020
 */

public class Notification {

    private String message;
    public float remainingAnimation = -1.0f;

    Timer completionTimer = new Timer();

    public Notification(String message) {
        this.message = message;

        completionTimer.reset();
    }

    public boolean isComplete() {
        return completionTimer.passed(2500, Timer.Format.System);
    }

    public String getMessage() {
        return this.message;
    }
}

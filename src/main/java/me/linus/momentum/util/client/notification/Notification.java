package me.linus.momentum.util.client.notification;

import me.linus.momentum.util.world.Timer;

/**
 * @author linustouchtips
 * @since 12/07/2020
 */

public class Notification {

    private final String message;
    private final long completionTime;
    public float remainingAnimation = -1.0f;

    Timer completionTimer = new Timer();

    public Notification(String message) {
        this.message = message;
        this.completionTime = 2500;

        completionTimer.reset();
    }

    public boolean isComplete() {
        return completionTimer.passed(completionTime, Timer.Format.System);
    }

    public String getMessage() {
        return this.message;
    }
}

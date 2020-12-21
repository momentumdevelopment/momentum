package me.linus.momentum.util.client.notification;

import me.linus.momentum.util.client.system.Timer;

/**
 * @author linustouchtips
 * @since 12/07/2020
 */

public class Notification {

    private final String message;
    private final long completionTime;
    public float remainingAnimation = -1.0f;

    Timer beginTimer = new Timer();
    Timer completionTimer = new Timer();

    public Notification(String message) {
        this.message = message;
        this.completionTime = 2500;

        completionTimer.reset();
        beginTimer.reset();
    }

    public boolean isComplete() {
        return completionTimer.passed(completionTime);
    }

    public String getMessage() {
        return this.message;
    }
}

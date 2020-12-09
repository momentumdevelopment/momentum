package me.linus.momentum.util.client.notification;

import me.linus.momentum.util.client.Timer;

/**
 * @author linustouchtips
 * @since 12/07/2020
 */

public class Notification {

    private String name;
    private String message;
    private int x;
    private int y;
    private long completionTime;
    public float remainingAnimation = -1.0f;

    Timer beginTimer = new Timer();
    Timer completionTimer = new Timer();

    public Notification(String name, String message) {
        this.name = name;
        this.message = message;
        this.completionTime = 2500;

        completionTimer.reset();
        beginTimer.reset();
    }

    public boolean isComplete() {
        return completionTimer.passed(completionTime);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public String getName() {
        return this.name;
    }

    public String getMessage() {
        return this.message;
    }
}

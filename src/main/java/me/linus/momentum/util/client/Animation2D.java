package me.linus.momentum.util.client;

public class Animation2D {

    public static float moveTowards(float current, float end, float minSpeed) {
        float defaultSpeed = 0.125f;
        return moveTowards(current, end, defaultSpeed, minSpeed);
    }

    public static float moveTowards(float current, float end, float smoothSpeed, float minSpeed) {
        float movement = (end - current) * smoothSpeed;

        if (movement > 0) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        }
        else if (movement < 0) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }

        return current + movement;
    }
}

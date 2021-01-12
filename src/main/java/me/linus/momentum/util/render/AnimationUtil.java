package me.linus.momentum.util.render;

/**
 * @author https://github.com/Memeszz/Aurora-public/blob/master/src/main/java/me/memeszz/aurora/util/render/Animation.java
 * @since 12/17/2020
 */

public class AnimationUtil {

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

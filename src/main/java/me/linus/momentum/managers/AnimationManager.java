package me.linus.momentum.managers;

import me.linus.momentum.util.world.Timer;

/**
 * @author linustouchtips
 * @since 01/31/2020
 */

public class AnimationManager {

    public State state = State.Static;
    public Timer animationTimer = new Timer();
    public long animationFactor = 0;

    public double getRemainingAnimation() {
        if (this.state == State.Opening)
            return (System.currentTimeMillis() - animationFactor) / (double) 2000;

        if (this.state == State.Closing)
            return ((long) 2000 - (System.currentTimeMillis() - animationFactor)) / (double) 2000;
        else
            return 1;
    }

    public void updateState() {
        if (animationTimer.passed(2000, Timer.Format.System))
            this.state = State.Static;
    }

    public void setState(State state) {
        this.state = state;

        animationFactor = System.currentTimeMillis();
    }

    public enum State {
        Opening,
        Closing,
        Static
    }
}

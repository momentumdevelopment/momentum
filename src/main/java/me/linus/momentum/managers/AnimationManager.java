package me.linus.momentum.managers;

import me.linus.momentum.mixin.MixinInterface;

/**
 * @author linustouchtips
 * @since 01/31/2021
 */

public class AnimationManager implements MixinInterface {

    public int time;
    public boolean initialState;
    public State previousState = State.Static;
    public State currentState = State.Static;
    public long currentStateStart = 0;

    public AnimationManager(int time, boolean initialState) {
        this.time = time;
        this.initialState = initialState;

        if (initialState)
            previousState = State.Expanding;
    }

    public void tick() {
        if (currentState != State.Static) {
            if (System.currentTimeMillis() - currentStateStart > time) {
                previousState = currentState;
                currentState = State.Static;

                if (previousState == State.Retracting)
                    initialState = false;
            }
        }
    }

    public double getAnimationFactor() {
        if (currentState == State.Expanding)
            return (System.currentTimeMillis() - currentStateStart) / (double) time;
        if (currentState == State.Retracting)
            return ((long) time - (System.currentTimeMillis() - currentStateStart)) / (double) time;

        return previousState == State.Expanding ? 1 : 0;
    }

    public boolean getState() {
        return initialState;
    }

    public void setState(boolean value) {
        if (value) {
            currentState = State.Expanding;
            initialState = true;
        } else
            currentState = State.Retracting;

        currentStateStart = System.currentTimeMillis();
    }


    public void setStateHard(boolean value) {
        if (value) {
            currentState = State.Expanding;
            initialState = true;
            currentStateStart = System.currentTimeMillis();
        }

        else {
            previousState = State.Retracting;
            currentState = State.Static;
            initialState = false;
        }
    }

    public void toggle() {
        setState(!getState());
    }

    public enum State {
        Expanding, Retracting, Static
    }
}

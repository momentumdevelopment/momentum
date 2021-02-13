package me.linus.momentum.managers;

import me.linus.momentum.mixin.MixinInterface;

/**
 * @author linustouchtips
 * @since 01/31/2021
 */

public class AnimationManager implements MixinInterface {
    
    public int length;
    public long animationStart = 0;
    public double animationFactor = 0;
    
    public AnimationManager(int length) {
        this.length = length;
    }

    public void updateTime() {
        animationFactor = System.currentTimeMillis() - animationStart < length ? System.currentTimeMillis() - animationStart / (double) length : 1;
    }
    
    public void updateState() {
        animationStart = 0;
    }
    
    public double getAnimationFactor() {
        return animationFactor;
    }
}

package me.linus.momentum.managers;

import me.linus.momentum.mixin.MixinInterface;

/**
 * @author linustouchtips
 * @since 01/31/2020
 */

public class AnimationManager implements MixinInterface {
    
    private final int length;
    private boolean state;
    private Animation prevAnimation = Animation.Static;
    private Animation animation = Animation.Static;
    private long animationStart = 0;
    
    public AnimationManager(int length, boolean state) {
        this.length = length;
        this.state = state;
        
        if (state) 
            prevAnimation = Animation.Expanding;
    }
    
    public void updateTime() {
        if (animation != Animation.Static) {
            if (System.currentTimeMillis() - animationStart > length) {
                prevAnimation = animation;
                animation = Animation.Static;
                
                if (prevAnimation == Animation.Retracting) 
                    state = false;
            }
        }
    }
    
    public double getAnimationFactor() {
        if (animation == Animation.Expanding) 
            return (System.currentTimeMillis() - animationStart) / (double) length;
        if (animation == Animation.Retracting) 
            return ((long) length - (System.currentTimeMillis() - animationStart)) / (double) length;
        
        return prevAnimation == Animation.Expanding ? 1 : 0;
    }


    public void updateState() {
        if (!state) {
            animation = Animation.Expanding;
            state = true;
        }

        else
            animation = Animation.Retracting;

        animationStart = System.currentTimeMillis();
    }
    
    public enum Animation { 
        Expanding, 
        Retracting, 
        Static 
    }
}

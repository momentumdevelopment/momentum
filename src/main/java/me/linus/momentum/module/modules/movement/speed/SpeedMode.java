package me.linus.momentum.module.modules.movement.speed;

import me.linus.momentum.event.events.player.MoveEvent;
import me.linus.momentum.mixin.MixinInterface;

/**
 * @author linustouchtips
 * @since 02/03/2021
 */

public class SpeedMode implements MixinInterface {

    public double moveSpeed = 0.0;
    public double lastDist = 0.0;
    public Stage stage = Stage.Cycle;

    public void onMotionUpdate() {

    }

    public void handleSpeed(MoveEvent event) {

    }

    public void onRubberband() {
        moveSpeed = 0.0;
        stage = Stage.Cycle;
        lastDist = 0;
    }

    public void onKnockback() {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public enum Stage {
        Pre,
        Jump,
        Post,
        Cycle
    }
}

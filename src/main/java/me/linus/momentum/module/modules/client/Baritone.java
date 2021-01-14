package me.linus.momentum.module.modules.client;

import baritone.api.BaritoneAPI;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.color.SubColor;
import me.linus.momentum.setting.slider.SubSlider;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Baritone extends Module {
    public Baritone() {
        super("Baritone", Category.CLIENT, "Settings for Baritone");
        this.enable();
    }
    
    public static Checkbox path = new Checkbox("Render Path", true);
    public static SubColor pathPicker = new SubColor(path, new Color(255, 0, 255));
    
    public static Checkbox goal = new Checkbox("Render Goal", true);
    public static SubColor goalPicker = new SubColor(goal, new Color(0, 255, 255));

    public static Checkbox allow = new Checkbox("Allow", true);
    public static SubCheckbox jumpAt256 = new SubCheckbox(allow, "Jump at Build Height", true);
    public static SubCheckbox placeBlocks = new SubCheckbox(allow, "Place Blocks", true);
    public static SubCheckbox breakBlocks = new SubCheckbox(allow, "Break Blocks", true);
    public static SubCheckbox parkour = new SubCheckbox(allow, "Parkour", true);
    public static SubCheckbox waterBucket = new SubCheckbox(allow, "Water Bucket", false);
    public static SubCheckbox sprint = new SubCheckbox(allow, "Sprint", true);
    public static SubCheckbox downward = new SubCheckbox(allow, "Downward", true);
    public static SubCheckbox vines = new SubCheckbox(allow, "Vines", false);
    public static SubCheckbox lava = new SubCheckbox(allow, "Lava", false);
    public static SubCheckbox water = new SubCheckbox(allow, "Water", false);

    public static Checkbox avoid = new Checkbox("Avoid Dangers", true);

    @Override
    public void setup() {
        addSetting(allow);
        addSetting(avoid);
        addSetting(path);
        addSetting(goal);
    }

    @Override
    public void onDisable() {
        this.enable();
    }

    @Override
    public void onUpdate() {
        BaritoneAPI.getSettings().colorCurrentPath.value = new Color(pathPicker.getColor().getRed(), pathPicker.getColor().getGreen(), pathPicker.getColor().getBlue());
        BaritoneAPI.getSettings().colorGoalBox.value = new Color(goalPicker.getColor().getRed(), goalPicker.getColor().getGreen(), goalPicker.getColor().getBlue());
        BaritoneAPI.getSettings().allowJumpAt256.value = jumpAt256.getValue();
        BaritoneAPI.getSettings().allowPlace.value = placeBlocks.getValue();
        BaritoneAPI.getSettings().allowBreak.value = breakBlocks.getValue();
        BaritoneAPI.getSettings().allowParkour.value = parkour.getValue();
        BaritoneAPI.getSettings().allowSprint.value = sprint.getValue();
        BaritoneAPI.getSettings().allowWaterBucketFall.value = waterBucket.getValue();
        BaritoneAPI.getSettings().renderPath.value = path.getValue();
        BaritoneAPI.getSettings().renderGoal.value = goal.getValue();
        BaritoneAPI.getSettings().okIfWater.value = water.getValue();
        BaritoneAPI.getSettings().allowDownward.value = downward.getValue();
        BaritoneAPI.getSettings().allowVines.value = vines.getValue();
        BaritoneAPI.getSettings().assumeWalkOnLava.value = lava.getValue();
        BaritoneAPI.getSettings().avoidance.value = avoid.getValue();
    }
}

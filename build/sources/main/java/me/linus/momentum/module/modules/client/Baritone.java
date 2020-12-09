package me.linus.momentum.module.modules.client;

import baritone.api.BaritoneAPI;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.slider.SubSlider;

import java.awt.*;

public class Baritone extends Module {
    public Baritone() {
        super("Baritone", Category.CLIENT, "Settings for Baritone");
        this.enable();
    }

    private static Checkbox color = new Checkbox("Colors", true);
    private static SubCheckbox path = new SubCheckbox(color, "Render Path", true);
    private static SubCheckbox goal = new SubCheckbox(color, "Render Goal", true);
    private static SubSlider pathRed = new SubSlider(color, "Path Red", 0.0D, 255.0D, 255.0D, 0);
    private static SubSlider pathGreen = new SubSlider(color, "Path Green", 0.0D, 0.0D, 255.0D, 0);
    private static SubSlider pathBlue = new SubSlider(color, "Path Blue", 0.0D, 255.0D, 255.0D, 0);
    private static SubSlider goalRed = new SubSlider(color, "Goal Red", 0.0D, 0.0D, 255.0D, 0);
    private static SubSlider goalGreen = new SubSlider(color, "Goal Green", 0.0D, 255.0D, 255.0D, 0);
    private static SubSlider goalBlue = new SubSlider(color, "Goal Blue", 0.0D, 0.0D, 255.0D, 0);

    private static Checkbox allow = new Checkbox("Allow", true);
    private static SubCheckbox jumpAt256 = new SubCheckbox(allow, "Jump at Build Height", true);
    private static SubCheckbox placeBlocks = new SubCheckbox(allow, "Place Blocks", true);
    private static SubCheckbox breakBlocks = new SubCheckbox(allow, "Break Blocks", true);
    private static SubCheckbox parkour = new SubCheckbox(allow, "Parkour", true);
    private static SubCheckbox waterBucket = new SubCheckbox(allow, "Water Bucket", false);
    private static SubCheckbox sprint = new SubCheckbox(allow, "Sprint", true);
    private static SubCheckbox downward = new SubCheckbox(allow, "Downward", true);
    private static SubCheckbox vines = new SubCheckbox(allow, "Vines", false);
    private static SubCheckbox lava = new SubCheckbox(allow, "Lava", false);
    private static SubCheckbox water = new SubCheckbox(allow, "Water", false);

    private static Checkbox avoid = new Checkbox("Avoid Dangers", true);

    @Override
    public void setup() {
        addSetting(allow);
        addSetting(avoid);
        addSetting(color);
    }

    @Override
    public void onDisable() {
        this.enable();
    }

    @Override
    public void onUpdate() {
        BaritoneAPI.getSettings().colorCurrentPath.value = new Color((int) pathRed.getValue(), (int) pathGreen.getValue(), (int) pathBlue.getValue());
        BaritoneAPI.getSettings().colorGoalBox.value = new Color((int) goalRed.getValue(), (int) goalGreen.getValue(), (int) goalBlue.getValue());
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

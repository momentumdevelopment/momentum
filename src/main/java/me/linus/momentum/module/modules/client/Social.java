package me.linus.momentum.module.modules.client;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;

/**
 * @author linustouchtips
 * @since 11/24/2020
 */

public class Social extends Module {
    public Social() {
        super("Social", Category.CLIENT, "Allows social system to function");
        this.enable();
        this.setDrawn(false);
    }

    public static Checkbox friends = new Checkbox("Friends", true);
    public static Checkbox enemies = new Checkbox("Enemies", true);

    @Override
    public void setup() {
        addSetting(friends);
        addSetting(enemies);
    }
}

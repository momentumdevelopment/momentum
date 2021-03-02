package me.linus.momentum.module.modules.render.esp.modes;

import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.module.modules.render.ESP;
import me.linus.momentum.module.modules.render.esp.ESPMode;
import net.minecraft.entity.Entity;

/**
 * @author linustouchtips
 * @since 01/06/2021
 */

public class Glow extends ESPMode {
    public Glow() {
        isRender = true;
    }

    @Override
    public void drawESP() {
        for (Entity entitylivingbaseIn : mc.world.loadedEntityList) {
            entitylivingbaseIn.setGlowing(true);

            if (ESP.mode.getValue() != 1)
                entitylivingbaseIn.setGlowing(false);

            if (!ModuleManager.getModuleByName("ESP").isEnabled())
                entitylivingbaseIn.setGlowing(false);
        }
    }
}

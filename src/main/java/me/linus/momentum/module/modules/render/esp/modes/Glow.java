package me.linus.momentum.module.modules.render.esp.modes;

import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.module.modules.render.ESP;
import me.linus.momentum.module.modules.render.esp.ESPMode;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author linustouchtips
 * @since 01/06/2021
 */

public class Glow extends ESPMode {

    @Override
    public void drawESP() {
        for (Entity entitylivingbaseIn : mc.world.loadedEntityList) {
            if (!entitylivingbaseIn.isGlowing() && (entitylivingbaseIn instanceof EntityPlayer && !(entitylivingbaseIn instanceof EntityPlayerSP) && ESP.players.getValue()) || (EntityUtil.isPassive(entitylivingbaseIn) && ESP.animals.getValue()) || (EntityUtil.isHostileMob(entitylivingbaseIn) && ESP.mobs.getValue()) || (EntityUtil.isVehicle(entitylivingbaseIn) && ESP.vehicles.getValue()) || (entitylivingbaseIn instanceof EntityEnderCrystal && ESP.crystals.getValue()) || (entitylivingbaseIn instanceof EntityItem && ESP.items.getValue()))
                entitylivingbaseIn.setGlowing(true);

            if (ESP.mode.getValue() != 1)
                entitylivingbaseIn.setGlowing(false);

            if (!ModuleManager.getModuleByName("ESP").isEnabled())
                entitylivingbaseIn.setGlowing(false);
        }
    }
}

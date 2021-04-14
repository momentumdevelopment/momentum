package me.linus.momentum.module.modules.render.esp.modes;

import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.module.modules.render.ESP;
import me.linus.momentum.module.modules.render.esp.ESPMode;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.shader.Shader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Objects;

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
            if ((entitylivingbaseIn instanceof EntityPlayer && !(entitylivingbaseIn instanceof EntityPlayerSP) && ESP.players.getValue() || (EntityUtil.isPassive(entitylivingbaseIn) && ESP.animals.getValue()) || (EntityUtil.isHostileMob(entitylivingbaseIn) && ESP.mobs.getValue()) || (EntityUtil.isVehicle(entitylivingbaseIn) && ESP.vehicles.getValue()) || (entitylivingbaseIn instanceof EntityEnderCrystal && ESP.crystals.getValue())))
                entitylivingbaseIn.setGlowing(true);

            if (ESP.mode.getValue() != 1)
                entitylivingbaseIn.setGlowing(false);

            if (!ModuleManager.getModuleByName("ESP").isEnabled())
                entitylivingbaseIn.setGlowing(false);
        }
    }
}

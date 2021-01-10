package me.linus.momentum.module.modules.render.esp.modes;

import me.linus.momentum.module.modules.render.ESP;
import me.linus.momentum.module.modules.render.esp.ESPMode;
import me.linus.momentum.util.render.ESPUtil;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 01/06/2021
 */

public class Glow extends ESPMode {

    @Override
    public void drawESP() {
        for (Entity entitylivingbaseIn : mc.world.loadedEntityList) {
            if (!entitylivingbaseIn.isGlowing() && (entitylivingbaseIn instanceof EntityPlayer && !(entitylivingbaseIn instanceof EntityPlayerSP) && ESP.players.getValue()))
                ESPUtil.drawGlow(entitylivingbaseIn, "players", TextFormatting.RED);

            if ((EntityUtil.isPassive(entitylivingbaseIn) && ESP.animals.getValue()))
                ESPUtil.drawGlow(entitylivingbaseIn, "animals", TextFormatting.GREEN);

            if ((EntityUtil.isHostileMob(entitylivingbaseIn) && ESP.mobs.getValue()))
                ESPUtil.drawGlow(entitylivingbaseIn, "mobs", TextFormatting.DARK_PURPLE);

            if ((EntityUtil.isVehicle(entitylivingbaseIn) && ESP.vehicles.getValue()))
                ESPUtil.drawGlow(entitylivingbaseIn, "vehicles", TextFormatting.GOLD);

            if (entitylivingbaseIn instanceof EntityEnderCrystal && ESP.crystals.getValue())
                ESPUtil.drawGlow(entitylivingbaseIn, "crystals", TextFormatting.BLUE);

            if (ESP.mode.getValue() != 1)
                entitylivingbaseIn.setGlowing(false);
        }
    }
}

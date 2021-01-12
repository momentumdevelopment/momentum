package me.linus.momentum.module.modules.render.esp.modes;

import me.linus.momentum.module.modules.render.ESP;
import me.linus.momentum.module.modules.render.esp.ESPMode;
import me.linus.momentum.util.client.ColorUtil;
import me.linus.momentum.util.render.builder.RenderUtil;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;

public class Box extends ESPMode {

    @Override
    public void drawESP() {
        if ((mc.getRenderManager()).options == null)
            return;

        mc.world.loadedEntityList.stream().filter(entity -> (mc.player != entity)).forEach(entitylivingbaseIn -> {
            if (entitylivingbaseIn instanceof EntityPlayer && !(entitylivingbaseIn instanceof EntityPlayerSP) && ESP.players.getValue() || (EntityUtil.isPassive(entitylivingbaseIn) && ESP.animals.getValue()) || (EntityUtil.isHostileMob(entitylivingbaseIn) && ESP.mobs.getValue()) || (EntityUtil.isVehicle(entitylivingbaseIn) && ESP.vehicles.getValue()) || (entitylivingbaseIn instanceof EntityEnderCrystal && ESP.crystals.getValue())) {
                Color color = ColorUtil.getEntityColor(entitylivingbaseIn);
                RenderUtil.drawSelectionBoundingBox(entitylivingbaseIn.boundingBox, 0, color.getRed(), color.getGreen(), color.getBlue(), 255);
            }
        });
    }
}

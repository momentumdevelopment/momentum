package me.linus.momentum.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.linus.momentum.module.Module;
import me.linus.momentum.util.client.MessageUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.init.SoundEvents;

/**
 * @author olliem5
 * @since 12/17/2020
 */

public class EntityAlert extends Module {
    public EntityAlert() {
        super("EntityAlert", Category.MISC, "Logs positions of nearby entities");
    }

    private int donkeyDelay;
    private int llamaDelay;
    private int muleDelay;

    public void onUpdate() {
        if (nullCheck())
            return;

        ++donkeyDelay;
        ++llamaDelay;
        ++muleDelay;

        for (Entity entity : mc.world.getLoadedEntityList()) {
            if (entity instanceof EntityDonkey && this.donkeyDelay >= 100) {
                MessageUtil.sendClientMessage(ChatFormatting.GRAY + "[" + ChatFormatting.LIGHT_PURPLE + "EntityAlert" + ChatFormatting.GRAY + "] " + ChatFormatting.WHITE + "Found a " + ChatFormatting.AQUA + "donkey " + ChatFormatting.WHITE + "at " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + Math.round(entity.lastTickPosX) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity.lastTickPosY) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity.lastTickPosZ) + ChatFormatting.GRAY + "]");
                mc.getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F));
                this.donkeyDelay = -750;
            } else if (entity instanceof EntityLlama && this.llamaDelay >= 100) {
                MessageUtil.sendClientMessage(ChatFormatting.GRAY + "[" + ChatFormatting.LIGHT_PURPLE + "EntityAlert" + ChatFormatting.GRAY + "] " + ChatFormatting.WHITE + "Found a " + ChatFormatting.AQUA + "llama " + ChatFormatting.WHITE + "at " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + Math.round(entity.lastTickPosX) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity.lastTickPosY) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity.lastTickPosZ) + ChatFormatting.GRAY + "]");
                mc.getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F));
                this.llamaDelay = -750;
            } else if (entity instanceof EntityMule && this.muleDelay >= 100) {
                MessageUtil.sendClientMessage(ChatFormatting.GRAY + "[" + ChatFormatting.LIGHT_PURPLE + "EntityAlert" + ChatFormatting.GRAY + "] " + ChatFormatting.WHITE + "Found a " + ChatFormatting.AQUA + "mule " + ChatFormatting.WHITE + "at " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + Math.round(entity.lastTickPosX) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity.lastTickPosY) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity.lastTickPosZ) + ChatFormatting.GRAY + "]");
                mc.getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F));
                this.muleDelay = -750;
            }
        }
    }
}

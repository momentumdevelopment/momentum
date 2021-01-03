package me.linus.momentum.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.util.client.MessageUtil;
import me.linus.momentum.util.client.notification.Notification;
import me.linus.momentum.util.client.notification.NotificationManager;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.init.SoundEvents;

/**
 * @author olliem5 & linustouchtips
 * @since 12/17/2020
 */

public class EntityAlert extends Module {
    public EntityAlert() {
        super("EntityAlert", Category.MISC, "Logs positions of nearby entities");
    }

    private static final Checkbox donkeys = new Checkbox("Donkeys", true);
    private static final Checkbox llamas = new Checkbox("Llamas", true);
    private static final Checkbox mules = new Checkbox("Mules", true);

    @Override
    public void setup() {
        addSetting(donkeys);
        addSetting(llamas);
        addSetting(mules);
    }

    int donkeyDelay;
    int llamaDelay;
    int muleDelay;

    public void onUpdate() {
        if (nullCheck())
            return;

        donkeyDelay++;
        llamaDelay++;
        muleDelay++;

        for (Entity entity : mc.world.getLoadedEntityList()) {
            if (entity instanceof EntityDonkey && donkeyDelay >= 100 && donkeys.getValue()) {
                NotificationManager.notifications.add(new Notification("Found a donkey at [" + Math.round(entity.lastTickPosX) + ", " + Math.round(entity.lastTickPosY) + ", " + Math.round(entity.lastTickPosZ) + "]"));
                MessageUtil.sendClientMessage("Found a " + ChatFormatting.AQUA + "donkey " + ChatFormatting.WHITE + "at " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + Math.round(entity.lastTickPosX) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity.lastTickPosY) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity.lastTickPosZ) + ChatFormatting.GRAY + "]");
                mc.getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F));
                donkeyDelay = -750;
            } 
            
            if (entity instanceof EntityLlama && llamaDelay >= 100 && llamas.getValue()) {
                NotificationManager.notifications.add(new Notification("Found a llama at [" + Math.round(entity.lastTickPosX) + ", " + Math.round(entity.lastTickPosY) + ", " + Math.round(entity.lastTickPosZ) + "]"));
                MessageUtil.sendClientMessage("Found a " + ChatFormatting.AQUA + "llama " + ChatFormatting.WHITE + "at " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + Math.round(entity.lastTickPosX) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity.lastTickPosY) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity.lastTickPosZ) + ChatFormatting.GRAY + "]");
                mc.getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F));
                llamaDelay = -750;
            } 
            
            if (entity instanceof EntityMule && muleDelay >= 100 && mules.getValue()) {
                NotificationManager.notifications.add(new Notification("Found a mule at [" + Math.round(entity.lastTickPosX) + ", " + Math.round(entity.lastTickPosY) + ", " + Math.round(entity.lastTickPosZ) + "]"));
                MessageUtil.sendClientMessage("Found a " + ChatFormatting.AQUA + "mule " + ChatFormatting.WHITE + "at " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + Math.round(entity.lastTickPosX) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity.lastTickPosY) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity.lastTickPosZ) + ChatFormatting.GRAY + "]");
                mc.getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F));
                muleDelay = -750;
            }
        }
    }
}

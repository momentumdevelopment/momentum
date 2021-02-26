package me.linus.momentum.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.linus.momentum.Momentum;
import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.managers.GearManager;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.util.client.MessageUtil;
import me.linus.momentum.util.combat.EnemyUtil;
import me.linus.momentum.util.world.Timer;
import me.linus.momentum.util.world.Timer.Format;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

/**
 * @author linustouchtips & olliem5
 * @since 12/07/2020
 */

public class Notifier extends Module {
    public Notifier() {
        super("Notifier", Category.MISC, "Notifies you for various things");
    }

    public static Checkbox totem = new Checkbox("Totem Pop", true);
    public static Checkbox armor = new Checkbox("Armor", true);
    public static Checkbox visualRange = new Checkbox("Visual Range", false);

    public static Checkbox entityAlert = new Checkbox("Entity Alert", false);
    public static SubCheckbox donkeys = new SubCheckbox(entityAlert, "Donkeys", true);
    public static SubCheckbox llamas = new SubCheckbox(entityAlert, "Llamas", true);
    public static SubCheckbox mules = new SubCheckbox(entityAlert, "Mules", true);

    @Override
    public void setup() {
        addSetting(totem);
        addSetting(armor);
        addSetting(visualRange);
        addSetting(entityAlert);
    }

    Timer notificationTimer = new Timer();

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (armor.getValue() && mc.player.getArmorInventoryList() != null && EnemyUtil.getArmor(mc.player) < 15 && notificationTimer.passed(60, Format.Ticks))
            MessageUtil.sendClientMessage("Your armor durability is getting low!");

        mc.world.loadedEntityList.forEach(entity -> {
            if (entity instanceof EntityDonkey && donkeys.getValue() && notificationTimer.passed(60, Format.Ticks)) {
                MessageUtil.sendClientMessage("Found a " + ChatFormatting.AQUA + "donkey " + ChatFormatting.WHITE + "at " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + Math.round(entity.lastTickPosX) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity.lastTickPosY) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity.lastTickPosZ) + ChatFormatting.GRAY + "]");
                mc.getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F));
            }

            if (entity instanceof EntityLlama && llamas.getValue() && notificationTimer.passed(60, Format.Ticks)) {
                MessageUtil.sendClientMessage("Found a " + ChatFormatting.AQUA + "llama " + ChatFormatting.WHITE + "at " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + Math.round(entity.lastTickPosX) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity.lastTickPosY) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity.lastTickPosZ) + ChatFormatting.GRAY + "]");
                mc.getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F));
            }

            if (entity instanceof EntityMule && mules.getValue() && notificationTimer.passed(60, Format.Ticks)) {
                MessageUtil.sendClientMessage("Found a " + ChatFormatting.AQUA + "mule " + ChatFormatting.WHITE + "at " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + Math.round(entity.lastTickPosX) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity.lastTickPosY) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity.lastTickPosZ) + ChatFormatting.GRAY + "]");
                mc.getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F));
            }
        });

        mc.world.playerEntities.stream().forEach(player -> {
            if (totem.getValue()) {
                if (!GearManager.totemMap.containsKey(player.getName()))
                    return;

                if (EnemyUtil.getHealth(player) <= 0.0f) {
                    GearManager.totemMap.remove(player.getName());
                    MessageUtil.sendClientMessage(player.getName() + " died after popping " + TextFormatting.RED + GearManager.totemMap.get(player.getName()) + TextFormatting.WHITE + " totems!");
                }
            }

            if (player != mc.player && visualRange.getValue() && notificationTimer.passed(60, Format.Ticks)) {
                if (Momentum.friendManager.isFriend(player.getName()))
                    MessageUtil.sendClientMessage("Your friend, " + player.getName() + ", has entered your visual range!");
                else if (notificationTimer.passed(60, Format.Ticks))
                    MessageUtil.sendClientMessage(player.getName() + "has entered your visual range!");
            }
        });
    }
}
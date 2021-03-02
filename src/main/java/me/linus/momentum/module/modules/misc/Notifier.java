package me.linus.momentum.module.modules.misc;

import me.linus.momentum.managers.GearManager;
import me.linus.momentum.managers.social.friend.FriendManager;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.util.client.MessageUtil;
import me.linus.momentum.util.combat.EnemyUtil;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.util.text.TextFormatting;

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

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (armor.getValue()) {
            mc.player.getArmorInventoryList();
            if (EnemyUtil.getArmor(mc.player) < 15) MessageUtil.sendClientMessage("Your armor durability is getting low!");
        }

        mc.world.loadedEntityList.forEach(entity -> {
            if (entity instanceof EntityDonkey && donkeys.getValue())
                MessageUtil.sendClientMessage("Found a donkey at " + "[" +  Math.round(entity.lastTickPosX) + ", " +  Math.round(entity.lastTickPosY) + ", " +  Math.round(entity.lastTickPosZ) + "]");

            if (entity instanceof EntityLlama && llamas.getValue())
                MessageUtil.sendClientMessage("Found a llama at " + "[" +  Math.round(entity.lastTickPosX) + ", " +  Math.round(entity.lastTickPosY) + ", " +  Math.round(entity.lastTickPosZ) + "]");

            if (entity instanceof EntityMule && mules.getValue())
                MessageUtil.sendClientMessage("Found a mule at " + "[" +  Math.round(entity.lastTickPosX) + ", " +  Math.round(entity.lastTickPosY) + ", " +  Math.round(entity.lastTickPosZ) + "]");
        });

        mc.world.playerEntities.forEach(player -> {
            if (totem.getValue()) {
                if (!GearManager.totemMap.containsKey(player.getName()))
                    return;

                if (EnemyUtil.getHealth(player) <= 0.0f) {
                    GearManager.totemMap.remove(player.getName());
                    MessageUtil.sendClientMessage(player.getName() + " died after popping " + TextFormatting.RED + GearManager.totemMap.get(player.getName()) + TextFormatting.WHITE + " totems!");
                }
            }

            if (player != mc.player && visualRange.getValue()) {
                if (FriendManager.isFriend(player.getName()))
                    MessageUtil.sendClientMessage("Your friend, " + player.getName() + ", has entered your visual range!");
                else
                    MessageUtil.sendClientMessage(player.getName() + " has entered your visual range!");
            }
        });
    }
}
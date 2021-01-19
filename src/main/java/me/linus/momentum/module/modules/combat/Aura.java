package me.linus.momentum.module.modules.combat;

import me.linus.momentum.event.events.player.RotationEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.world.Timer;
import me.linus.momentum.util.client.friend.FriendManager;
import me.linus.momentum.util.combat.EnemyUtil;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.player.PlayerUtil;
import me.linus.momentum.util.player.rotation.Rotation;
import me.linus.momentum.util.player.rotation.RotationUtil;
import me.linus.momentum.util.world.*;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Comparator;

/**
 * @author linustouchtips
 * @since 11/27/2020
 */

public class Aura extends Module {
    public Aura() {
        super("Aura", Category.COMBAT, "Attacks entities");
    }

    public static Mode mode = new Mode("Mode", "Closest", "Health", "Armor");

    public static Checkbox attackCheck = new Checkbox("Attack Check", true);
    public static SubCheckbox players = new SubCheckbox(attackCheck, "Players", true);
    public static SubCheckbox animals = new SubCheckbox(attackCheck, "Animals", true);
    public static SubCheckbox mobs = new SubCheckbox(attackCheck, "Mobs", true);

    public static Checkbox delay = new Checkbox("Delay", true);
    public static SubCheckbox useTicks = new SubCheckbox(delay, "Use Ticks", true);
    public static SubSlider tickDelay = new SubSlider(delay, "Tick Delay", 0.0D, 10.0D, 20.0D, 1);
    public static SubCheckbox sync = new SubCheckbox(delay, "TPS Sync", false);

    public static Checkbox armorMelt = new Checkbox("Armor Melt", false);

    public static Mode weaponCheck = new Mode("Weapon", "Swing", "Damage");
    public static SubCheckbox autoSwitch = new SubCheckbox(weaponCheck, "Auto Switch", true);
    public static SubCheckbox swordOnly = new SubCheckbox(weaponCheck, "Sword Only", true);
    public static SubCheckbox thirtyTwoKOnly = new SubCheckbox(weaponCheck, "32K Only", false);

    public static Checkbox pause = new Checkbox("Pause", true);
    public static SubCheckbox cannotSee = new SubCheckbox(pause, "Target Cannot be Seen", false);
    public static SubCheckbox crystalPause = new SubCheckbox(pause, "When Crystalling", false);
    public static SubCheckbox holePause = new SubCheckbox(pause, "When not in Hole", false);
    public static SubCheckbox eatPause = new SubCheckbox(pause, "When Eating", false);

    public static Mode rotate = new Mode("Rotate", "Packet", "Legit", "None");
    public static SubCheckbox spoof = new SubCheckbox(rotate, "Spoof Angles", false);

    public static Slider range = new Slider("Range", 0.0D, 6.0D, 10.0D, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(attackCheck);
        addSetting(delay);
        addSetting(weaponCheck);
        addSetting(pause);
        addSetting(rotate);
        addSetting(range);
    }

    Timer syncTimer = new Timer();
    Entity currentTarget;
    Rotation auraRotation;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (autoSwitch.getValue())
            InventoryUtil.switchToSlot(getItem());

        if (currentTarget != null && !FriendManager.isFriend(currentTarget.getName()) && FriendManager.isFriendModuleEnabled()) {
            auraRotation = new Rotation(RotationUtil.getAngles(currentTarget)[0], RotationUtil.getAngles(currentTarget)[1]);
            RotationUtil.updateRotations(auraRotation, rotate.getValue());
        }

        killAura();
    }

    @SubscribeEvent
    public void onRotation(RotationEvent event) {
        if (auraRotation != null && rotate.getValue() == 0) {
            event.setCanceled(true);
            event.setPitch(auraRotation.yaw);
            event.setYaw(auraRotation.pitch);
        }
    }

    public void killAura() {
        switch (mode.getValue()) {
            case 0:
                currentTarget = WorldUtil.getNearbyPlayers(range.getValue()).stream().filter(entity -> EnemyUtil.attackCheck(entity, players.getValue(), animals.getValue(), mobs.getValue())).sorted(Comparator.comparing(e -> mc.player.getDistance(e))).findFirst().orElse(null);
                break;
            case 1:
                currentTarget = WorldUtil.getNearbyPlayers(range.getValue()).stream().min(Comparator.comparing(entityPlayer -> EnemyUtil.getHealth(entityPlayer))).orElse(null);
                break;
            case 2:
                currentTarget = WorldUtil.getNearbyPlayers(range.getValue()).stream().min(Comparator.comparing(entityPlayer -> EnemyUtil.getArmor(entityPlayer))).orElse(null);
                break;
        }

        if (swordOnly.getValue() && !(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword))
            return;

        if (cannotSee.getValue() && (!mc.player.canEntityBeSeen(currentTarget) && !EntityUtil.canEntityFeetBeSeen(currentTarget)))
            return;

        if (crystalPause.getValue() && (ModuleManager.getModuleByName("AutoCrystal").isEnabled() || mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal))
            return;

        if (PlayerUtil.isEating() && eatPause.getValue() )
            return;

        if (holePause.getValue() && !HoleUtil.isInHole(mc.player))
            return;

        if (thirtyTwoKOnly.getValue() && !InventoryUtil.Is32k(mc.player.getHeldItemMainhand()))
            return;

        if (FriendManager.isFriend(currentTarget.getName()) && FriendManager.isFriendModuleEnabled())
            return;

        if (currentTarget != null)
            attackEntity(currentTarget);
    }

    public void attackEntity(Entity target) {
        if (useTicks.getValue() && !sync.getValue() && syncTimer.passed((long) (tickDelay.getValue() * 50), Timer.Format.System))
            PlayerUtil.attackEntity(target);

        if (sync.getValue() && syncTimer.passed((long) ((TickUtil.TPS / 20) * 1000), Timer.Format.System))
            PlayerUtil.attackEntity(target);

        if (armorMelt.getValue()) {
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 9, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
            PlayerUtil.attackEntity(target);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 9, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
            PlayerUtil.attackEntity(target);
        }

        else
            PlayerUtil.attackEntity(target);
    }

    public Item getItem() {
        if (weaponCheck.getValue() == 0)
            return Items.DIAMOND_SWORD;
        else
            return Items.DIAMOND_AXE;
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
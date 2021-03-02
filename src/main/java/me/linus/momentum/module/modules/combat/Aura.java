package me.linus.momentum.module.modules.combat;

import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.managers.RotationManager;
import me.linus.momentum.managers.social.friend.FriendManager;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.player.PlayerUtil;
import me.linus.momentum.util.player.rotation.Rotation;
import me.linus.momentum.util.player.rotation.Rotation.RotationMode;
import me.linus.momentum.util.player.rotation.RotationPriority;
import me.linus.momentum.util.player.rotation.RotationUtil;
import me.linus.momentum.util.world.HoleUtil;
import me.linus.momentum.util.world.RaytraceUtil;
import me.linus.momentum.util.world.Timer;
import me.linus.momentum.util.world.Timer.Format;
import me.linus.momentum.util.world.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemSword;

/**
 * @author linustouchtips
 * @since 11/27/2020
 */

public class Aura extends Module {
    public Aura() {
        super("Aura", Category.COMBAT, "Attacks entities");
    }

    public static Mode mode = new Mode("Mode", "Closest", "LowestHealth", "LowestArmor");

    public static Checkbox players = new Checkbox("Players", true);
    public static Checkbox animals = new Checkbox("Animals", true);
    public static Checkbox mobs = new Checkbox("Mobs", true);
    public static Checkbox projectiles = new Checkbox("Projectiles", true);

    public static Checkbox delay = new Checkbox("Delay", true);
    public static SubCheckbox cooldown = new SubCheckbox(delay, "Cooldown", true);
    public static SubCheckbox useTicks = new SubCheckbox(delay, "Use Ticks", true);
    public static SubSlider tickDelay = new SubSlider(delay, "Tick Delay", 0.0D, 10.0D, 20.0D, 1);
    public static SubCheckbox sync = new SubCheckbox(delay, "TPS Sync", false);

    public static Checkbox useTimer = new Checkbox("Use Timer", false);
    public static SubSlider timerTicks = new SubSlider(useTimer, "Timer Speed", 0.1D, 1.2D, 2.0D, 2);

    public static Mode weaponCheck = new Mode("Weapon", "Swing", "Damage");
    public static SubCheckbox autoSwitch = new SubCheckbox(weaponCheck, "Auto Switch", true);
    public static SubCheckbox swordOnly = new SubCheckbox(weaponCheck, "Sword Only", true);
    public static SubCheckbox thirtyTwoKOnly = new SubCheckbox(weaponCheck, "32K Only", false);

    public static Checkbox raytrace = new Checkbox("Ray-Trace", false);

    public static Checkbox pause = new Checkbox("Pause", true);
    public static SubCheckbox crystalPause = new SubCheckbox(pause, "When Crystalling", true);
    public static SubCheckbox holePause = new SubCheckbox(pause, "When not in Hole", false);
    public static SubCheckbox eatPause = new SubCheckbox(pause, "When Eating", false);

    public static Mode rotate = new Mode("Rotate", "Packet", "Legit", "None");
    public static Slider range = new Slider("Range", 0.0D, 6.0D, 10.0D, 0);

    public static Checkbox packet = new Checkbox("Packet Swing", true);
    public static Checkbox teleport = new Checkbox("Teleport", false);
    public static Checkbox armorMelt = new Checkbox("Armor Melt", false);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(players);
        addSetting(mobs);
        addSetting(animals);
        addSetting(projectiles);
        addSetting(delay);
        addSetting(useTimer);
        addSetting(weaponCheck);
        addSetting(raytrace);
        addSetting(pause);
        addSetting(rotate);
        addSetting(range);
        addSetting(packet);
        addSetting(teleport);
        addSetting(armorMelt);
    }

    Timer syncTimer = new Timer();
    Entity currentTarget;
    Rotation auraRotation = null;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (useTimer.getValue())
            mc.timer.tickLength = (float) (50f / timerTicks.getValue());

        currentTarget = WorldUtil.getTarget(range.getValue(), mode.getValue());

        if (teleport.getValue())
            mc.player.setPosition(currentTarget.posX, currentTarget.posY, currentTarget.posZ);

        if (autoSwitch.getValue())
            InventoryUtil.switchToSlot(getItem());

        if (!InventoryUtil.getHeldItem(Items.DIAMOND_SWORD))
            auraRotation.restoreRotation();

        if (currentTarget != null && !FriendManager.isFriend(currentTarget.getName()) && FriendManager.isFriendModuleEnabled()) {
            switch (rotate.getValue()) {
                case 0:
                    auraRotation = new Rotation(RotationUtil.getAngles(currentTarget)[0], RotationUtil.getAngles(currentTarget)[1], RotationMode.Packet, RotationPriority.High);
                    break;
                case 1:
                    auraRotation = new Rotation(RotationUtil.getAngles(currentTarget)[0], RotationUtil.getAngles(currentTarget)[1], RotationMode.Legit, RotationPriority.High);
                    break;
            }

            RotationManager.rotationQueue.add(auraRotation);
        }

        killAura();
    }

    public void killAura() {
        if (swordOnly.getValue() && !(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword))
            return;

        if (raytrace.getValue() && (!RaytraceUtil.raytraceEntity(currentTarget) && !RaytraceUtil.raytraceFeet(currentTarget)))
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

        if (currentTarget != null) {
            mc.timer.tickLength = 50f / 1.2f;
            attackEntity(currentTarget);
        }
    }

    public void attackEntity(Entity target) {
        if (useTicks.getValue() && !sync.getValue() && syncTimer.passed((long) tickDelay.getValue(), Format.Ticks))
            PlayerUtil.attackEntity(target, packet.getValue(), cooldown.getValue(), sync.getValue());

        else if (armorMelt.getValue()) {
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 9, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
            PlayerUtil.attackEntity(target, packet.getValue(), cooldown.getValue(), sync.getValue());
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 9, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
            PlayerUtil.attackEntity(target, packet.getValue(), cooldown.getValue(), sync.getValue());
        }

        else
            PlayerUtil.attackEntity(target, packet.getValue(), cooldown.getValue(), sync.getValue());
    }

    public Item getItem() {
        return (weaponCheck.getValue() == 0) ? Items.DIAMOND_SWORD : Items.DIAMOND_AXE;
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
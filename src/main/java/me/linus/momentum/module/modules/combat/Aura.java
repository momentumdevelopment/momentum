package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.client.Timer;
import me.linus.momentum.util.client.friend.FriendManager;
import me.linus.momentum.util.combat.CrystalUtil;
import me.linus.momentum.util.combat.EnemyUtil;
import me.linus.momentum.util.world.EntityUtil;
import me.linus.momentum.util.world.InventoryUtil;
import me.linus.momentum.util.world.PlayerUtil;
import me.linus.momentum.util.world.TickUtil;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linustouchtips
 * @since 11/27/2020
 */

public class Aura extends Module {

    public Aura() {
        super("Aura", Category.COMBAT, "Attacks entities");
    }

    private static final Mode mode = new Mode("Mode", "Closest", "Priority", "Armor");
    public static SubSlider priorityHealth = new SubSlider(mode, "Priority Health", 0.0D, 20.0D, 36.0D, 0);

    public static Checkbox attackCheck = new Checkbox("Attack Check", true);
    private static final SubCheckbox players = new SubCheckbox(attackCheck, "Players", true);
    private static final SubCheckbox animals = new SubCheckbox(attackCheck, "Animals", true);
    private static final SubCheckbox mobs = new SubCheckbox(attackCheck, "Mobs", true);

    public static Checkbox delay = new Checkbox("Delay", true);
    public static SubCheckbox useTicks = new SubCheckbox(delay, "Use Ticks", true);
    public static SubSlider tickDelay = new SubSlider(delay, "Tick Delay", 0.0D, 10.0D, 20.0D, 1);
    public static SubCheckbox sync = new SubCheckbox(delay, "TPS Sync", true);

    public static Checkbox armorMelt = new Checkbox("Armor Melt", false);

    public static Mode weaponCheck = new Mode("Weapon", "Swing", "Damage");
    private static final SubCheckbox autoSwitch = new SubCheckbox(weaponCheck, "Auto Switch", true);
    private static final SubCheckbox swordOnly = new SubCheckbox(weaponCheck, "Sword Only", true);
    private static final SubCheckbox thirtyTwoKOnly = new SubCheckbox(weaponCheck, "32K Only", false);

    public static Checkbox pause = new Checkbox("Pause", true);
    public static SubCheckbox cannotSee = new SubCheckbox(pause, "Target Cannot be Seen", false);
    public static SubCheckbox crystalPause = new SubCheckbox(pause, "When Crystalling", false);
    public static SubCheckbox holePause = new SubCheckbox(pause, "When not in Hole", false);
    public static SubCheckbox eatPause = new SubCheckbox(pause, "When Eating", false);

    public static Checkbox rotate = new Checkbox("Rotate", false);
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

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        int slot;
        if (autoSwitch.getValue()) {
            for (int i = 0; i < 9; i++) {
                if (mc.player.inventory.getStackInSlot(i).getItem().equals(getItem())) {
                    slot = i;
                    mc.player.inventory.currentItem = slot;
                    mc.playerController.updateController();
                    break;
                }
            }
        }

        if (rotate.getValue()) {
            Vec3d pos = EntityUtil.interpolateEntityTime(currentTarget, this.mc.getRenderPartialTicks());
            float[] angles = MathUtil.calcAngle(EntityUtil.interpolateEntityTime(this.mc.player, this.mc.getRenderPartialTicks()), pos);

            if (!spoof.getValue()) {
                mc.player.rotationYaw = angles[0];
                mc.player.rotationPitch = angles[1];
            }

            if (spoof.getValue())
                CrystalUtil.lookAtPacket(pos.x, pos.y, pos.z, mc.player);
        }

        killAura();
    }

    public void killAura() {
        List<Entity> targets = null;
        if (mode.getValue() == 0)
            targets = mc.world.loadedEntityList.stream().filter(entity -> entity != mc.player).filter(entity -> mc.player.getDistance(entity) <= range.getValue()).filter(entity -> !entity.isDead).filter(entity -> EnemyUtil.attackCheck(entity, players.getValue(), animals.getValue(), mobs.getValue())).sorted(Comparator.comparing(e -> mc.player.getDistance(e))).collect(Collectors.toList());

        if (mode.getValue() == 1)
            targets = mc.world.playerEntities.stream().filter(entity -> entity != mc.player).filter(entity -> mc.player.getDistance(entity) <= range.getValue()).filter(entity -> !entity.isDead).sorted(Comparator.comparing(e -> e.getHealth() + e.getAbsorptionAmount())).collect(Collectors.toList());

        targets.forEach(target -> {
            currentTarget = target;

            if (swordOnly.getValue() && !(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword))
                return;

            if (cannotSee.getValue() && (!mc.player.canEntityBeSeen(target) && !EntityUtil.canEntityFeetBeSeen(target)))
                return;

            if (crystalPause.getValue() && (ModuleManager.getModuleByName("AutoCrystal").isEnabled() || mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal))
                return;

            if (eatPause.getValue() && (mc.player.getHeldItemMainhand().getItem() instanceof ItemFood))
                return;

            if (holePause.getValue() && !PlayerUtil.isInHole())
                return;

            if (thirtyTwoKOnly.getValue() && !InventoryUtil.Is32k(mc.player.getHeldItemMainhand()))
                return;

            if (FriendManager.isFriend(target.getName()) && FriendManager.isFriendModuleEnabled())
                return;

            attackEntity(target);
        });
    }

    public void attackEntity(Entity target) {
        if (useTicks.getValue() && syncTimer.passed((long) (tickDelay.getValue() * 50)))
            PlayerUtil.attackEntity(target);

        if (sync.getValue() && syncTimer.passed((long) ((TickUtil.TPS / 20) * 1000)))
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
        if (mode.getValue() == 0)
            return Items.DIAMOND_SWORD;
        else
            return Items.DIAMOND_AXE;
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}

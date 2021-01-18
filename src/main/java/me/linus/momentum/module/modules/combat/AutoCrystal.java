package me.linus.momentum.module.modules.combat;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.event.events.player.RotationEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.color.SubColor;
import me.linus.momentum.setting.keybind.SubKeybind;
import me.linus.momentum.setting.mode.SubMode;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.render.builder.RenderBuilder;
import me.linus.momentum.util.world.Timer;
import me.linus.momentum.util.client.friend.FriendManager;
import me.linus.momentum.util.combat.CrystalUtil;
import me.linus.momentum.util.combat.EnemyUtil;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.player.PlayerUtil;
import me.linus.momentum.util.player.rotation.Rotation;
import me.linus.momentum.util.player.rotation.RotationUtil;
import me.linus.momentum.util.render.builder.RenderUtil;
import me.linus.momentum.util.world.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author linustouchtips
 * @since 11/24/2020
 */

// TODO: fix placement calc
public class AutoCrystal extends Module {
    public AutoCrystal() {
        super("AutoCrystal", Category.COMBAT, "Automatically places and explodes crystals");
        rotateMode.setMode(1);
    }

    public static Checkbox explode = new Checkbox("Break", true);
    public static SubMode breakMode = new SubMode(explode, "Mode", "All", "Only Own");
    public static SubSlider breakRange = new SubSlider(explode, "Break Range", 0.0D, 5.0D, 7.0D, 1);
    public static SubSlider breakDelay = new SubSlider(explode, "Break Delay", 0.0D, 20.0D, 60.0D, 0);
    public static SubSlider breakAttempts = new SubSlider(explode, "Break Attempts", 0.0D, 1.0D, 5.0D, 0);
    public static SubSlider wallRange = new SubSlider(explode, "Walls Range", 0.0D, 3.0D, 7.0D, 1);
    public static SubCheckbox syncBreak = new SubCheckbox(explode, "Sync Break", true);
    public static SubCheckbox unload = new SubCheckbox(explode, "Unload Crystal", true);
    public static SubCheckbox packetBreak = new SubCheckbox(explode, "Packet Break", true);
    public static SubCheckbox damageSync = new SubCheckbox(explode, "Damage Sync", false);
    public static SubCheckbox antiWeakness = new SubCheckbox(explode, "Anti-Weakness", false);
    public static SubMode breakHand = new SubMode(explode, "BreakHand", "MainHand", "OffHand", "Both", "GhostHand");

    public static Checkbox place = new Checkbox("Place", true);
    public static SubSlider placeRange = new SubSlider(place, "Place Range", 0.0D, 5.0D, 7.0D, 1);
    public static SubSlider enemyRange = new SubSlider(place, "Enemy Range", 0.0D, 5.0D, 15.0D, 1);
    public static SubSlider placeDelay = new SubSlider(place, "Place Delay", 0.0D, 40.0D, 600.0D, 0);
    public static SubSlider minDamage = new SubSlider(place, "Minimum Damage", 0.0D, 7.0D, 36.0D, 0);
    public static SubSlider resetThreshold = new SubSlider(place, "Reset Threshold", 0.0D, 3.0D, 10.0D, 1);
    public static SubCheckbox packetPlace = new SubCheckbox(place, "Packet Place", true);
    public static SubCheckbox prediction = new SubCheckbox(place, "Prediction", true);
    public static SubCheckbox walls = new SubCheckbox(place, "Through Walls", true);
    public static SubCheckbox autoSwitch = new SubCheckbox(place, "Auto-Switch", false);
    public static SubCheckbox rayTrace = new SubCheckbox(place, "Ray-Trace", true);
    public static SubCheckbox multiPlace = new SubCheckbox(place, "MultiPlace", false);
    public static SubCheckbox multiPlaceInHole = new SubCheckbox(place, "MultiPlace in Hole", false);

    public static Checkbox rotate = new Checkbox("Rotate", true);
    public static SubMode rotateMode = new SubMode(rotate, "Mode", "Packet", "Face", "Legit", "None");
    public static SubSlider rotateDelay = new SubSlider(rotate, "Rotation Delay", 0.0D, 0.0D, 5000.0D, 0);
    public static SubCheckbox onlyInViewFrustrum = new SubCheckbox(rotate, "Only In View Frustrum", false);
    public static SubCheckbox randomRotate = new SubCheckbox(rotate, "Random Rotations", false);

    public static Checkbox pause = new Checkbox("Pause", true);
    public static SubMode pauseMode = new SubMode(pause, "Mode", "Place", "Break", "Both");
    public static SubSlider pauseHealth = new SubSlider(pause, "Pause Health", 0.0D, 7.0D, 36.0D, 0);
    public static SubCheckbox pastDistance = new SubCheckbox(pause, "Past Damage Distance", false);
    public static SubCheckbox whenMining = new SubCheckbox(pause, "When Mining", false);
    public static SubCheckbox whenEating = new SubCheckbox(pause, "When Eating", false);
    public static SubCheckbox closePlacements = new SubCheckbox(pause, "Close Placements", false);

    public static Checkbox facePlace = new Checkbox("FacePlace", true);
    public static SubSlider facePlaceHealth = new SubSlider(facePlace, "FacePlace Health", 0.0D, 16.0D, 36.0D, 0);
    public static SubCheckbox armorMelt = new SubCheckbox(facePlace, "Armor Melt", false);
    public static SubSlider armorDurability = new SubSlider(facePlace, "Armor Durability", 0.0D, 15.0D, 100.0D, 0);
    public static SubCheckbox facePlaceInHole = new SubCheckbox(facePlace, "FacePlace in Hole", false);
    public static SubKeybind forceFaceplace = new SubKeybind(facePlace, "Force FacePlace", -2);

    public static Checkbox calculations = new Checkbox("Calculations", true);
    public static SubMode placeCalc = new SubMode(calculations, "Place Calculation", "Ideal", "Actual");
    public static SubMode verifyCalc = new SubMode(calculations, "Verify Calculation", "None", "Check");
    public static SubMode damageCalc = new SubMode(calculations, "Damage Calculation", "Full", "Semi");
    public static SubCheckbox taiwanTick = new SubCheckbox(calculations, "Taiwan-Tick", false);

    public static Checkbox logic = new Checkbox("Logic", true);
    public static SubMode logicMode = new SubMode(logic, "Crystal Logic", "Break -> Place", "Place -> Break");
    public static SubMode blockCalc = new SubMode(logic, "Block Logic", "Normal", "1.13+");

    public static Checkbox renderCrystal = new Checkbox("Render", true);
    public static SubColor colorPicker = new SubColor(renderCrystal, new Color(250, 0, 250, 50));
    public static SubCheckbox renderDamage = new SubCheckbox(renderCrystal, "Render Damage", true);
    public static SubCheckbox outline = new SubCheckbox(renderCrystal, "Outline", false);

    @Override
    public void setup() {
        addSetting(explode);
        addSetting(place);
        addSetting(rotate);
        addSetting(facePlace);
        addSetting(pause);
        addSetting(calculations);
        addSetting(logic);
        addSetting(renderCrystal);
    }

    Timer breakTimer = new Timer();
    Timer placeTimer = new Timer();
    Timer rotationTimer = new Timer();
    EntityPlayer currentTarget = null;
    EntityEnderCrystal crystal = null;
    Rotation crystalRotation = null;
    BlockPos placePos;
    double placeDamage;
    List<BlockPos> placedCrystals = new ArrayList<>();

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        super.onEnable();

        placedCrystals.clear();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        placedCrystals.clear();
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        currentTarget = WorldUtil.getClosestPlayer(enemyRange.getValue());

        if (currentTarget != null && (!FriendManager.isFriend(currentTarget.getName()) && FriendManager.isFriendModuleEnabled()) && rotationTimer.passed((long) rotateDelay.getValue(), Timer.Format.System)) {
            crystalRotation = randomRotate.getValue() ? new Rotation(new Random().nextInt(360), RotationUtil.getAngles(currentTarget)[1]) : new Rotation(RotationUtil.getAngles(currentTarget)[0], RotationUtil.getAngles(currentTarget)[1]);
                
            RotationUtil.updateRotations(crystalRotation, rotateMode.getValue());
            rotationTimer.reset();
        }

        if (!taiwanTick.getValue())
            autoCrystal();
    }

    @Override
    public void onFastUpdate() {
        if (nullCheck())
            return;

        if (taiwanTick.getValue())
            autoCrystal();
    }

    public void autoCrystal() {
        switch (logicMode.getValue()) {
            case 0:
                breakCrystal();
                placeCrystal();
                break;
            case 1:
                placeCrystal();
                breakCrystal();
                break;
        }
    }

    public void breakCrystal() {
        crystal = (EntityEnderCrystal) mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).filter(entity -> CrystalUtil.attackCheck(entity, breakMode.getValue(), breakRange.getValue(), placedCrystals)).min(Comparator.comparing(c -> mc.player.getDistance(c))).orElse(null);

        if (crystal != null && breakTimer.passed((long) breakDelay.getValue(), Timer.Format.System)) {
            if (crystal.getDistance(mc.player) > (!mc.player.canEntityBeSeen(crystal) ? wallRange.getValue() : breakRange.getValue()))
                return;

            if (pause.getValue() && PlayerUtil.getHealth() <= pauseHealth.getValue() && (pauseMode.getValue() == 1 || pauseMode.getValue() == 2))
                return;

            if (closePlacements.getValue() && mc.player.getDistance(crystal) < 1.5)
                return;

            if (!RotationUtil.isInViewFrustrum(crystal) && onlyInViewFrustrum.getValue())
                return;

            if (antiWeakness.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS))
                InventoryUtil.switchToSlot(Items.DIAMOND_SWORD);

            if (explode.getValue())
                for (int i = 0; i < breakAttempts.getValue(); i++)
                    CrystalUtil.attackCrystal(crystal, packetBreak.getValue());

            CrystalUtil.swingArm(breakHand.getValue());
            if (syncBreak.getValue())
                crystal.setDead();

            if (unload.getValue()) {
                mc.world.removeAllEntities();
                mc.world.getLoadedEntityList();
            }

            mc.world.loadedEntityList.stream().filter(entity -> entity.getDistance(crystal) <= breakMode.getValue()).forEach(entity -> {
                if (damageSync.getValue())
                    entity.attackEntityFrom(DamageSource.causeExplosionDamage(new Explosion(mc.world, crystal, crystal.posX, crystal.posY, crystal.posZ, 6.0f, false, true)), 8);
            });
        }

        breakTimer.reset();

        if (!multiPlace.getValue() || (multiPlaceInHole.getValue() && HoleUtil.isInHole(mc.player)))
            return;
    }

    public void placeCrystal() {
        BlockPos tempPos = null;
        List<Entity> entities = mc.world.playerEntities.stream().collect(Collectors.toList());
        double tempDamage = 0.5;

        for (Entity entityTarget : entities) {
            if (entityTarget != mc.player) {
                if (EnemyUtil.getHealth((EntityPlayer) entityTarget) <= 0 && !(damageCalc.getValue() == 1))
                    continue;

                if (mc.player.getDistanceSq(entityTarget) > enemyRange.getValue() * enemyRange.getValue())
                    continue;

                if (FriendManager.isFriend(entityTarget.getName()) && FriendManager.isFriendModuleEnabled())
                    continue;

                for (BlockPos calculatedPos : CrystalUtil.crystalBlocks(mc.player, placeRange.getValue(), prediction.getValue(), blockCalc.getValue())) {
                    if (!CrystalUtil.canBlockBeSeen(calculatedPos) && mc.player.getDistanceSq(calculatedPos) > MathUtil.square(wallRange.getValue()))
                        continue;
                    
                    if (entityTarget.getDistanceSq(calculatedPos) > 56.2 && pastDistance.getValue())
                        continue;

                    if (verifyCalc.getValue() == 1 && mc.player.getDistanceSq(calculatedPos) > MathUtil.square(breakRange.getValue()))
                        continue;

                    double calculatedDamage;
                    if (placeCalc.getValue() == 1)
                        calculatedDamage = CrystalUtil.calculateDamage(calculatedPos.getX(), calculatedPos.getY() + 1, calculatedPos.getZ(), entityTarget);
                    else
                        calculatedDamage = CrystalUtil.calculateDamage(calculatedPos.getX() + 0.5, calculatedPos.getY() + 1, calculatedPos.getZ() + 0.5, entityTarget);

                    int minDamagePlace;
                    if (EnemyUtil.getArmor((EntityPlayer) entityTarget, armorMelt.getValue(), armorDurability.getValue()))
                        minDamagePlace = 2;
                    else if (HoleUtil.isInHole((EntityPlayer) entityTarget) && facePlaceInHole.getValue())
                        minDamagePlace = 2;
                    else
                        minDamagePlace = (int) minDamage.getValue();
                    if (calculatedDamage < minDamagePlace && EnemyUtil.getHealth((EntityPlayer) entityTarget) > facePlaceHealth.getValue())
                        continue;

                    if (calculatedDamage <= placeDamage + resetThreshold.getValue() && calculatedDamage >= placeDamage - resetThreshold.getValue()) {
                        tempDamage = placeDamage;
                        tempPos = placePos;
                        continue;
                    }

                    if (calculatedDamage <= tempDamage && !(damageCalc.getValue() == 1))
                        continue;

                    double selfDamage = CrystalUtil.calculateDamage(calculatedPos.getX() + 0.5, calculatedPos.getY() + 1, calculatedPos.getZ() + 0.5, mc.player);
                    if (PlayerUtil.getHealth() - selfDamage <= pauseHealth.getValue() && pause.getValue() && (pauseMode.getValue() == 0 || pauseMode.getValue() == 2))
                        continue;

                    if (selfDamage > calculatedDamage)
                        continue;

                    tempDamage = calculatedDamage;
                    tempPos = calculatedPos;
                }
            }
        }

        if (tempDamage == 0.5) {
            placePos = null;
            return;
        }

        placePos = tempPos;
        placeDamage = tempDamage;

        if (autoSwitch.getValue() && (!InventoryUtil.getHeldItem(Items.GOLDEN_APPLE) && whenEating.getValue()) && (!InventoryUtil.getHeldItem(Items.DIAMOND_PICKAXE) && whenMining.getValue()))
            InventoryUtil.switchToSlot(Items.END_CRYSTAL);

        if (placeTimer.passed((long) placeDelay.getValue(), Timer.Format.System) && place.getValue() && InventoryUtil.getHeldItem(Items.END_CRYSTAL) && placePos != null) {
            CrystalUtil.placeCrystal(placePos, rayTrace.getValue() ? CrystalUtil.getEnumFacing(rayTrace.getValue(), placePos) : EnumFacing.UP, packetPlace.getValue());
            placedCrystals.add(placePos);
        }

        placeTimer.reset();
    }
    
    @SubscribeEvent
    public void onRotation(RotationEvent event) {
        if (crystalRotation != null && rotateMode.getValue() == 0) {
            event.setCanceled(true);
            event.setPitch(crystalRotation.yaw);
            event.setYaw(crystalRotation.pitch);
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (renderCrystal.getValue() && placePos != null) {
            RenderUtil.drawBoxBlockPos(placePos, 0, colorPicker.getColor(), outline.getValue() ? RenderBuilder.renderMode.Both : RenderBuilder.renderMode.Fill);

            if (renderDamage.getValue())
                RenderUtil.drawNametagFromBlockPos(placePos, String.valueOf(MathUtil.roundAvoid(placeDamage, 1)));
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            if (((SPacketSoundEffect) event.getPacket()).category == SoundCategory.BLOCKS && ((SPacketSoundEffect) event.getPacket()).sound == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                mc.world.loadedEntityList.stream().filter(entity -> entity.getDistance(((SPacketSoundEffect) event.getPacket()).getX(), ((SPacketSoundEffect) event.getPacket()).getY(), ((SPacketSoundEffect) event.getPacket()).getZ()) <= breakRange.getValue()).filter(entity -> entity instanceof EntityEnderCrystal).forEach(entity -> {
                    placedCrystals.removeIf(calculatedPos -> calculatedPos.getDistance((int) ((SPacketSoundEffect) event.getPacket()).getX(), (int)  ((SPacketSoundEffect) event.getPacket()).getY(), (int) ((SPacketSoundEffect) event.getPacket()).getZ()) <= breakRange.getValue());
                });
            }
        }
    }

    @Override
    public String getHUDData() {
        return currentTarget != null ? " " + currentTarget.getName() : " None";
    }
}
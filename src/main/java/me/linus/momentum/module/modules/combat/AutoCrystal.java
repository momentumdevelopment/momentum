package me.linus.momentum.module.modules.combat;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.event.events.player.RotationEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.keybind.SubKeybind;
import me.linus.momentum.setting.mode.SubMode;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.combat.crystal.CrystalPosition;
import me.linus.momentum.util.render.builder.RenderBuilder;
import me.linus.momentum.util.world.Timer;
import me.linus.momentum.util.combat.crystal.CrystalUtil;
import me.linus.momentum.util.combat.EnemyUtil;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.player.PlayerUtil;
import me.linus.momentum.util.player.rotation.Rotation;
import me.linus.momentum.util.player.rotation.RotationUtil;
import me.linus.momentum.util.render.builder.RenderUtil;
import me.linus.momentum.util.world.*;
import me.linus.momentum.util.world.BlockUtil;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author linustouchtips
 * @since 11/24/2020
 */

public class AutoCrystal extends Module {
    public AutoCrystal() {
        super("AutoCrystal", Category.COMBAT, "Automatically places and explodes crystals");
    }

    public static Checkbox explode = new Checkbox("Break", true);
    public static SubMode breakMode = new SubMode(explode, "Mode", "All", "Only Own");
    public static SubSlider breakRange = new SubSlider(explode, "Break Range", 0.0D, 5.0D, 7.0D, 1);
    public static SubSlider breakDelay = new SubSlider(explode, "Break Delay", 0.0D, 20.0D, 60.0D, 0);
    public static SubSlider breakAttempts = new SubSlider(explode, "Break Attempts", 0.0D, 1.0D, 5.0D, 0);
    public static SubCheckbox packetBreak = new SubCheckbox(explode, "Packet Break", true);
    public static SubCheckbox walls = new SubCheckbox(explode, "Through Walls", true);
    public static SubCheckbox syncBreak = new SubCheckbox(explode, "Sync Break", true);
    public static SubCheckbox unsafeSync = new SubCheckbox(explode, "Unsafe Sync", false);
    public static SubCheckbox unload = new SubCheckbox(explode, "Unload Crystal", true);
    public static SubCheckbox damageSync = new SubCheckbox(explode, "Damage Sync", false);
    public static SubCheckbox antiWeakness = new SubCheckbox(explode, "Anti-Weakness", false);
    public static SubMode breakHand = new SubMode(explode, "BreakHand", "MainHand", "OffHand", "Both", "MultiSwing");

    public static Checkbox place = new Checkbox("Place", true);
    public static SubSlider placeRange = new SubSlider(place, "Place Range", 0.0D, 5.0D, 7.0D, 1);
    public static SubSlider enemyRange = new SubSlider(place, "Enemy Range", 0.0D, 5.0D, 15.0D, 1);
    public static SubSlider wallRange = new SubSlider(place, "Walls Range", 0.0D, 3.0D, 7.0D, 1);
    public static SubSlider placeDelay = new SubSlider(place, "Place Delay", 0.0D, 40.0D, 500.0D, 0);
    public static SubSlider minDamage = new SubSlider(place, "Minimum Damage", 0.0D, 7.0D, 36.0D, 0);
    public static SubSlider resetThreshold = new SubSlider(place, "Reset Threshold", 0.0D, 3.0D, 10.0D, 1);
    public static SubMode autoSwitch = new SubMode(place, "Auto-Switch", "None", "Normal", "Packet");
    public static SubCheckbox packetPlace = new SubCheckbox(place, "Packet Place", true);
    public static SubCheckbox prediction = new SubCheckbox(place, "Prediction", true);
    public static SubCheckbox rayTrace = new SubCheckbox(place, "Ray-Trace", true);
    public static SubCheckbox antiSurround = new SubCheckbox(place, "Anti-Surround", true);
    public static SubCheckbox multiPlace = new SubCheckbox(place, "MultiPlace", false);
    public static SubCheckbox multiPlaceInHole = new SubCheckbox(place, "MultiPlace in Hole", false);

    public static Checkbox rotate = new Checkbox("Rotate", true);
    public static SubMode rotateMode = new SubMode(rotate, "Mode", "Packet", "Legit", "None");
    public static SubSlider rotateDelay = new SubSlider(rotate, "Rotation Delay", 0.0D, 0.0D, 5000.0D, 0);
    public static SubCheckbox onlyInViewFrustrum = new SubCheckbox(rotate, "Only In View Frustrum", false);
    public static SubCheckbox randomRotate = new SubCheckbox(rotate, "Random Rotations", false);

    public static Checkbox pause = new Checkbox("Pause", true);
    public static SubMode pauseMode = new SubMode(pause, "Mode", "Place", "Break", "Both");
    public static SubMode friendProtect = new SubMode(pause, "Friend Protect", "None", "Place", "Break");
    public static SubSlider pauseHealth = new SubSlider(pause, "Pause Health", 0.0D, 7.0D, 36.0D, 0);
    public static SubCheckbox whenMining = new SubCheckbox(pause, "When Mining", false);
    public static SubCheckbox whenEating = new SubCheckbox(pause, "When Eating", false);
    public static SubCheckbox closePlacements = new SubCheckbox(pause, "Close Placements", false);

    public static Checkbox facePlace = new Checkbox("FacePlace", true);
    public static SubSlider facePlaceHealth = new SubSlider(facePlace, "FacePlace Health", 0.0D, 16.0D, 36.0D, 0);
    public static SubCheckbox armorMelt = new SubCheckbox(facePlace, "Armor Melt", false);
    public static SubSlider armorDurability = new SubSlider(facePlace, "Armor Durability", 0.0D, 15.0D, 100.0D, 0);
    public static SubCheckbox facePlaceInHole = new SubCheckbox(facePlace, "FacePlace HoleCampers", false);
    public static SubKeybind forceFaceplace = new SubKeybind(facePlace, "Force FacePlace", -2);

    public static Checkbox calculations = new Checkbox("Calculations", true);
    public static SubMode heuristic = new SubMode(calculations, "Heuristic", "Damage", "MiniMax", "Atomic");
    public static SubCheckbox serverConfirm = new SubCheckbox(calculations, "Server Confirm", true);
    public static SubCheckbox verifyPlace = new SubCheckbox(calculations, "Verify Placements", false);
    public static SubCheckbox taiwanTick = new SubCheckbox(calculations, "Taiwan-Tick", false);

    public static Checkbox logic = new Checkbox("Logic", true);
    public static SubMode logicMode = new SubMode(logic, "Crystal Logic", "Break -> Place", "Place -> Break");
    public static SubMode blockCalc = new SubMode(logic, "Block Logic", "Normal", "1.13+");

    public static Checkbox renderCrystal = new Checkbox("Render", true);
    public static ColorPicker colorPicker = new ColorPicker(renderCrystal, new Color(250, 0, 250, 50));
    public static SubMode renderMode = new SubMode(renderCrystal, "Mode", "Fill", "Outline", "Both");
    public static SubCheckbox renderDamage = new SubCheckbox(renderCrystal, "Render Damage", true);

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
    CrystalPosition crystalPosition = new CrystalPosition(BlockPos.ORIGIN, 0, 0);
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

        if (currentTarget != null && rotationTimer.passed((long) rotateDelay.getValue(), Timer.Format.System)) {
            if (!mc.player.canEntityBeSeen(crystal) && onlyInViewFrustrum.getValue())
                return;

            crystalRotation = randomRotate.getValue() ? new Rotation(new Random().nextInt(360), RotationUtil.getAngles(crystal)[1]) : new Rotation(RotationUtil.getAngles(crystal)[0], RotationUtil.getAngles(crystal)[1]);
            crystalRotation.updateRotations(rotateMode.getValue());
            rotationTimer.reset();
        }

        if (PlayerUtil.isEating() && whenEating.getValue() || PlayerUtil.isMining() && whenMining.getValue())
            return;

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
            if (crystal.getDistance(mc.player) > breakRange.getValue())
                return;

            if (!mc.player.canEntityBeSeen(crystal) && !walls.getValue())
                return;

            if (pause.getValue() && PlayerUtil.getHealth() <= pauseHealth.getValue() && (pauseMode.getValue() == 1 || pauseMode.getValue() == 2))
                return;

            if (closePlacements.getValue() && mc.player.getDistance(crystal) < 1.5)
                return;

            for (EntityPlayer friend : WorldUtil.getNearbyFriends(placeRange.getValue()))
                if (EnemyUtil.getHealth(friend) - (CrystalUtil.calculateDamage(crystal.posX + 0.5, crystal.posY + 1, crystal.posZ + 0.5, friend)) <= pauseHealth.getValue() && friendProtect.getValue() == 0)
                    return;

            if (antiWeakness.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS))
                InventoryUtil.switchToSlot(Items.DIAMOND_SWORD);

            if (explode.getValue())
                for (int i = 0; i < breakAttempts.getValue(); i++)
                    CrystalUtil.attackCrystal(crystal, packetBreak.getValue());

            CrystalUtil.swingArm(breakHand.getValue());

            if (unsafeSync.getValue())
                crystal.setDead();

            if (!serverConfirm.getValue())
                placedCrystals.remove(crystal.getPosition());

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
        CrystalPosition tempPosition = new CrystalPosition(BlockPos.ORIGIN, 0, 0);

        for (EntityPlayer calculatedPlayer : WorldUtil.getNearbyPlayers(enemyRange.getValue())) {
            for (BlockPos calculatedPosition : CrystalUtil.crystalBlocks(mc.player, placeRange.getValue(), prediction.getValue(), antiSurround.getValue(), blockCalc.getValue())) {
                if (!BlockUtil.canBlockBeSeen(calculatedPosition) && mc.player.getDistanceSq(calculatedPosition) > MathUtil.square(wallRange.getValue()))
                    continue;

                if (verifyPlace.getValue() && mc.player.getDistanceSq(calculatedPosition) > MathUtil.square(breakRange.getValue()))
                    continue;

                double calculatedTargetDamage = CrystalUtil.calculateDamage(calculatedPosition.getX() + 0.5, calculatedPosition.getY() + 1, calculatedPosition.getZ() + 0.5, calculatedPlayer);
                double calculatedSelfDamage = CrystalUtil.calculateDamage(calculatedPosition.getX() + 0.5, calculatedPosition.getY() + 1, calculatedPosition.getZ() + 0.5, mc.player);
                double heuristicDamage = CrystalUtil.getHeuristic(calculatedPosition, calculatedTargetDamage, calculatedSelfDamage, heuristic.getValue());

                if (heuristicDamage < minDamage.getValue() && (!EnemyUtil.getArmor(calculatedPlayer, armorMelt.getValue(), armorDurability.getValue()) || !HoleUtil.isInHole(calculatedPlayer) && facePlaceInHole.getValue() || EnemyUtil.getHealth(calculatedPlayer) > facePlaceHealth.getValue()))
                    continue;

                if (PlayerUtil.getHealth() - calculatedSelfDamage <= pauseHealth.getValue() && pause.getValue() && (pauseMode.getValue() == 0 || pauseMode.getValue() == 2))
                    continue;

                if (calculatedSelfDamage > heuristicDamage)
                    continue;

                if (heuristicDamage > tempPosition.getTargetDamage() - resetThreshold.getValue())
                    tempPosition = new CrystalPosition(calculatedPosition, heuristicDamage, calculatedSelfDamage);

                for (EntityPlayer friend : WorldUtil.getNearbyFriends(placeRange.getValue()))
                    if (EnemyUtil.getHealth(friend) - (CrystalUtil.calculateDamage(calculatedPosition.getX() + 0.5, calculatedPosition.getY() + 1, calculatedPosition.getZ() + 0.5, friend)) <= pauseHealth.getValue() && friendProtect.getValue() == 1)
                        tempPosition = new CrystalPosition(BlockPos.ORIGIN, 0, 0);
            }

            currentTarget = calculatedPlayer;
        }

        if (tempPosition == new CrystalPosition(BlockPos.ORIGIN, 0, 0)) {
            currentTarget = null;
            crystalRotation.restoreRotation();
            return;
        }

        crystalPosition = tempPosition;

        switch (autoSwitch.getValue()) {
            case 0:
                break;
            case 1:
                InventoryUtil.switchToSlot(Items.END_CRYSTAL);
                break;
            case 2:
                InventoryUtil.switchToSlotGhost(Items.END_CRYSTAL);
                break;
        }

        if (placeTimer.passed((long) placeDelay.getValue(), Timer.Format.System) && place.getValue() && InventoryUtil.getHeldItem(Items.END_CRYSTAL) && crystalPosition.getCrystalPosition() != null) {
            CrystalUtil.placeCrystal(crystalPosition.getCrystalPosition(), rayTrace.getValue() ? CrystalUtil.getEnumFacing(rayTrace.getValue(), crystalPosition.getCrystalPosition()) : EnumFacing.UP, packetPlace.getValue());
            placedCrystals.add(crystalPosition.getCrystalPosition());
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
        if (renderCrystal.getValue() && crystalPosition.getCrystalPosition() != null) {
            switch (renderMode.getValue()) {
                case 0:
                    RenderUtil.drawBoxBlockPos(crystalPosition.getCrystalPosition(), 0, colorPicker.getColor(), RenderBuilder.renderMode.Fill);
                    break;
                case 1:
                    RenderUtil.drawBoxBlockPos(crystalPosition.getCrystalPosition(), 0, colorPicker.getColor(), RenderBuilder.renderMode.Outline);
                    break;
                case 2:
                    RenderUtil.drawBoxBlockPos(crystalPosition.getCrystalPosition(), 0, colorPicker.getColor(), RenderBuilder.renderMode.Both);
                    break;
            }

            if (renderDamage.getValue())
                RenderUtil.drawNametagFromBlockPos(crystalPosition.getCrystalPosition(), 0.5f,  String.valueOf(MathUtil.roundAvoid(crystalPosition.getTargetDamage(), 1)));
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            if (((SPacketSoundEffect) event.getPacket()).category == SoundCategory.BLOCKS && ((SPacketSoundEffect) event.getPacket()).sound == SoundEvents.ENTITY_GENERIC_EXPLODE && serverConfirm.getValue()) {
                mc.world.loadedEntityList.stream().filter(entity -> entity.getDistance(((SPacketSoundEffect) event.getPacket()).getX(), ((SPacketSoundEffect) event.getPacket()).getY(), ((SPacketSoundEffect) event.getPacket()).getZ()) <= breakRange.getValue()).filter(entity -> entity instanceof EntityEnderCrystal).forEach(entity -> {
                    placedCrystals.removeIf(calculatedPosition -> calculatedPosition.getDistance((int) ((SPacketSoundEffect) event.getPacket()).getX(), (int)  ((SPacketSoundEffect) event.getPacket()).getY(), (int) ((SPacketSoundEffect) event.getPacket()).getZ()) <= breakRange.getValue());
                });
            }
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketPlayer && crystalRotation != null && crystalRotation.requiresUpdate && rotateMode.getValue() == 0) {
            ((CPacketPlayer) event.getPacket()).yaw = crystalRotation.yaw;
            ((CPacketPlayer) event.getPacket()).pitch = crystalRotation.pitch;
        }

        if (event.getPacket() instanceof CPacketUseEntity && ((CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && ((CPacketUseEntity) event.getPacket()).getEntityFromWorld(mc.world) instanceof EntityEnderCrystal && syncBreak.getValue())
            ((CPacketUseEntity) event.getPacket()).getEntityFromWorld(mc.world).setDead();
    }

    @Override
    public String getHUDData() {
        return currentTarget != null ? " " + currentTarget.getName() : " None";
    }
}
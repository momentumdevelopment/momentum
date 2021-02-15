package me.linus.momentum.module.modules.combat;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.managers.CrystalManager;
import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.managers.RotationManager;
import me.linus.momentum.managers.notification.Notification;
import me.linus.momentum.managers.notification.NotificationManager;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.keybind.SubKeybind;
import me.linus.momentum.setting.mode.SubMode;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.player.rotation.RotationPriority;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.render.builder.RenderBuilder;
import me.linus.momentum.util.world.Timer;
import me.linus.momentum.util.combat.crystal.*;
import me.linus.momentum.util.combat.EnemyUtil;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.player.PlayerUtil;
import me.linus.momentum.util.player.rotation.Rotation;
import me.linus.momentum.util.player.rotation.RotationUtil;
import me.linus.momentum.util.world.*;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author linustouchtips
 * @since 11/24/2020
 */

// TODO: break calc & better wall placements
public class AutoCrystal extends Module {
    public AutoCrystal() {
        super("AutoCrystal", Category.COMBAT, "Automatically places and explodes crystals");
    }

    public static Checkbox explode = new Checkbox("Break", true);
    public static SubMode breakMode = new SubMode(explode, "Mode", "All");
    public static SubMode breakHand = new SubMode(explode, "BreakHand", "OffHand", "MainHand", "Both", "MultiSwing");
    public static SubSlider breakRange = new SubSlider(explode, "Break Range", 0.0D, 5.0D, 7.0D, 1);
    public static SubSlider breakDelay = new SubSlider(explode, "Break Delay", 0.0D, 80.0D, 200.0D, 0);
    public static SubSlider breakAttempts = new SubSlider(explode, "Break Attempts", 0.0D, 1.0D, 5.0D, 0);
    public static SubMode sync = new SubMode(explode, "Sync", "None", "Instant", "Attack", "Sound", "Unsafe");
    public static SubCheckbox packetBreak = new SubCheckbox(explode, "Packet Break", true);
    public static SubCheckbox walls = new SubCheckbox(explode, "Through Walls", true);
    public static SubCheckbox sequential = new SubCheckbox(explode, "Sequential", true);
    public static SubCheckbox inhibit = new SubCheckbox(explode, "Inhibit", true);
    public static SubCheckbox antiWeakness = new SubCheckbox(explode, "Anti-Weakness", false);

    public static Checkbox place = new Checkbox("Place", true);
    public static SubSlider placeRange = new SubSlider(place, "Place Range", 0.0D, 5.0D, 7.0D, 1);
    public static SubSlider enemyRange = new SubSlider(place, "Enemy Range", 0.0D, 10.0D, 15.0D, 1);
    public static SubSlider wallRange = new SubSlider(place, "Walls Range", 0.0D, 3.0D, 7.0D, 1);
    public static SubSlider placeDelay = new SubSlider(place, "Place Delay", 0.0D, 0.0D, 500.0D, 0);
    public static SubSlider minDamage = new SubSlider(place, "Minimum Damage", 0.0D, 7.0D, 36.0D, 0);
    public static SubSlider maxLocalDamage = new SubSlider(place, "Maximum Local Damage", 0.0D, 8.0D, 36.0D, 0);
    public static SubSlider threshold = new SubSlider(place, "Threshold", 0.0D, 0.0D, 10.0D, 0);
    public static SubMode autoSwitch = new SubMode(place, "Switch", "None", "Normal", "Packet");
    public static SubCheckbox packetPlace = new SubCheckbox(place, "Packet Place", true);
    public static SubCheckbox prediction = new SubCheckbox(place, "Prediction", true);
    public static SubCheckbox rayTrace = new SubCheckbox(place, "Ray-Trace", true);
    public static SubCheckbox multiPlace = new SubCheckbox(place, "MultiPlace", false);

    public static Checkbox rotate = new Checkbox("Rotate", true);
    public static SubMode rotateDuring = new SubMode(rotate, "When", "Break", "Place", "Both");
    public static SubMode rotateMode = new SubMode(rotate, "Type", "Packet", "Legit", "None");
    public static SubMode rotateType = new SubMode(rotate, "Render", "Full", "Head", "NoRender");
    public static SubSlider rotateStep = new SubSlider(rotate, "Rotation Step", 0.0D, 180.0D, 360.0D, 0);
    public static SubCheckbox onlyInViewFrustrum = new SubCheckbox(rotate, "Only In View Frustrum", false);
    public static SubCheckbox randomRotate = new SubCheckbox(rotate, "Random Rotations", false);
    public static SubCheckbox strict = new SubCheckbox(rotate, "NCP Strict", false);

    public static Checkbox pause = new Checkbox("Pause", true);
    public static SubMode pauseMode = new SubMode(pause, "Pause Mode", "Place", "Break", "Both");
    public static SubMode friendProtect = new SubMode(pause, "Friend Protect", "Ignore", "Place", "Break");
    public static SubSlider pauseHealth = new SubSlider(pause, "Pause Health", 0.0D, 7.0D, 36.0D, 0);
    public static SubCheckbox whenMining = new SubCheckbox(pause, "When Mining", false);
    public static SubCheckbox whenEating = new SubCheckbox(pause, "When Eating", false);
    public static SubCheckbox closePlacements = new SubCheckbox(pause, "Close Placements", false);

    public static Checkbox facePlace = new Checkbox("FacePlace", true);
    public static SubSlider facePlaceHealth = new SubSlider(facePlace, "FacePlace Health", 0.0D, 16.0D, 36.0D, 0);
    public static SubCheckbox armorBreaker = new SubCheckbox(facePlace, "Armor Breaker", false);
    public static SubSlider armorScale = new SubSlider(facePlace, "Armor Scale", 0.0D, 15.0D, 100.0D, 0);
    public static SubCheckbox gearPlay = new SubCheckbox(facePlace, "Gear Play", false);
    public static SubSlider expScale = new SubSlider(facePlace, "EXP Scale", 0.0D, 1.0D, 6.0D, 0);
    public static SubCheckbox facePlaceInHole = new SubCheckbox(facePlace, "FacePlace HoleCampers", false);
    public static SubKeybind forceFaceplace = new SubKeybind(facePlace, "Force FacePlace", Keyboard.KEY_O);

    public static Checkbox calculations = new Checkbox("Calculations", true);
    public static SubMode heuristic = new SubMode(calculations, "Heuristic", "Damage", "MiniMax", "Atomic");
    public static SubCheckbox serverConfirm = new SubCheckbox(calculations, "Server Confirm", true);
    public static SubCheckbox verifyPlace = new SubCheckbox(calculations, "Verify Placements", false);
    public static SubCheckbox taiwanTick = new SubCheckbox(calculations, "Taiwan-Tick", false);

    public static Checkbox logic = new Checkbox("Logic", true);
    public static SubMode logicMode = new SubMode(logic, "Crystal Logic", "Break -> Place", "Place -> Break");
    public static SubMode blockCalc = new SubMode(logic, "Block Logic", "Normal", "1.13+");
    public static SubMode enemyLogic = new SubMode(logic, "Enemy Logic", "Closest", "LowestHealth", "LowestArmor");

    public static Checkbox renderCrystal = new Checkbox("Render", true);
    public static ColorPicker colorPicker = new ColorPicker(renderCrystal, "Color Picker", new Color(250, 0, 250, 50));
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

    public static Timer breakTimer = new Timer();
    public static Timer placeTimer = new Timer();
    public static EntityPlayer crystalTarget = null;
    public static Rotation crystalRotation = null;
    public static Crystal crystal = null;
    public static CrystalPosition crystalPosition = new CrystalPosition(BlockPos.ORIGIN, 0, 0);

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        CrystalManager.swings = 0;
        super.onEnable();
        CrystalManager.placedCrystals.clear();
    }

    @Override
    public void onDisable() {
        CrystalManager.swings = 0;
        super.onDisable();
        CrystalManager.placedCrystals.clear();
    }

    @Override
    public void onUpdate() {
        if (nullCheck()) {
            this.disable();
            return;
        }

        crystalTarget = WorldUtil.getTarget(enemyRange.getValue(), enemyLogic.getValue());

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
        if (handlePause() && (pauseMode.getValue() == 0 || pauseMode.getValue() == 2))
            return;

        EntityEnderCrystal calculatedCrystal = (EntityEnderCrystal) mc.world.loadedEntityList.stream().filter(entity -> CrystalUtil.attackCheck(entity)).min(Comparator.comparing(crystal -> crystalTarget.getDistance(crystal))).orElse(null);
        double calculatedTargetDamage = CrystalUtil.calculateDamage(calculatedCrystal.posX + 0.5, calculatedCrystal.posY + 1, calculatedCrystal.posZ, crystalTarget);
        double calculatedSelfDamage = mc.player.capabilities.isCreativeMode ? 0 : CrystalUtil.calculateDamage(calculatedCrystal.posX + 0.5, calculatedCrystal.posY + 1, calculatedCrystal.posZ, mc.player);

        crystal = new Crystal(calculatedCrystal, calculatedTargetDamage, calculatedSelfDamage);
        if (crystal.getCrystal() != null && explode.getValue()) {
            if (crystal.getCrystal().getDistance(mc.player) > breakRange.getValue())
                return;

            if (!RaytraceUtil.raytraceEntity(crystal.getCrystal()) && !walls.getValue())
                return;

            if (antiWeakness.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS))
                InventoryUtil.switchToSlot(Items.DIAMOND_SWORD);

            if (breakTimer.passed(CrystalManager.skipTick ? 200 : (long) breakDelay.getValue(), Timer.Format.System)) {
                if (rotateDuring.getValue() == 0 || rotateDuring.getValue() == 2)
                    handleRotations();

                for (int i = 0; i < breakAttempts.getValue(); i++) {
                    CrystalUtil.attackCrystal(crystal.getCrystal(), packetBreak.getValue());
                    CrystalUtil.swingArm(breakHand.getValue());
                    CrystalManager.updateSwings();
                }

                if (sync.getValue() == 1)
                    crystal.getCrystal().setDead();

               breakTimer.reset();
            }

            if (!serverConfirm.getValue())
                CrystalManager.placedCrystals.remove(crystal.getCrystal().getPosition());
        }
    }

    public void placeCrystal() {
        if (handlePause() && (pauseMode.getValue() == 0 || pauseMode.getValue() == 2))
            return;

        List<CrystalPosition> crystalPositions = new ArrayList<>();
        CrystalPosition tempPosition;

        for (BlockPos calculatedPosition : CrystalUtil.crystalBlocks(mc.player, placeRange.getValue(), prediction.getValue(), !multiPlace.getValue(), blockCalc.getValue())) {
            if (!RaytraceUtil.raytraceBlock(calculatedPosition) && mc.player.getDistanceSq(calculatedPosition) > MathUtil.square(wallRange.getValue()))
                continue;

            if (verifyPlace.getValue() && mc.player.getDistanceSq(calculatedPosition) > MathUtil.square(breakRange.getValue()))
                continue;

            double calculatedTargetDamage = CrystalUtil.calculateDamage(calculatedPosition.getX() + 0.5, calculatedPosition.getY() + 1, calculatedPosition.getZ() + 0.5, crystalTarget);
            double calculatedSelfDamage = mc.player.capabilities.isCreativeMode ? 0 : CrystalUtil.calculateDamage(calculatedPosition.getX() + 0.5, calculatedPosition.getY() + 1, calculatedPosition.getZ() + 0.5, mc.player);

            if (calculatedTargetDamage < minDamage.getValue() && handleMinDamage())
                continue;

            if (calculatedSelfDamage > maxLocalDamage.getValue())
                continue;

            crystalPositions.add(new CrystalPosition(calculatedPosition, calculatedTargetDamage, calculatedSelfDamage));
        }

        tempPosition = crystalPositions.stream().max(Comparator.comparing(idealCrystalPosition -> CrystalUtil.getHeuristic(idealCrystalPosition, heuristic.getValue()))).orElse(null);

        if (tempPosition == null) {
            crystalTarget = null;
            crystalRotation.restoreRotation();
            return;
        }

        crystalPosition = tempPosition;

        switch (autoSwitch.getValue()) {
            case 1:
                InventoryUtil.switchToSlot(Items.END_CRYSTAL);
                break;
            case 2:
                InventoryUtil.switchToSlotGhost(Items.END_CRYSTAL);
                break;
        }

        if (placeTimer.passed((long) placeDelay.getValue(), Timer.Format.System) && place.getValue() && InventoryUtil.getHeldItem(Items.END_CRYSTAL) && crystalPosition.getCrystalPosition() != BlockPos.ORIGIN) {
            if (rotateDuring.getValue() == 1 || rotateDuring.getValue() == 2)
                handleRotations();

            CrystalUtil.placeCrystal(crystalPosition.getCrystalPosition(), CrystalUtil.getEnumFacing(rayTrace.getValue(), crystalPosition.getCrystalPosition()), packetPlace.getValue());
            placeTimer.reset();
        }
    }

    public boolean handlePause() {
        if (ModuleManager.getModuleByName("Surround").isEnabled() && !HoleUtil.isInHole(mc.player))
            return true;

        for (EntityPlayer friend : WorldUtil.getNearbyFriends(placeRange.getValue())) {
            if (EnemyUtil.getHealth(friend) - (CrystalUtil.calculateDamage(crystal.crystal.posX + 0.5, crystal.crystal.posY + 1, crystal.crystal.posZ + 0.5, friend)) <= pauseHealth.getValue() && friendProtect.getValue() == 0)
                return true;
        }

        if (PlayerUtil.getHealth() < pauseHealth.getValue() && (EnemyUtil.getHealth(crystalTarget) - crystalPosition.getTargetDamage() > threshold.getValue()))
            return true;
        else if (PlayerUtil.isEating() && whenEating.getValue() || PlayerUtil.isMining() && whenMining.getValue())
            return true;
        else if (closePlacements.getValue() && mc.player.getDistance(crystal.crystal) < 1.5)
            return true;
        if (CrystalManager.swings > 50 && inhibit.getValue()) {
            CrystalManager.updateTicks(true);

            NotificationManager.addNotification(new Notification("AutoCrystal Frozen! Pausing for 1 tick!", Notification.Type.Warning));

            crystal = null;
            if (sync.getValue() == 4)
                crystal.getCrystal().setDead();

            return false;
        }

        else
            return false;
    }

    public boolean handleMinDamage() {
        if (EnemyUtil.getHealth(crystalTarget) < facePlaceHealth.getValue())
            return false;
        else if (EnemyUtil.getArmor(crystalTarget, armorBreaker.getValue(), armorScale.getValue()))
            return false;
        else if (EnemyUtil.getGearPlay(crystalTarget, expScale.getValue()) && gearPlay.getValue())
            return false;
        else if (facePlaceInHole.getValue() && HoleUtil.isInHole(crystalTarget))
            return false;
        else if (Keyboard.isKeyDown(forceFaceplace.getKey()))
            return false;

        return true;
    }

    public void handleRotations() {
        if (rotateDuring.getValue() == 0 ? !RaytraceUtil.raytraceEntity(crystal.getCrystal()) : RaytraceUtil.raytraceBlock(crystalPosition.getCrystalPosition()) && onlyInViewFrustrum.getValue())
            return;

        float[] rotations = null;

        switch (rotateDuring.getValue()) {
            case 0:
                rotations = randomRotate.getValue() ? new float[] {(float) new Random().nextGaussian(), (float) new Random().nextGaussian()} : RotationUtil.getAngles(crystal.getCrystal());
                break;
            case 1:
            case 2:
                rotations = randomRotate.getValue() ? new float[] {(float) new Random().nextGaussian(), (float) new Random().nextGaussian()} : RotationUtil.getPositionAngles(crystalPosition.getCrystalPosition());
                break;
        }

        switch (rotateMode.getValue()) {
            case 0:
                Rotation packetRotation = new Rotation(rotations[0], rotations[1], Rotation.RotationMode.Packet, RotationPriority.Highest);
                crystalRotation = strict.getValue() ? RotationUtil.rotationStep(RotationManager.serverRotation, packetRotation, (float) rotateStep.getValue()) : packetRotation;
                break;
            case 1:
                Rotation legitRotation = new Rotation(rotations[0], rotations[1], Rotation.RotationMode.Legit, RotationPriority.Highest);
                crystalRotation = strict.getValue() ? RotationUtil.rotationStep(RotationManager.serverRotation, legitRotation, (float) rotateStep.getValue()) : legitRotation;
                break;
        }

        RotationManager.rotationQueue.add(crystalRotation);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
       try {
            if (renderCrystal.getValue() && crystalPosition.getCrystalPosition() != BlockPos.ORIGIN && crystalPosition.getCrystalPosition() != null && crystalPosition != null) {
                switch (renderMode.getValue()) {
                    case 0:
                        RenderUtil.drawBoxBlockPos(crystalPosition.getCrystalPosition(), 0, colorPicker.getColor(), RenderBuilder.RenderMode.Fill);
                        break;
                    case 1:
                        RenderUtil.drawBoxBlockPos(crystalPosition.getCrystalPosition(), 0, colorPicker.getColor(), RenderBuilder.RenderMode.Outline);
                        break;
                    case 2:
                        RenderUtil.drawBoxBlockPos(crystalPosition.getCrystalPosition(), 0, colorPicker.getColor(), RenderBuilder.RenderMode.Both);
                        break;
                }

                if (renderDamage.getValue())
                    RenderUtil.drawNametagFromBlockPos(crystalPosition.getCrystalPosition(), 0.5f, String.valueOf(MathUtil.roundAvoid(crystalPosition.getTargetDamage() / (78 / 36), 1)));
            }
        } catch (Exception e) {

        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketSpawnObject && ((SPacketSpawnObject) event.getPacket()).getType() == 51 && sequential.getValue() && explode.getValue()) {
            if (mc.player.getDistance(((SPacketSpawnObject) event.getPacket()).getX(), ((SPacketSpawnObject) event.getPacket()).getY(), ((SPacketSpawnObject) event.getPacket()).getZ()) > breakRange.getValue())
                return;

            if (!RaytraceUtil.raytraceBlock(new BlockPos(((SPacketSpawnObject) event.getPacket()).getX(), ((SPacketSpawnObject) event.getPacket()).getY(), ((SPacketSpawnObject) event.getPacket()).getZ())) && !walls.getValue())
                return;

            CPacketUseEntity sequentialCrystal = new CPacketUseEntity();
            sequentialCrystal.entityId = ((SPacketSpawnObject) event.getPacket()).getEntityID();
            sequentialCrystal.action = CPacketUseEntity.Action.ATTACK;
            mc.player.connection.sendPacket(sequentialCrystal);
        }

        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            NotificationManager.addNotification(new Notification("Rubberband detected! Reset Rotations!", Notification.Type.Warning));
            crystalRotation.restoreRotation();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketUseEntity && ((CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && ((CPacketUseEntity) event.getPacket()).getEntityFromWorld(mc.world) instanceof EntityEnderCrystal) {
            if (sync.getValue() == 2)
                ((CPacketUseEntity) event.getPacket()).getEntityFromWorld(mc.world).setDead();
        }
    }

    @Override
    public String getHUDData() {
        return crystalTarget != null ? " " + crystalTarget.getName() : " None";
    }
}
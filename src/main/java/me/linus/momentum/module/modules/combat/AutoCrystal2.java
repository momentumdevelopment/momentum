package me.linus.momentum.module.modules.combat;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.mode.SubMode;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.combat.EnemyUtil;
import me.linus.momentum.util.combat.crystal.CrystalPosition;
import me.linus.momentum.util.combat.crystal.CrystalUtil;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.player.PlayerUtil;
import me.linus.momentum.util.render.builder.RenderBuilder;
import me.linus.momentum.util.render.builder.RenderUtil;
import me.linus.momentum.util.world.Timer;
import me.linus.momentum.util.world.WorldUtil;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Comparator;

public class AutoCrystal2 extends Module {
    public AutoCrystal2() {
        super("AutoCrystal2", Category.COMBAT, "AutoCrystal but rewrite with new features");
    }

    public static Checkbox explode = new Checkbox("Break", true);
    public static SubMode breakHand = new SubMode(explode, "BreakHand", "OffHand", "MainHand", "Both", "MultiSwing");
    public static SubSlider breakRange = new SubSlider(explode, "Break Range", 0.0D, 5.0D, 7.0D, 1);
    public static SubSlider breakDelay = new SubSlider(explode, "Break Delay", 0.0D, 80.0D, 200.0D, 0);
    public static SubSlider breakAttempts = new SubSlider(explode, "Break Attempts", 0.0D, 1.0D, 5.0D, 0);
    public static SubCheckbox packetBreak = new SubCheckbox(explode, "Packet Break", true);
    public static SubCheckbox sequential = new SubCheckbox(explode, "Sequential", true);
    public static SubCheckbox syncBreak = new SubCheckbox(explode, "Sync", false);

    public static Checkbox place = new Checkbox("Place", true);
    public static SubSlider placeDelay = new SubSlider(place, "Place Delay", 0.0D, 0.0D, 500.0D, 0);
    public static SubSlider placeRange = new SubSlider(place, "Place Range", 0.0D, 5.0D, 7.0D, 1);
    public static SubSlider wallRange = new SubSlider(place, "Walls Range", 0.0D, 3.0D, 7.0D, 1);
    public static SubSlider enemyRange = new SubSlider(place, "Enemy Range", 0.0D, 10.0D, 15.0D, 1);
    public static SubSlider minDamage = new SubSlider(place, "Minimum Damage", 0.0D, 7.0D, 36.0D, 0);
    public static SubSlider maxLocalDamage = new SubSlider(place, "Maximum Local Damage", 0.0D, 20.0D, 36.0D, 0);
    public static SubSlider threshold = new SubSlider(place, "Threshold", 0.0D, 3.0D, 10.0D, 1);
    public static SubCheckbox packetPlace = new SubCheckbox(place, "Packet Place", true);
    public static SubCheckbox prediction = new SubCheckbox(place, "Prediction", false);
    public static SubCheckbox rayTrace = new SubCheckbox(place, "RayTrace", true);
    public static SubCheckbox multiPlace = new SubCheckbox(place, "MultiPlace", false);

    public static Checkbox pause = new Checkbox("Pause", true);
    public static SubMode pauseMode = new SubMode(pause, "Mode", "Place", "Break", "Both");
    public static SubSlider pauseHealth = new SubSlider(pause, "Pause Health", 0.0D, 7.0D, 36.0D, 0);
    public static SubCheckbox closePlacements = new SubCheckbox(pause, "Close Placements", false);
    public static SubCheckbox whenMining = new SubCheckbox(pause, "When Mining", false);
    public static SubCheckbox whenEating = new SubCheckbox(pause, "When Eating", false);

    public static Checkbox facePlace = new Checkbox("FacePlace", true);
    public static SubSlider facePlaceHealth = new SubSlider(facePlace, "FacePlace Health", 0.0D, 16.0D, 36.0D, 0);
    public static SubCheckbox armorBreaker = new SubCheckbox(facePlace, "Armor Breaker", false);
    public static SubSlider armorScale = new SubSlider(facePlace, "Armor Scale", 0.0D, 15.0D, 100.0D, 0);

    public static Checkbox calculations = new Checkbox("Calculations", true);
    public static SubMode heuristic = new SubMode(calculations, "Heuristic", "Damage", "MiniMax", "Atomic");

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
        addSetting(pause);
        addSetting(facePlace);
        addSetting(calculations);
        addSetting(logic);
        addSetting(renderCrystal);
    }

    EntityEnderCrystal crystal = null;
    CrystalPosition crystalPosition = new CrystalPosition(BlockPos.ORIGIN, 0, 0);
    Timer breakTimer = new Timer();
    Timer placeTimer = new Timer();

    @Override
    public void onUpdate() {
        autoCrystal();
    }

    public void autoCrystal() {
        breakCrystal();
        placeCrystal();
    }

    public void breakCrystal() {
        crystal = (EntityEnderCrystal) mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).filter(entityCrystal -> mc.player.getDistance(entityCrystal) <= breakRange.getValue()).min(Comparator.comparing(c -> mc.player.getDistance(c))).orElse(null);

        if (crystal != null && explode.getValue()) {
            for (int i = 0; i < breakAttempts.getValue(); i++) {
                if (breakTimer.passed((long) breakDelay.getValue(), Timer.Format.System)) {
                    CrystalUtil.swingArm(breakHand.getValue());
                    CrystalUtil.attackCrystal(crystal, packetBreak.getValue());
                    breakTimer.reset();
                }
            }
        }
    }

    public void placeCrystal() {
        CrystalPosition tempPosition = new CrystalPosition(BlockPos.ORIGIN, 0, 0);

        for (EntityPlayer calculatedPlayer : WorldUtil.getNearbyPlayers(enemyRange.getValue())) {
            for (BlockPos calculatedPosition : CrystalUtil.crystalBlocks(mc.player, placeRange.getValue(), prediction.getValue(), !multiPlace.getValue(), blockCalc.getValue())) {
                double calculatedTargetDamage = CrystalUtil.calculateDamage(calculatedPosition.getX() + 0.5, calculatedPosition.getY() + 1, calculatedPosition.getZ() + 0.5, calculatedPlayer);
                double calculatedSelfDamage = CrystalUtil.calculateDamage(calculatedPosition.getX() + 0.5, calculatedPosition.getY() + 1, calculatedPosition.getZ() + 0.5, mc.player);
                double heuristicDamage = CrystalUtil.getHeuristic(calculatedPosition, calculatedTargetDamage, calculatedSelfDamage, heuristic.getValue());

                if (calculatedTargetDamage < minDamage.getValue())
                    continue;

                if (calculatedSelfDamage > maxLocalDamage.getValue())
                    continue;

                if (heuristicDamage > tempPosition.getTargetDamage() - threshold.getValue())
                    tempPosition = new CrystalPosition(calculatedPosition, heuristicDamage, calculatedSelfDamage);
            }
        }

        if (tempPosition == new CrystalPosition(BlockPos.ORIGIN, 0, 0))
            return;

        crystalPosition = tempPosition;

        if (placeTimer.passed((long) placeDelay.getValue(), Timer.Format.System) && place.getValue() && InventoryUtil.getHeldItem(Items.END_CRYSTAL) && crystalPosition.getCrystalPosition() != null) {
            CrystalUtil.placeCrystal(crystalPosition.getCrystalPosition(), CrystalUtil.getEnumFacing(rayTrace.getValue(), crystalPosition.getCrystalPosition()), packetPlace.getValue());
            placeTimer.reset();
        }
    }

    public boolean pauseAutoCrystal() {
        boolean needPause = false;

        if (PlayerUtil.getHealth() - crystalPosition.getSelfDamage() < pauseHealth.getValue())
            needPause = true;

        if (PlayerUtil.isEating() && whenEating.getValue() || PlayerUtil.isMining() && whenMining.getValue())
            needPause = true;

        if (mc.player.getDistance(crystal) < 1.5 && closePlacements.getValue())
            needPause = true;

        return needPause;
    }

    public boolean facePlace(EntityPlayer currentTarget) {
        boolean needFacePlace = false;

        if (EnemyUtil.getHealth(currentTarget) < facePlaceHealth.getValue())
            needFacePlace = true;

        if (EnemyUtil.getArmor(currentTarget, armorBreaker.getValue(), armorScale.getValue()))
            needFacePlace = true;

        return needFacePlace;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (renderCrystal.getValue() && crystalPosition != null && crystalPosition.getCrystalPosition() != null) {
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
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketUseEntity && ((CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && ((CPacketUseEntity) event.getPacket()).getEntityFromWorld(mc.world) instanceof EntityEnderCrystal && syncBreak.getValue())
            ((CPacketUseEntity) event.getPacket()).getEntityFromWorld(mc.world).setDead();
    }

    @SubscribeEvent
    public void onPacketRecieve(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketSpawnObject && ((SPacketSpawnObject) event.getPacket()).getType() == 51 && sequential.getValue()) {
            CPacketUseEntity sequentialCrystal = new CPacketUseEntity();
            sequentialCrystal.entityId = ((SPacketSpawnObject) event.getPacket()).getEntityID();
            sequentialCrystal.action = CPacketUseEntity.Action.ATTACK;
            mc.player.connection.sendPacket(sequentialCrystal);
        }
    }
}
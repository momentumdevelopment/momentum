package me.linus.momentum.module.modules.combat.autocrystal.algorithms;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.module.modules.combat.AutoCrystal;
import me.linus.momentum.module.modules.combat.autocrystal.AutoCrystalAlgorithm;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.combat.CrystalUtil;
import me.linus.momentum.util.combat.EnemyUtil;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.player.PlayerUtil;
import me.linus.momentum.util.player.rotation.RotationUtil;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.HoleUtil;
import me.linus.momentum.util.world.Timer;
import me.linus.momentum.util.world.WorldUtil;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author linustouchtips
 * @since 01/08/2021
 */

public class DepthFirst extends AutoCrystalAlgorithm {

    Timer breakTimer = new Timer();
    Timer placeTimer = new Timer();
    List<BlockPos> placedCrystals = new ArrayList<>();

    @Override
    public void breakCrystal() {
        this.crystal = (EntityEnderCrystal) mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).filter(entity -> CrystalUtil.attackCheck(entity, AutoCrystal.breakMode.getValue(), AutoCrystal.breakRange.getValue(), placedCrystals)).min(Comparator.comparing(c -> mc.player.getDistance(c))).orElse(null);

        if (this.crystal != null && breakTimer.passed((long) AutoCrystal.breakDelay.getValue(), Timer.Format.System) && mc.player.getDistance(crystal) <= AutoCrystal.breakRange.getValue()) {
           if (AutoCrystal.pause.getValue() && PlayerUtil.getHealth() <= AutoCrystal.pauseHealth.getValue() && (AutoCrystal.pauseMode.getValue() == 1 || AutoCrystal.pauseMode.getValue() == 2))
                return;

            if (AutoCrystal.closePlacements.getValue() && mc.player.getDistance(this.crystal) < 1.5)
                return;

            if (!RotationUtil.isInViewFrustrum(this.crystal) && AutoCrystal.onlyInViewFrustrum.getValue())
                return;

            if (AutoCrystal.antiWeakness.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS))
                InventoryUtil.switchToSlot(Items.DIAMOND_SWORD);

            if (AutoCrystal.explode.getValue())
                for (int i = 0; i < AutoCrystal.breakAttempts.getValue(); i++)
                    CrystalUtil.attackCrystal(this.crystal, AutoCrystal.packetBreak.getValue());

            CrystalUtil.swingArm(AutoCrystal.breakHand.getValue());
            if (AutoCrystal.removeCrystal.getValue())
                mc.world.removeEntityFromWorld(this.crystal.entityId);

            if (AutoCrystal.syncBreak.getValue())
                this.crystal.setDead();

            if (AutoCrystal.unload.getValue()) {
                mc.world.removeAllEntities();
                mc.world.getLoadedEntityList();
            }

            mc.world.loadedEntityList.stream().filter(entity -> entity.getDistance(crystal) <= AutoCrystal.breakMode.getValue()).forEach(entity -> {
                if (AutoCrystal.damageSync.getValue())
                    entity.attackEntityFrom(DamageSource.causeExplosionDamage(new Explosion(mc.world, crystal, crystal.posX, crystal.posY, crystal.posZ, 6.0f, false, true)), 8);
            });
        }

        breakTimer.reset();

        if (!AutoCrystal.multiPlace.getValue() || (AutoCrystal.multiPlaceInHole.getValue() && HoleUtil.isInHole(mc.player)))
            return;
    }

    @Override
    public void placeCrystal() {
        double tempDamage = 0;
        BlockPos tempPos = null;

        for (EntityPlayer tempPlayer : WorldUtil.getNearbyPlayers(AutoCrystal.enemyRange.getValue())) {
            for (BlockPos calculatedPos : CrystalUtil.getCrystalBlocks(mc.player, AutoCrystal.placeRange.getValue(), AutoCrystal.prediction.getValue(), AutoCrystal.blockCalc.getValue())) {
                if (!RotationUtil.canBlockBeSeen(calculatedPos) && mc.player.getDistanceSq(calculatedPos) > MathUtil.square(AutoCrystal.wallRange.getValue()))
                    continue;

                if (AutoCrystal.verifyCalc.getValue() && mc.player.getDistanceSq(calculatedPos) > MathUtil.square(AutoCrystal.breakRange.getValue()) || mc.player.getDistanceSq(calculatedPos) > 52.6 && AutoCrystal.pastDistance.getValue())
                    continue;

                double calculatedDamage = AutoCrystal.placeCalc.getValue() == 0 ? CrystalUtil.getDamage(new Vec3d(calculatedPos.add(0.5, 1, 0.5)), tempPlayer) : CrystalUtil.getDamage(new Vec3d(calculatedPos.getX(), calculatedPos.getY() + 1, calculatedPos.getZ()), tempPlayer);

                double minCalculatedDamage = AutoCrystal.minDamage.getValue();
                if (EnemyUtil.getHealth(tempPlayer) <= AutoCrystal.facePlaceHealth.getValue() || HoleUtil.isInHole(tempPlayer) && AutoCrystal.facePlaceHole.getValue() || EnemyUtil.getArmor(tempPlayer, AutoCrystal.armorMelt.getValue(), AutoCrystal.armorDurability.getValue()))
                    minCalculatedDamage = 2;

                if (calculatedDamage < minCalculatedDamage || calculatedDamage < tempDamage)
                    continue;

                if (calculatedDamage <= tempDamage + AutoCrystal.resetThreshold.getValue() && calculatedDamage > tempDamage) {
                    tempDamage = placeDamage;
                    tempPos = placePos;
                    continue;
                }

                double selfDamage = mc.player.isCreative() ? 0 : CrystalUtil.getDamage(new Vec3d(calculatedPos.getX() + 0.5, calculatedPos.getY() + 1, calculatedPos.getZ() + 0.5), mc.player);
                if (PlayerUtil.getHealth() - selfDamage <= AutoCrystal.pauseHealth.getValue() && AutoCrystal.pause.getValue() && (AutoCrystal.pauseMode.getValue() == 0 || AutoCrystal.pauseMode.getValue() == 2))
                    continue;

                if (selfDamage > calculatedDamage)
                    continue;

                if (tempPlayer != null && calculatedDamage != 0) {
                    tempDamage = calculatedDamage;
                    tempPos = calculatedPos;
                    currentTarget = tempPlayer;
                }
            }
        }

        if (tempPos != null && tempDamage != 0) {
            placeDamage = tempDamage;
            placePos = tempPos;
        }

        if (AutoCrystal.autoSwitch.getValue())
            InventoryUtil.switchToSlot(Items.END_CRYSTAL);

        if (placeTimer.passed((long) AutoCrystal.placeDelay.getValue(), Timer.Format.System) && AutoCrystal.place.getValue() && InventoryUtil.getHeldItem(Items.END_CRYSTAL) && placePos != null) {
            CrystalUtil.placeCrystal(placePos, CrystalUtil.getEnumFacing(AutoCrystal.rayTrace.getValue(), placePos), AutoCrystal.packetPlace.getValue());
            placedCrystals.add(placePos);
        }

        placeTimer.reset();
    }

    @Override
    public void renderPlacement() {
        if (AutoCrystal.renderCrystal.getValue() && this.placePos != null) {
            RenderUtil.drawBoxBlockPos(this.placePos, 0, new Color((int) AutoCrystal.r.getValue(), (int) AutoCrystal.g.getValue(), (int) AutoCrystal.b.getValue(), (int) AutoCrystal.a.getValue()));

            if (AutoCrystal.outline.getValue())
                RenderUtil.drawBoundingBoxBlockPos(this.placePos, 0, new Color((int) AutoCrystal.r.getValue(), (int) AutoCrystal.g.getValue(), (int) AutoCrystal.b.getValue(), 144));

            if (AutoCrystal.renderDamage.getValue())
                RenderUtil.drawNametagFromBlockPos(placePos, String.valueOf(MathUtil.roundAvoid(placeDamage, 1)));
        }
    }

    @Override
    public void onToggle() {
        placedCrystals.clear();
    }

    @Override
    public void onSync(PacketReceiveEvent event) {
        placedCrystals.removeIf(calculatedPos -> calculatedPos.getDistance((int) ((SPacketSoundEffect) event.getPacket()).getX(), (int)  ((SPacketSoundEffect) event.getPacket()).getY(), (int) ((SPacketSoundEffect) event.getPacket()).getZ()) <= AutoCrystal.breakRange.getValue());
    }
}

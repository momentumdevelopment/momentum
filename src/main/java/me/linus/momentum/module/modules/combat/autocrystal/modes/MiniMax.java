package me.linus.momentum.module.modules.combat.autocrystal.modes;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.module.modules.combat.AutoCrystal;
import me.linus.momentum.module.modules.combat.autocrystal.AutoCrystalAlgorithm;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.client.Pair;
import me.linus.momentum.util.combat.CrystalUtil;
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

public class MiniMax extends AutoCrystalAlgorithm {

    Timer breakTimer = new Timer();
    Timer placeTimer = new Timer();
    List<BlockPos> placedCrystals = new ArrayList<>();

    @Override
    public void breakCrystal() {
        this.crystal = (EntityEnderCrystal) mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).filter(entity -> CrystalUtil.attackCheck(entity, AutoCrystal.breakMode.getValue(), AutoCrystal.breakRange.getValue(), placedCrystals)).min(Comparator.comparing(c -> mc.player.getDistance(c))).orElse(null);

        if (this.crystal != null && breakTimer.passed((long) AutoCrystal.breakDelay.getValue(), Timer.Format.System)) {
            if (this.crystal.getDistance(mc.player) > (!mc.player.canEntityBeSeen(this.crystal) ? AutoCrystal.wallRange.getValue() : AutoCrystal.breakRange.getValue()))
                return;

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
        List<Pair<Float, BlockPos>> selfDamage = new ArrayList<>();
        List<Pair<Float, BlockPos>> targetDamage = new ArrayList<>();
        BlockPos tempPos = null;
        double tempDamage = 0;

        for (EntityPlayer tempPlayer : WorldUtil.getNearbyPlayers(AutoCrystal.enemyRange.getValue())) {
            for (BlockPos calculatedPos : CrystalUtil.getCrystalBlocks(mc.player, AutoCrystal.placeRange.getValue(), AutoCrystal.prediction.getValue(), AutoCrystal.blockCalc.getValue())) {
                float calculatedTargetDamage = AutoCrystal.placeCalc.getValue() == 0 ? CrystalUtil.getDamage(new Vec3d(calculatedPos.add(0.5, 1, 0.5)), tempPlayer) : CrystalUtil.getDamage(new Vec3d(calculatedPos.getX(), calculatedPos.getY() + 1, calculatedPos.getZ()), tempPlayer);
                float calculatedSelfDamage = mc.player.isCreative() ? 0 : CrystalUtil.getDamage(new Vec3d(calculatedPos.getX() + 0.5, calculatedPos.getY() + 1, calculatedPos.getZ() + 0.5), mc.player);

                if (calculatedTargetDamage < AutoCrystal.minDamage.getValue())
                    continue;

                if (PlayerUtil.getHealth() - calculatedSelfDamage <= AutoCrystal.pauseHealth.getValue())
                    continue;

                targetDamage.add(new Pair<>(calculatedTargetDamage, calculatedPos));
                selfDamage.add(new Pair<>(calculatedSelfDamage, calculatedPos));

                tempPos = targetDamage.stream().max(Comparator.comparing(damage -> damage.getKey())).get().getValue();
                tempDamage = calculatedTargetDamage;
            }
            
            this.currentTarget = tempPlayer;
        }

        if (tempPos != null && tempDamage != 0) {
            this.placePos = tempPos;
            this.placeDamage = tempDamage;
        }

        if (AutoCrystal.autoSwitch.getValue())
            InventoryUtil.switchToSlot(Items.END_CRYSTAL);

        if (placeTimer.passed((long) AutoCrystal.placeDelay.getValue(), Timer.Format.System) && AutoCrystal.place.getValue() && InventoryUtil.getHeldItem(Items.END_CRYSTAL) && this.placePos != null) {
            CrystalUtil.placeCrystal(this.placePos, CrystalUtil.getEnumFacing(AutoCrystal.rayTrace.getValue(), this.placePos), AutoCrystal.packetPlace.getValue());
            placedCrystals.add(this.placePos);
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

package me.linus.momentum.module.modules.combat.autocrystal.modes;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.module.modules.combat.AutoCrystalTest;
import me.linus.momentum.module.modules.combat.autocrystal.AutoCrystalAlgorithm;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.client.Pair;
import me.linus.momentum.util.combat.CrystalUtil;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.player.PlayerUtil;
import me.linus.momentum.util.player.rotation.RotationUtil;
import me.linus.momentum.util.render.RenderUtil;
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
        this.crystal = (EntityEnderCrystal) mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).filter(entity -> CrystalUtil.attackCheck(entity, AutoCrystalTest.breakMode.getValue(), AutoCrystalTest.breakRange.getValue(), placedCrystals)).min(Comparator.comparing(c -> mc.player.getDistance(c))).orElse(null);

        if (this.crystal != null && breakTimer.passed((long) AutoCrystalTest.breakDelay.getValue(), Timer.Format.System)) {
            if (this.crystal.getDistance(mc.player) > (!mc.player.canEntityBeSeen(this.crystal) ? AutoCrystalTest.wallRange.getValue() : AutoCrystalTest.breakRange.getValue()))
                return;

            if (AutoCrystalTest.pause.getValue() && PlayerUtil.getHealth() <= AutoCrystalTest.pauseHealth.getValue() && (AutoCrystalTest.pauseMode.getValue() == 1 || AutoCrystalTest.pauseMode.getValue() == 2))
                return;

            if (AutoCrystalTest.closePlacements.getValue() && mc.player.getDistance(this.crystal) < 1.5)
                return;

            if (!RotationUtil.isInViewFrustrum(this.crystal) && AutoCrystalTest.onlyInViewFrustrum.getValue())
                return;

            if (AutoCrystalTest.antiWeakness.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS))
                InventoryUtil.switchToSlot(Items.DIAMOND_SWORD);

            if (AutoCrystalTest.explode.getValue())
                for (int i = 0; i < AutoCrystalTest.breakAttempts.getValue(); i++)
                    CrystalUtil.attackCrystal(this.crystal, AutoCrystalTest.packetBreak.getValue());

            CrystalUtil.swingArm(AutoCrystalTest.breakHand.getValue());
            if (AutoCrystalTest.removeCrystal.getValue())
                mc.world.removeEntityFromWorld(this.crystal.entityId);

            if (AutoCrystalTest.syncBreak.getValue())
                this.crystal.setDead();

            if (AutoCrystalTest.unload.getValue()) {
                mc.world.removeAllEntities();
                mc.world.getLoadedEntityList();
            }

            mc.world.loadedEntityList.stream().filter(entity -> entity.getDistance(crystal) <= AutoCrystalTest.breakMode.getValue()).forEach(entity -> {
                if (AutoCrystalTest.damageSync.getValue())
                    entity.attackEntityFrom(DamageSource.causeExplosionDamage(new Explosion(mc.world, crystal, crystal.posX, crystal.posY, crystal.posZ, 6.0f, false, true)), 8);
            });
        }
    }

    @Override
    public void placeCrystal() {
        List<Pair<Float, BlockPos>> selfDamage = new ArrayList<>();
        List<Pair<Float, BlockPos>> targetDamage = new ArrayList<>();
        BlockPos tempPos = null;
        double tempDamage = 0;

        for (EntityPlayer tempPlayer : WorldUtil.getNearbyPlayers(AutoCrystalTest.enemyRange.getValue())) {
            for (BlockPos calculatedPos : CrystalUtil.getCrystalBlocks(mc.player, AutoCrystalTest.placeRange.getValue(), AutoCrystalTest.prediction.getValue(), AutoCrystalTest.blockCalc.getValue())) {
                float calculatedTargetDamage = AutoCrystalTest.placeCalc.getValue() == 0 ? CrystalUtil.getDamage(new Vec3d(calculatedPos.add(0.5, 1, 0.5)), tempPlayer) : CrystalUtil.getDamage(new Vec3d(calculatedPos.getX(), calculatedPos.getY() + 1, calculatedPos.getZ()), tempPlayer);
                float calculatedSelfDamage = mc.player.isCreative() ? 0 : CrystalUtil.getDamage(new Vec3d(calculatedPos.getX() + 0.5, calculatedPos.getY() + 1, calculatedPos.getZ() + 0.5), mc.player);

                if (calculatedTargetDamage < AutoCrystalTest.minDamage.getValue())
                    continue;

                if (PlayerUtil.getHealth() - calculatedSelfDamage <= AutoCrystalTest.pauseHealth.getValue())
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

        if (AutoCrystalTest.autoSwitch.getValue())
            InventoryUtil.switchToSlot(Items.END_CRYSTAL);

        if (placeTimer.passed((long) AutoCrystalTest.placeDelay.getValue(), Timer.Format.System) && AutoCrystalTest.place.getValue() && InventoryUtil.getHeldItem(Items.END_CRYSTAL) && this.placePos != null) {
            CrystalUtil.placeCrystal(this.placePos, CrystalUtil.getEnumFacing(AutoCrystalTest.rayTrace.getValue(), this.placePos), AutoCrystalTest.packetPlace.getValue());
            placedCrystals.add(this.placePos);
        }

        placeTimer.reset();
    }
    
    @Override
    public void renderPlacement() {
        if (AutoCrystalTest.renderCrystal.getValue() && this.placePos != null) {
            RenderUtil.drawBoxBlockPos(this.placePos, 0, new Color((int) AutoCrystalTest.r.getValue(), (int) AutoCrystalTest.g.getValue(), (int) AutoCrystalTest.b.getValue(), (int) AutoCrystalTest.a.getValue()));

            if (AutoCrystalTest.outline.getValue())
                RenderUtil.drawBoundingBoxBlockPos(this.placePos, 0, new Color((int) AutoCrystalTest.r.getValue(), (int) AutoCrystalTest.g.getValue(), (int) AutoCrystalTest.b.getValue(), 144));

            if (AutoCrystalTest.renderDamage.getValue())
                RenderUtil.drawNametagFromBlockPos(placePos, String.valueOf(MathUtil.roundAvoid(placeDamage, 1)));
        }
    }

    @Override
    public void onToggle() {
        placedCrystals.clear();
    }

    @Override
    public void onSync(PacketReceiveEvent event) {
        placedCrystals.removeIf(calculatedPos -> calculatedPos.getDistance((int) ((SPacketSoundEffect) event.getPacket()).getX(), (int)  ((SPacketSoundEffect) event.getPacket()).getY(), (int) ((SPacketSoundEffect) event.getPacket()).getZ()) <= AutoCrystalTest.breakRange.getValue());
    }
}

package me.linus.momentum.module.modules.combat;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.SubMode;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.client.Timer;
import me.linus.momentum.util.client.friend.FriendManager;
import me.linus.momentum.util.combat.CrystalUtil;
import me.linus.momentum.util.combat.EnemyUtil;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.BlockUtils;
import me.linus.momentum.util.world.EntityUtil;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
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
import java.util.stream.Collectors;

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
    public static SubSlider wallRange = new SubSlider(explode, "Walls Range", 0.0D, 3.0D, 7.0D, 1);
    public static SubCheckbox antiDeSync = new SubCheckbox(explode, "Anti-DeSync", true);
    public static SubCheckbox syncBreak = new SubCheckbox(explode, "Sync Break", true);
    public static SubCheckbox unload = new SubCheckbox(explode, "Unload Crystal", true);
    public static SubCheckbox packetBreak = new SubCheckbox(explode, "Packet Break", true);
    public static SubCheckbox prediction = new SubCheckbox(explode, "Prediction", true);
    public static SubCheckbox damageSync = new SubCheckbox(explode, "Damage Sync", false);
    public static SubCheckbox antiWeakness = new SubCheckbox(explode, "Anti-Weakness", false);
    public static SubMode rotate = new SubMode(explode, "Rotate", "Spoof", "Legit");
    public static SubMode breakHand = new SubMode(explode, "BreakHand", "MainHand", "OffHand", "Both", "GhostHand");

    public static Checkbox place = new Checkbox("Place", true);
    public static SubSlider placeRange = new SubSlider(place, "Place Range", 0.0D, 5.0D, 7.0D, 1);
    public static SubSlider enemyRange = new SubSlider(place, "Enemy Range", 0.0D, 5.0D, 7.0D, 1);
    public static SubSlider placeDelay = new SubSlider(place, "Place Delay", 0.0D, 40.0D, 600.0D, 0);
    public static SubSlider minDamage = new SubSlider(place, "Minimum Damage", 0.0D, 7.0D, 36.0D, 0);
    public static SubCheckbox autoSwitch = new SubCheckbox(place, "Auto-Switch", false);
    public static SubCheckbox rayTrace = new SubCheckbox(place, "Ray-Trace", true);
    public static SubCheckbox multiPlace = new SubCheckbox(place, "MultiPlace", false);

    public static Checkbox pause = new Checkbox("Pause", true);
    public static SubSlider pauseHealth = new SubSlider(pause, "Pause Health", 0.0D, 7.0D, 36.0D, 0);
    public static SubMode pauseMode = new SubMode(pause, "Mode", "Place", "Break", "Both");
    public static SubCheckbox whenMining = new SubCheckbox(pause, "When Mining", false);
    public static SubCheckbox whenEating = new SubCheckbox(pause, "When Eating", false);

    public static Checkbox prevent = new Checkbox("Prevent", true);
    public static SubCheckbox waste = new SubCheckbox(prevent, "Waste", false);
    public static SubSlider wasteAmount = new SubSlider(prevent, "Waste Amount", 0.0D, 20.0D, 40.0D, 0);
    public static SubCheckbox closePlacements = new SubCheckbox(prevent, "Close Placements", false);

    public static Checkbox facePlace = new Checkbox("Face-Place", true);
    public static SubSlider facePlaceHealth = new SubSlider(facePlace, "Face-Place Health", 0.0D, 16.0D, 36.0D, 0);
    public static SubCheckbox armorMelt = new SubCheckbox(facePlace, "Armor Melt", false);
    public static SubSlider armorDurability = new SubSlider(facePlace, "Armor Durability", 0.0D, 15.0D, 100.0D, 0);

    public static Checkbox calculations = new Checkbox("Calculations", true);
    public static SubMode placeCalc = new SubMode(calculations, "Place Calculation", "Ideal", "Actual");
    public static SubMode enemyCalc = new SubMode(calculations, "Enemy Calculation", "None", "Dynamic", "Priority");
    public static SubMode verifyCalc = new SubMode(calculations, "Verify Calculation", "None", "Check");
    public static SubMode damageCalc = new SubMode(calculations, "Damage Calculation", "Full", "Semi");

    public static Checkbox logic = new Checkbox("Logic", true);
    public static SubMode logicMode = new SubMode(logic, "Crystal Logic", "Break -> Place", "Place -> Break");
    public static SubMode blockCalc = new SubMode(logic, "Block Logic", "Normal", "1.13+");

    public static Checkbox hole = new Checkbox("Hole", false);
    public static SubCheckbox multiPlaceInHole = new SubCheckbox(hole, "MultiPlace in Hole", false);

    public static Checkbox renderCrystal = new Checkbox("Render", true);
    public static SubSlider r = new SubSlider(renderCrystal, "Red", 0.0D, 250.0D, 255.0D, 0);
    public static SubSlider g = new SubSlider(renderCrystal, "Green", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider b = new SubSlider(renderCrystal, "Blue", 0.0D, 250.0D, 255.0D, 0);
    public static SubSlider a = new SubSlider(renderCrystal, "Alpha", 0.0D, 50.0D, 255.0D, 0);
    public static SubCheckbox renderDamage = new SubCheckbox(renderCrystal, "Render Damage", true);
    public static SubCheckbox outline = new SubCheckbox(renderCrystal, "Outline", false);

    @Override
    public void setup() {
        addSetting(explode);
        addSetting(place);
        addSetting(facePlace);
        addSetting(pause);
        addSetting(prevent);
        addSetting(calculations);
        addSetting(logic);
        addSetting(hole);
        addSetting(renderCrystal);
    }

    Timer breakTimer = new Timer();
    Timer placeTimer = new Timer();
    public static EntityPlayer currentTarget;
    private boolean switchCooldown = false;
    private BlockPos render;
    private Entity renderEnt;
    private final ArrayList<BlockPos> placedCrystals = new ArrayList<>();

    @Override
    public void onToggle() {
        if (nullCheck())
            return;

        placedCrystals.clear();
    }

    @Override
    public void onUpdate() {
        currentTarget = EntityUtil.getClosestPlayer(enemyRange.getValue());

        if (logicMode.getValue() == 1) {
            placeCrystal();
            breakCrystal();
        } else {
            breakCrystal();
            placeCrystal();
        }
    }

    public void breakCrystal() {
        EntityEnderCrystal crystal = (EntityEnderCrystal) mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).filter(entity -> CrystalUtil.attackCheck(entity, breakMode.getValue(), breakRange.getValue(), placedCrystals)).min(Comparator.comparing(c -> mc.player.getDistance(c))).orElse(null);
        float[] angles = MathUtil.calcAngle(EntityUtil.interpolateEntityTime(this.mc.player, this.mc.getRenderPartialTicks()), EntityUtil.interpolateEntityTime(crystal, this.mc.getRenderPartialTicks()));
        if (crystal != null && mc.player.getDistance(crystal) <= breakRange.getValue() && breakTimer.passed((long) breakDelay.getValue())) {
            if (pause.getValue() && PlayerUtil.getHealth() <= pauseHealth.getValue() && pauseMode.getValue() == 0 && (pauseMode.getValue() == 1 || pauseMode.getValue() == 2))
                return;

            if (closePlacements.getValue() && mc.player.getDistance(crystal) < 2)
                return;

            if (!mc.player.canEntityBeSeen(crystal) && mc.player.getDistance(crystal) > wallRange.getValue())
                return;

            if (antiWeakness.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS))
                CrystalUtil.doAntiWeakness(switchCooldown);

            if (rotate.getValue() == 0)
                CrystalUtil.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, mc.player);
            else {
                mc.player.rotationYaw = angles[0];
                mc.player.rotationPitch = angles[1];
            }

            if (explode.getValue()) {
                for (int i = 0; i < breakAttempts.getValue(); i++)
                    CrystalUtil.attackCrystal(crystal, packetBreak.getValue());
            }

            CrystalUtil.getSwingArm(breakHand.getValue());
            if (syncBreak.getValue())
                crystal.setDead();

            if (unload.getValue()) {
                mc.world.removeAllEntities();
                mc.world.getLoadedEntityList();
            }

            if (damageSync.getValue())
                currentTarget.attackEntityFrom(DamageSource.causeExplosionDamage(new Explosion(mc.world, crystal, crystal.posX, crystal.posY, crystal.posZ, 6.0f, false, true)), 8);
        }

        breakTimer.reset();

        if (!multiPlace.getValue() || (multiPlaceInHole.getValue() && PlayerUtil.isInHole()))
            return;
    }

    public void placeCrystal() {
        int crystalSlot = (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }

        boolean offhand = false;
        if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL)
            offhand = true;
        else if (crystalSlot == -1)
            return;

        Entity entity = null;
        BlockPos finalPos = null;
        List<BlockPos> blocks = findCrystalBlocks();
        List<Entity> entities = new ArrayList<>();
        entities.addAll(mc.world.playerEntities.stream().collect(Collectors.toList()));
        double damage = 0.5;
        for (final Entity entityTarget : entities) {
            if (entityTarget != mc.player) {
                if (((EntityLivingBase) entityTarget).getHealth() <= 0.0f && !(damageCalc.getValue() == 1))
                    continue;

                if (mc.player.getDistanceSq(entityTarget) > enemyRange.getValue() * enemyRange.getValue())
                    continue;

                if (FriendManager.isFriend(entityTarget.getName()) && FriendManager.isFriendModuleEnabled())
                    continue;

                for (final BlockPos blockPos : blocks) {
                    if (!CrystalUtil.canBlockBeSeen(blockPos) && mc.player.getDistanceSq(blockPos) > wallRange.getValue() * wallRange.getValue())
                        continue;

                    final double targetDistanceSq = entityTarget.getDistanceSq(blockPos);
                    if (targetDistanceSq > 56.2)
                        continue;

                    if (verifyCalc.getValue() == 1 && mc.player.getDistanceSq(blockPos) > breakRange.getValue() * breakRange.getValue())
                        continue;

                    final double calcDamage;
                    if (placeCalc.getValue() == 1) {
                        calcDamage = CrystalUtil.calculateDamage(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ(), entityTarget);
                    } else {
                        calcDamage = CrystalUtil.calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, entityTarget);
                    }

                    int minDamagePlace;
                    if (EnemyUtil.getArmor((EntityPlayer) entityTarget, armorMelt.getValue(), armorDurability.getValue())) {
                        minDamagePlace = 2;
                    } else {
                        minDamagePlace = (int) minDamage.getValue();
                    } if (calcDamage < minDamagePlace && ((EntityLivingBase) entityTarget).getHealth() + ((EntityLivingBase) entityTarget).getAbsorptionAmount() > facePlaceHealth.getValue())
                        continue;
                      if (calcDamage <= damage && !(damageCalc.getValue() == 1))
                        continue;

                    final double selfDamage = CrystalUtil.calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, mc.player);
                    if (PlayerUtil.getHealth() - selfDamage <= pauseHealth.getValue() && pause.getValue() && (pauseMode.getValue() == 0 || pauseMode.getValue() == 2))
                        continue;
                    if (selfDamage  > calcDamage)
                        continue;

                    damage = calcDamage;
                    finalPos = blockPos;
                    entity = entityTarget;
                }
            }
        }

        if (damage == 0.5) {
            this.render = null;
            this.renderEnt = null;
            CrystalUtil.resetRotation();
            return;
        }

        this.render = finalPos;
        this.renderEnt = entity;

        if (true) {
            if (!offhand && mc.player.inventory.currentItem != crystalSlot) {
                if (autoSwitch.getValue())
                    mc.player.inventory.currentItem = crystalSlot;

                CrystalUtil.resetRotation();
                this.switchCooldown = true;
                return;
            }
        }

        if (place.getValue()) {
            CrystalUtil.lookAtPacket(finalPos.getX() + 0.5, finalPos.getY() - 0.5, finalPos.getZ() + 0.5, mc.player);
            EnumFacing enumFacing = CrystalUtil.getEnumFacing(rayTrace.getValue(), render, finalPos);

            if (placeTimer.passed((long) placeDelay.getValue())) {
                if (rayTrace.getValue() && enumFacing != null)
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(finalPos, enumFacing, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                else
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(finalPos, EnumFacing.UP, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));

                placedCrystals.add(finalPos);
            }

            placeTimer.reset();
        }
    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent eventRender) {
        if (renderCrystal.getValue() && render != null) {
            RenderUtil.drawVanillaBoxFromBlockPos(render, (float) r.getValue() / 255f, (float) g.getValue() / 255f, (float) b.getValue() / 255f, (float) a.getValue() / 255f);

            double damage = CrystalUtil.calculateDamage(render.getX() + .5, render.getY() + 1, render.getZ() + .5, renderEnt);
            double damageRounded = MathUtil.roundAvoid(damage, 1);

            if (outline.getValue())
                RenderUtil.drawBoundingBoxBlockPos(render, 1.0f, new Color((int) r.getValue(), (int) g.getValue(),  (int)b.getValue(), 144));

            if (renderDamage.getValue())
                RenderUtil.drawNametagFromBlockPos(render, String.valueOf(damageRounded));
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketSoundEffect && antiDeSync.getValue() && explode.getValue()) {
            final SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (Entity e : mc.world.loadedEntityList) {
                    if (e instanceof EntityEnderCrystal) {
                        if (e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0f) {
                            e.setDead();
                        }
                    }
                }
            }
        }
    }

    private List<BlockPos> findCrystalBlocks() {
        NonNullList positions = NonNullList.create();
        if (blockCalc.getValue() == 0)
            positions.addAll(BlockUtils.getSphere(CrystalUtil.getPlayerPos(), (float) placeRange.getValue(), (int) placeRange.getValue(), false, true, 0).stream().filter(CrystalUtil::canPlaceCrystal).collect(Collectors.toList()));
        else
            positions.addAll(BlockUtils.getSphere(CrystalUtil.getPlayerPos(), (float) placeRange.getValue(), (int) placeRange.getValue(), false, true, 0).stream().filter(CrystalUtil::canPlaceThirteenCrystal).collect(Collectors.toList()));
        return (List<BlockPos>) positions;
    }

    @Override
    public String getHUDData() {
        if (currentTarget != null)
            return " " + currentTarget.getName();
        else
            return " None";
    }
}
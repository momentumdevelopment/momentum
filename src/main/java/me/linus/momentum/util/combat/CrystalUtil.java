package me.linus.momentum.util.combat;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.MathUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linustouchtips
 * @since 11/24/2020
 */

public class CrystalUtil implements MixinInterface {

    public static List<BlockPos> getCrystalBlocks(EntityPlayer player, double placeRange, boolean prediction, int blockCalc) {
        List<BlockPos> nearbyCrystalBlocks = new ArrayList<>();
        int range = (int) MathUtil.roundDouble(placeRange, 0);

        if (prediction)
            player.getPosition().add(new Vec3i(player.motionX, player.motionY, player.motionZ));

        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range; y++)
                for (int z = -range; z <= range; z++)
                    nearbyCrystalBlocks.add(player.getPosition().add(x, y, z));

        return nearbyCrystalBlocks.stream().filter(blockPos -> (blockCalc == 0 ? CrystalUtil.canPlaceCrystal(blockPos) : CrystalUtil.canPlaceThirteenCrystal(blockPos)) && mc.player.getDistanceSq(blockPos) <= (MathUtil.square(placeRange))).collect(Collectors.toList());
    }

    public static float getDamage(Vec3d pos, EntityPlayer entity) {
        double power = (1.0D - (entity.getDistance(pos.x, pos.y, pos.z) / 12.0D)) * entity.world.getBlockDensity(pos, entity.getEntityBoundingBox());
        float damage = (float) ((int) ((power * power + power) / 2.0D * 7.0D * 12.0D + 1.0D));

        int difficulty = mc.world.getDifficulty().getDifficultyId();
        damage *= (difficulty == 0 ? 0 : (difficulty == 2 ? 1 : (difficulty == 1 ? 0.5f : 1.5f)));

        return getReduction(entity, damage, new Explosion(mc.world, null, pos.x, pos.y, pos.z, 6F, false, true));
    }

    public static float getReduction(EntityPlayer player, float damage, Explosion explosion) {
        damage = CombatRules.getDamageAfterAbsorb(damage, (float) player.getTotalArmorValue(), (float) player.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        damage *= (1.0F - (float) EnchantmentHelper.getEnchantmentModifierDamage(player.getArmorInventoryList(), DamageSource.causeExplosionDamage(explosion)) / 25.0F);

        if (player.isPotionActive(Potion.getPotionById(11)))
            damage -= damage / 4;

        return damage;
    }

    public static boolean canBlockBeSeen(BlockPos blockPos) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), false, true, false) == null;
    }

    public static void attackCrystal(EntityEnderCrystal crystal, boolean packet) {
        if (packet)
            mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
        else
            mc.playerController.attackEntity(mc.player, crystal);
    }

    public static boolean attackCheck(Entity crystal, int mode, double breakRange, List<BlockPos> placedCrystals) {
        if (!(crystal instanceof EntityEnderCrystal))
            return false;

        switch (mode) {
            case 0:
                return true;
            case 1:
                for (BlockPos placePos : new ArrayList<>(placedCrystals)) {
                    if (placePos != null && placePos.getDistance((int) crystal.posX, (int) crystal.posY, (int) crystal.posZ) <= breakRange)
                        return true;
                }
        }

        return false;
    }

    public static void swingArm(int mode) {
        switch (mode) {
            case 0:
                mc.player.swingArm(EnumHand.MAIN_HAND);
            case 1:
                if (!mc.player.getHeldItemOffhand().isEmpty())
                    mc.player.swingArm(EnumHand.OFF_HAND);
            case 2:
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.OFF_HAND);
            case 3:
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    public static void placeCrystal(BlockPos placePos, EnumFacing enumFacing, boolean packet) {
        if (packet)
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(placePos, enumFacing, mc.player.getHeldItemOffhand().equals(Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        else
            mc.playerController.processRightClickBlock(mc.player, mc.world, placePos, enumFacing, new Vec3d(0, 0, 0), mc.player.getHeldItemOffhand().equals(Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
    }

    public static EnumFacing getEnumFacing(boolean rayTrace, BlockPos finalPos) {
        RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(finalPos.getX() + 0.5, finalPos.getY() - 0.5, finalPos.getZ() + 0.5));

        if (rayTrace)
          return (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;

        return EnumFacing.UP;
    }

    public static boolean canPlaceCrystal(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);
        return (mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && mc.world.getBlockState(boost).getBlock() == Blocks.AIR && mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }

    public static boolean canPlaceThirteenCrystal(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);
        return (mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
}

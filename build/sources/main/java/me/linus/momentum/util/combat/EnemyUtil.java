package me.linus.momentum.util.combat;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * @author linustouchtips
 * @since 11/24/2020
 */

public class EnemyUtil implements MixinInterface {

    public static boolean getArmor(EntityPlayer target, boolean melt, double durability) {
        for (ItemStack stack : target.getArmorInventoryList()) {
            if (stack == null || stack.getItem() == Items.AIR)
                return true;

            final float armorDurability = ((float) (stack.getMaxDamage() - stack.getItemDamage()) / (float) stack.getMaxDamage()) * 100.0f;
            if (melt && durability >= armorDurability)
                return true;
        }

        return false;
    }

    public static boolean enemyCheck(EntityPlayer enemy, int range) {
        if (enemy.getHealth() <= 0.0f) {
            return false;
        } if (mc.player.getDistanceSq(enemy) > range * range) {
            return false;
        } else return true;
    }

    public static List<BlockPos> getCityBlocks(final EntityPlayer player, final boolean crystal) {
        BlockPos playerPos = new BlockPos(player.posX, player.posY, player.posZ);
        NonNullList<BlockPos> cityBlocks = NonNullList.create();

        if (mc.world.getBlockState(playerPos.north()).getBlock() == Blocks.OBSIDIAN) {
            if (crystal) {
                cityBlocks.add(playerPos.north());
            } else if (mc.world.getBlockState(playerPos.north().north()).getBlock() == Blocks.AIR) {
                cityBlocks.add(playerPos.north());
            }
        }

        if (mc.world.getBlockState(playerPos.east()).getBlock() == Blocks.OBSIDIAN) {
            if (crystal) {
                cityBlocks.add(playerPos.east());
            } else if (mc.world.getBlockState(playerPos.east().east()).getBlock() == Blocks.AIR) {
                cityBlocks.add(playerPos.east());
            }
        }

        if (mc.world.getBlockState(playerPos.south()).getBlock() == Blocks.OBSIDIAN) {
            if (crystal) {
                cityBlocks.add(playerPos.south());
            } else if (mc.world.getBlockState(playerPos.south().south()).getBlock() == Blocks.AIR) {
                cityBlocks.add(playerPos.south());
            }

        }

        if (mc.world.getBlockState(playerPos.west()).getBlock() == Blocks.OBSIDIAN) {
            if (crystal) {
                cityBlocks.add(playerPos.west());
            } else if (mc.world.getBlockState(playerPos.west().west()).getBlock() == Blocks.AIR) {
                cityBlocks.add(playerPos.west());
            }
        }

        return cityBlocks;
    }

    public static boolean attackCheck(Entity entity, boolean players, boolean animals, boolean mobs) {
        if (players && entity instanceof EntityPlayer) {
            if (((EntityPlayer) entity).getHealth() > 0)
                return true;
        }

        if (animals && (EntityUtil.isPassive(entity) || EntityUtil.isNeutralMob(entity)))
            return true;

        if (mobs && EntityUtil.isHostileMob(entity))
            return true;

        else
            return false;
    }
}

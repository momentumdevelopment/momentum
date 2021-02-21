package me.linus.momentum.util.combat;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.world.BlockUtil;
import me.linus.momentum.util.world.BlockUtil.BlockResistance;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips
 * @since 11/24/2020
 */

public class EnemyUtil implements MixinInterface {

    public static float getHealth(EntityPlayer entityPlayer) {
        return entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount();
    }

    public static float getArmor(EntityPlayer target) {
        float armorDurability = 0;
        for (ItemStack stack : target.getArmorInventoryList()) {
            if (stack == null || stack.getItem() == Items.AIR)
                continue;

            armorDurability += ((float) (stack.getMaxDamage() - stack.getItemDamage()) / (float) stack.getMaxDamage()) * 100.0f;
        }

        return armorDurability;
    }

    public static boolean getArmor(EntityPlayer target, boolean melt, double durability) {
        for (ItemStack stack : target.getArmorInventoryList()) {
            if (stack == null || stack.getItem() == Items.AIR)
                return true;

            if (melt && durability >= ((float) (stack.getMaxDamage() - stack.getItemDamage()) / (float) stack.getMaxDamage()) * 100.0f)
                return true;
        }

        return false;
    }

    public static boolean getGearPlay(EntityPlayer target, double stacks) {
        if (InventoryUtil.getItemCount(target, Items.DIAMOND_HELMET) < 1 && (InventoryUtil.getItemCount(target, Items.EXPERIENCE_BOTTLE) / 64) < stacks)
            return true;

        return false;
    }

    public static List<BlockPos> getCityBlocks(EntityPlayer player, boolean crystal) {
        BlockPos playerPos = new BlockPos(player.posX, player.posY, player.posZ);
        List<BlockPos> cityBlocks = new ArrayList<>();

        if (BlockUtil.getBlockResistance(playerPos.north()) == BlockResistance.Resistant) {
            if (crystal)
                cityBlocks.add(playerPos.north());

            else if (BlockUtil.getBlockResistance(playerPos.north().north()) == BlockResistance.Blank)
                cityBlocks.add(playerPos.north());
        }

        if (BlockUtil.getBlockResistance(playerPos.east()) == BlockResistance.Resistant) {
            if (crystal)
                cityBlocks.add(playerPos.east());

            else if (BlockUtil.getBlockResistance(playerPos.east().east()) == BlockResistance.Blank)
                cityBlocks.add(playerPos.east());
        }

        if (BlockUtil.getBlockResistance(playerPos.south()) == BlockResistance.Resistant) {
            if (crystal)
                cityBlocks.add(playerPos.south());

            else if (BlockUtil.getBlockResistance(playerPos.south().south()) == BlockResistance.Blank)
                cityBlocks.add(playerPos.south());
        }

        if (BlockUtil.getBlockResistance(playerPos.west()) == BlockResistance.Resistant) {
            if (crystal)
                cityBlocks.add(playerPos.west());

            else if (BlockUtil.getBlockResistance(playerPos.west().west()) == BlockResistance.Blank)
                cityBlocks.add(playerPos.west());
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

        return false;
    }
}

package me.linus.momentum.util.player;

import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * @author linustouchtips
 * @since 11/24/2020
 */

public class PlayerUtil implements MixinInterface {

    public static double getHealth() {
        return mc.player.getHealth() + mc.player.getAbsorptionAmount();
    }

    public static void attackEntity(Entity entity) {
        if (mc.player.getCooledAttackStrength(0) >= 1) {
            mc.playerController.attackEntity(mc.player, entity);
            mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    public static int getArmorDurability() {
        int totalDurability = 0;

        for (ItemStack itemStack : mc.player.inventory.armorInventory)
            totalDurability = totalDurability + itemStack.getItemDamage();

        return totalDurability;
    }

    public static boolean inPlayer(BlockPos pos) {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(pos);
        return axisAlignedBB.intersects(mc.player.getEntityBoundingBox());
    }

    public static boolean isTrapped() {
        BlockPos playerPos = new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));

        final BlockPos[] trapPos = {
                playerPos.down(),
                playerPos.up().up(),
                playerPos.north(),
                playerPos.south(),
                playerPos.east(),
                playerPos.west(),
                playerPos.north().up(),
                playerPos.south().up(),
                playerPos.east().up(),
                playerPos.west().up(),
        };

        for (BlockPos pos : trapPos) {
            IBlockState state = mc.world.getBlockState(pos);

            if (state.getBlock() != Blocks.OBSIDIAN && mc.world.getBlockState(pos).getBlock() != Blocks.BEDROCK)
                return false;
        }

        return true;
    }

    public static boolean getArmor(EntityPlayer target, double durability) {
        for (ItemStack stack : target.getArmorInventoryList()) {
            if (stack == null || stack.getItem() == Items.AIR)
                return false;

            final float armorDurability = ((float) (stack.getMaxDamage() - stack.getItemDamage()) / (float) stack.getMaxDamage()) * 100.0f;
            if (durability >= armorDurability)
                return true;
        }

        return false;
    }

    public static Vec3d getCenter(double posX, double posY, double posZ) {
        double x = Math.floor(posX) + 0.5D;
        double y = Math.floor(posY);
        double z = Math.floor(posZ) + 0.5D;
        return new Vec3d(x, y, z);
    }

    public static boolean isInLiquid() {
        if (mc.player.fallDistance >= 3.0f)
            return false;

        if (mc.player != null) {
            boolean inLiquid = false;
            final AxisAlignedBB bb = mc.player.getRidingEntity() != null ? mc.player.getRidingEntity().getEntityBoundingBox() : mc.player.getEntityBoundingBox();
            int y = (int) bb.minY;

            for (int x = MathHelper.floor(bb.minX); x < MathHelper.floor(bb.maxX) + 1; x++) {
                for (int z = MathHelper.floor(bb.minZ); z < MathHelper.floor(bb.maxZ) + 1; z++) {
                    final Block block = mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();

                    if (!(block instanceof BlockAir)) {
                        if (!(block instanceof BlockLiquid))
                            return false;

                        inLiquid = true;
                    }
                }
            }

            return inLiquid;
        }

        return false;
    }
}

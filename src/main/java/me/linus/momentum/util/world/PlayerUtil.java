package me.linus.momentum.util.world;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.client.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.text.DecimalFormat;

/**
 * @author linustouchtips
 * @since 11/24/2020
 */

public class PlayerUtil implements MixinInterface {
    private static float roundedForward = getRoundedMovementInput(mc.player.movementInput.moveForward);
    private static float roundedStrafing = getRoundedMovementInput(mc.player.movementInput.moveStrafe);
    private static double prevPosX;
    private static double prevPosZ;
    private static Timer timer = new Timer();
    final static DecimalFormat Formatter = new DecimalFormat("#.#");

    public static double getHealth() {
        return mc.player.getHealth() + mc.player.getAbsorptionAmount();
    }

    public static boolean isInHole() {
        BlockPos blockPos = getLocalPlayerPosFloored();
        IBlockState blockState = mc.world.getBlockState(blockPos);

        if (blockState.getBlock() != Blocks.AIR)
            return false;

        if (mc.world.getBlockState(blockPos.up()).getBlock() != Blocks.AIR)
            return false;

        if (mc.world.getBlockState(blockPos.down()).getBlock() == Blocks.AIR)
            return false;

        final BlockPos[] touchingBlocks = new BlockPos[]{
                blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west()
        };

        int validHorizontalBlocks = 0;
        for (BlockPos touching : touchingBlocks) {
            final IBlockState touchingState = mc.world.getBlockState(touching);
            if ((touchingState.getBlock() != Blocks.AIR) && touchingState.isFullBlock())
                validHorizontalBlocks++;
        }

        if (validHorizontalBlocks < 4)
            return false;

        return true;
    }

    public static BlockPos getLocalPlayerPosFloored() {
        if (mc.player == null)
            return BlockPos.ORIGIN;

        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public static void attackEntity(Entity entity) {
        if (mc.player.getCooledAttackStrength(0) >= 1) {
            mc.playerController.attackEntity(mc.player, entity);
            mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    public static void placeBlock(final BlockPos pos, boolean rotate) {
        for (final EnumFacing enumFacing : EnumFacing.values()) {
            if (!mc.world.getBlockState(pos.offset(enumFacing)).getBlock().equals(Blocks.AIR) && !EntityUtil.isIntercepted(pos)) {
                final Vec3d vec = new Vec3d(pos.getX() + 0.5D + (double) enumFacing.getFrontOffsetX() * 0.5D, pos.getY() + 0.5D + (double) enumFacing.getFrontOffsetY() * 0.5D, pos.getZ() + 0.5D + (double) enumFacing.getFrontOffsetZ() * 0.5D);

                final float[] old = new float[] {
                        mc.player.rotationYaw, mc.player.rotationPitch
                };

                if (rotate)
                    mc.player.connection.sendPacket(new CPacketPlayer.Rotation((float) Math.toDegrees(Math.atan2((vec.z - mc.player.posZ), (vec.x - mc.player.posX))) - 90.0F, (float) (-Math.toDegrees(Math.atan2((vec.y - (mc.player.posY + (double) mc.player.getEyeHeight())), (Math.sqrt((vec.x - mc.player.posX) * (vec.x - mc.player.posX) + (vec.z - mc.player.posZ) * (vec.z - mc.player.posZ)))))), mc.player.onGround));

                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                mc.playerController.processRightClickBlock(mc.player, mc.world, pos.offset(enumFacing), enumFacing.getOpposite(), new Vec3d(pos), EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));

                if (rotate)
                    mc.player.connection.sendPacket(new CPacketPlayer.Rotation(old[0], old[1], mc.player.onGround));

                return;
            }
        }
    }

    public static boolean placeBlock(BlockPos pos, int slot, boolean rotate, boolean rotateBack) {
        if (BlockUtils.isBlockNotEmpty(pos) || mc.player.inventory.getStackInSlot(slot).getItem() == Item.getItemFromBlock(Blocks.WEB)) {
            for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))) {
                if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
                    return false;
                }
            }

            if (slot != mc.player.inventory.currentItem) {
                mc.player.inventory.currentItem = slot;
            }

            EnumFacing[] enumFacings = EnumFacing.values();

            for (EnumFacing enumFacing : enumFacings) {
                Block neighborBlock = mc.world.getBlockState(pos.offset(enumFacing)).getBlock();
                Vec3d vec = new Vec3d(pos.getX() + 0.5D + (double) enumFacing.getFrontOffsetX() * 0.5D, pos.getY() + 0.5D + (double) enumFacing.getFrontOffsetY() * 0.5D, pos.getZ() + 0.5D + (double) enumFacing.getFrontOffsetZ() * 0.5D);
                if (!BlockUtils.emptyBlocks.contains(neighborBlock) && mc.player.getPositionEyes(mc.getRenderPartialTicks()).distanceTo(vec) <= 4.25D) {

                    float[] rot = new float[]{
                            mc.player.rotationYaw,
                            mc.player.rotationPitch
                    };

                    if (rotate) {
                        rotateToPos(vec.x, vec.y, vec.z);
                    }

                    if (BlockUtils.rightClickableBlocks.contains(neighborBlock)) {
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                    }

                    mc.playerController.processRightClickBlock(mc.player, mc.world, pos.offset(enumFacing), enumFacing.getOpposite(), new Vec3d(pos), EnumHand.MAIN_HAND);

                    if (BlockUtils.rightClickableBlocks.contains(neighborBlock)) {
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                    }

                    if (rotateBack) {
                        mc.player.connection.sendPacket(new Rotation(rot[0], rot[1], mc.player.onGround));
                    }

                    return true;
                }
            }
        }

        return false;
    }

    public static void faceVectorPacketInstant(Vec3d vec) {
        final float[] rotations = getNeededRotations2(vec);
        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], rotations[1], mc.player.onGround));
    }

    private static float[] getNeededRotations2(Vec3d vec) {
        final Vec3d eyesPos = EntityUtil.getEyesPos();
        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        final float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));
        return new float[]{mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch)};
    }

    public static boolean isMoving() {
        return (mc.player.moveForward != 0.0D || mc.player.moveStrafing != 0.0D);
    }

    public static double getDirection() {
        float rotationYaw = mc.player.rotationYaw;

        if (mc.player.moveForward < 0f) rotationYaw += 180f;

        float forward = 1f;

        if (mc.player.moveForward < 0f) forward = -0.5f;
        else if (mc.player.moveForward > 0f) forward = 0.5f;

        if (mc.player.moveStrafing > 0f) rotationYaw -= 90f * forward;
        if (mc.player.moveStrafing < 0f) rotationYaw += 90f * forward;

        return Math.toRadians(rotationYaw);
    }

    public static double calcMoveYaw(float yawIn) {
        float moveForward = roundedForward;
        float moveString = roundedStrafing;

        float strafe = 90 * moveString;
        if (moveForward != 0f) {
            strafe *= moveForward * 0.5f;
        } else strafe *= 1f;

        float yaw = yawIn - strafe;
        if (moveForward < 0f) {
            yaw -= 180;
        } else yaw -= 0;

        return Math.toRadians(yaw);
    }

    private static float getRoundedMovementInput(Float input) {
        if (input > 0) {
            input = 1f;
        } else if (input < 0) {
            input = -1f;
        } else input = 0f;
        return input;
    }

    public static int getHotbarSlot(final Block block) {
        for (int i = 0; i < 9; i++) {
            final Item item = mc.player.inventory.getStackInSlot(i).getItem();

            if (item instanceof ItemBlock && ((ItemBlock) item).getBlock().equals(block)) return i;
        }

        return -1;
    }

    public static void rotateToPos(double x, double y, double z) {
        double diffX = x - mc.player.posX;
        double diffY = y - (mc.player.posY + (double) mc.player.getEyeHeight());
        double diffZ = z - mc.player.posZ;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));

        mc.player.connection.sendPacket(new Rotation(yaw, pitch, mc.player.onGround));
    }

    public static Vec3d direction(float yaw) {
        return new Vec3d(Math.cos(MathUtil.degToRad(yaw + 90f)), 0, Math.sin(MathUtil.degToRad(yaw + 90f)));
    }

    public static double[] directionSpeed(double speed) {
        float forward = mc.player.movementInput.moveForward;
        float side = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }

            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }

        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[] {
                posX, posZ
        };
    }

    public static boolean inPlayer(BlockPos pos) {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(pos);
        return axisAlignedBB.intersects(mc.player.getEntityBoundingBox());
    }

    public static void freezePlayer(double fallSpeed, double yOffset) {
        mc.player.motionX = 0.0;
        mc.player.motionY = 0.0;
        mc.player.motionZ = 0.0;
        mc.player.setVelocity(0f, 0f, 0f);
        mc.player.setPosition(mc.player.posX, mc.player.posY - fallSpeed + yOffset, mc.player.posZ);
    }

    public static void resetYaw() {
        if (mc.player.rotationYaw >= 40)
            mc.player.rotationYaw = 0;

        if (mc.player.rotationYaw <= 40)
            mc.player.rotationYaw = 0;
    }

    public static void resetPitch() {
        if (mc.player.rotationPitch >= 40)
            mc.player.rotationPitch = 0;

        if (mc.player.rotationPitch <= 40)
            mc.player.rotationPitch = 0;
    }

    public static String getSpeed() {
        if (timer.passed(1000)) {
            prevPosX = mc.player.prevPosX;
            prevPosZ = mc.player.prevPosZ;
        }

        final double deltaX = mc.player.posX - prevPosX;
        final double deltaZ = mc.player.posZ - prevPosZ;

        float distance = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        double KMH = Math.floor((distance / 1000.0f) / (0.05f / 3600.0f));

        String formatter = Formatter.format(KMH);

        if (!formatter.contains("."))
            formatter += ".0";

        final String bps = "Speed " + formatter + "km/h";

        return bps;
    }

    public String format(double input) {
        String result = Formatter.format(input);

        if (!result.contains("."))
            result += ".0";

        return result;
    }

    public static String getFacing() {
        switch (MathHelper.floor((double) (mc.player.rotationYaw * 8.0F / 360.0F) + 0.5D) & 7) {
            case 0:
                return "South";
            case 1:
                return "South West";
            case 2:
                return "West";
            case 3:
                return "North West";
            case 4:
                return "North";
            case 5:
                return "North East";
            case 6:
                return "East";
            case 7:
                return "South East";
        }

        return "Invalid";
    }

    public static String getTowards() {
        switch (MathHelper.floor((double) (mc.player.rotationYaw * 8.0F / 360.0F) + 0.5D) & 7) {
            case 0:
                return "+Z";
            case 1:
                return "-X +Z";
            case 2:
                return "-X";
            case 3:
                return "-X -Z";
            case 4:
                return "-Z";
            case 5:
                return "+X -Z";
            case 6:
                return "+X";
            case 7:
                return "+X +Z";
        }

        return "Invalid";
    }

    public static boolean isPlayerTrapped() {
        BlockPos playerPos = getLocalPlayerPosFloored();

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

    public static boolean hasMotion() {
        return mc.player.motionX != 0.0 && mc.player.motionZ != 0.0 && mc.player.motionY != 0.0;
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
}

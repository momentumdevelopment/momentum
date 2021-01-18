package me.linus.momentum.util.player.rotation;

import me.linus.momentum.event.events.player.RotationEvent;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.render.builder.RenderUtil;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * @author linustouchtips
 * @since 12/29/2020
 */

public class RotationUtil implements MixinInterface {

    /**
     * proper rotation spoofing system
     */

    // override vanilla packet sending here, we replace them with our own custom values
    public static void updateRotationPackets(RotationEvent event) {
        if (mc.player.isSprinting() != mc.player.serverSprintState) {
            if (mc.player.isSprinting())
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
            else
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));

            mc.player.serverSprintState = mc.player.isSprinting();
        }

        if (mc.player.isSneaking() != mc.player.serverSneakState) {
            if (mc.player.isSneaking())
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            else
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));

            mc.player.serverSneakState = mc.player.isSneaking();
        }

        if (mc.getRenderViewEntity() == mc.player) {
            double changePosX = mc.player.posX - mc.player.lastTickPosX;
            double changePosY = mc.player.getEntityBoundingBox().minY - mc.player.lastTickPosY;
            double changePosZ = mc.player.posZ - mc.player.lastTickPosZ;
            double changeYaw = event.getYaw() - mc.player.prevRotationYaw;
            double changePitch = event.getPitch() - mc.player.prevRotationPitch;

            mc.player.positionUpdateTicks++;

            boolean playerMoved = changePosX * changePosX + changePosY * changePosY + changePosZ * changePosZ > 9.0E-4D || mc.player.positionUpdateTicks >= 20;
            boolean playerRotated = changeYaw != 0.0D || changePitch != 0.0D;

            if (mc.player.isRiding()) {
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.motionX, -999.0D, mc.player.motionZ, event.getYaw(), event.getPitch(), mc.player.onGround));
                playerMoved = false;
            }

            else if (playerMoved && playerRotated)
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.getEntityBoundingBox().minY, mc.player.posZ, event.getYaw(), event.getPitch(), mc.player.onGround));
            else if (playerMoved)
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.getEntityBoundingBox().minY, mc.player.posZ, mc.player.onGround));
            else if (playerRotated)
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(event.getYaw(), event.getPitch(), mc.player.onGround));

            else if (mc.player.prevOnGround != mc.player.onGround)
                mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));

            if (playerMoved) {
                mc.player.lastReportedPosX = mc.player.posX;
                mc.player.lastReportedPosY = mc.player.getEntityBoundingBox().minY;
                mc.player.lastReportedPosZ = mc.player.posZ;
                mc.player.positionUpdateTicks = 0;
            }

            if (playerRotated) {
                mc.player.lastReportedYaw = event.getYaw();
                mc.player.lastReportedPitch = event.getPitch();
            }

            mc.player.prevOnGround = mc.player.onGround;
            mc.player.autoJumpEnabled = mc.player.mc.gameSettings.autoJump;
        }
    }

    // updates player rotations here
    public static void updateRotations(Rotation spoofRotation, int mode) {
       switch (mode) {
            case 0:
            case 1:
                mc.player.rotationYawHead = spoofRotation.yaw;
                break;
            case 2:
                mc.player.rotationYaw = spoofRotation.yaw;
                mc.player.rotationPitch = spoofRotation.pitch;
                break;
            case 3:
                break;
        }
    }

    public static void resetRotation(RotationEvent event) {
        event.setYaw(mc.player.rotationYaw);
        event.setPitch(mc.player.rotationPitch);
    }

    public static float[] getPositionAngles(BlockPos blockPos) {
        return MathUtil.calcAngle(new Vec3d(blockPos.x, blockPos.y, blockPos.z), new Vec3d(blockPos.x, blockPos.y, blockPos.z));
    }

    public static float[] getAngles(Entity entity) {
       return MathUtil.calcAngle(EntityUtil.interpolateEntityTime(mc.player, mc.getRenderPartialTicks()), EntityUtil.interpolateEntityTime(entity, mc.getRenderPartialTicks()));
    }

    public static float[] getTileAngles(TileEntity tileEntity) {
        return MathUtil.calcAngle(new Vec3d(tileEntity.getPos().x, tileEntity.getPos().y, tileEntity.getPos().z), new Vec3d(tileEntity.getPos().x, tileEntity.getPos().y, tileEntity.getPos().z));
    }

    /**
     * lock rotations
     */

    public static void resetYaw(double rotation) {
        if (mc.player.rotationYaw >= rotation)
            mc.player.rotationYaw = 0;

        if (mc.player.rotationYaw <= rotation)
            mc.player.rotationYaw = 0;
    }

    public static void resetPitch(double rotation) {
        if (mc.player.rotationPitch >= rotation)
            mc.player.rotationPitch = 0;

        if (mc.player.rotationPitch <= rotation)
            mc.player.rotationPitch = 0;
    }

    /**
     * player directions
     */

    public static boolean isInViewFrustrum(Entity entity) {
        return RenderUtil.camera.isBoundingBoxInFrustum(entity.getEntityBoundingBox());
    }

    public static Vec3d direction(float yaw) {
        return new Vec3d(Math.cos(MathUtil.degToRad(yaw + 90f)), 0, Math.sin(MathUtil.degToRad(yaw + 90f)));
    }

    public static double getDirection() {
        float rotationYaw = mc.player.rotationYaw;

        if (mc.player.moveForward < 0f)
            rotationYaw += 180f;

        float forward = 1f;

        if (mc.player.moveForward < 0f)
            forward = -0.5f;

        else if (mc.player.moveForward > 0f)
            forward = 0.5f;

        if (mc.player.moveStrafing > 0f)
            rotationYaw -= 90f * forward;

        if (mc.player.moveStrafing < 0f)
            rotationYaw += 90f * forward;

        return Math.toRadians(rotationYaw);
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
}

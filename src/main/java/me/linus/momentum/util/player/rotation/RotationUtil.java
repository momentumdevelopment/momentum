package me.linus.momentum.util.player.rotation;

import me.linus.momentum.event.events.player.RotationEvent;
import me.linus.momentum.managers.RotationManager;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.*;

import java.util.Random;

/**
 * @author linustouchtips
 * @since 12/29/2020
 */

public class RotationUtil implements MixinInterface {

    /**
     * proper rotation spoofing system
     */

    public static Rotation rotationStep(Rotation currentRotation, Rotation targetRotation, float rotationStep, int mode) {
        float yawDifference = ((targetRotation.yaw - currentRotation.yaw) % 360.0f + 540.0f) % 360.0f - 180.0f;
        float pitchDifference = ((targetRotation.pitch - currentRotation.pitch) % 360.0f + 540.0f) % 360.0f - 180.0f;

        switch (mode) {
            case 0:
                return targetRotation;
            case 1:
                return new Rotation(currentRotation.yaw + (yawDifference > rotationStep ? rotationStep : Math.max(yawDifference, -rotationStep)), currentRotation.pitch + (pitchDifference > rotationStep ? rotationStep : Math.max(pitchDifference, -rotationStep)), Rotation.RotationMode.Packet, targetRotation.rotationPriority);
            case 2:
                RotationManager.yawleftOver = yawDifference > rotationStep ? yawDifference - rotationStep : 0;
                RotationManager.pitchleftOver = pitchDifference > rotationStep ? pitchDifference - rotationStep : 0;
                RotationManager.resetTicks();
                return new Rotation(currentRotation.yaw + (yawDifference > rotationStep ? rotationStep : Math.max(yawDifference, -rotationStep)), currentRotation.pitch + (pitchDifference > rotationStep ? rotationStep : Math.max(pitchDifference, -rotationStep)), Rotation.RotationMode.Packet, targetRotation.rotationPriority);
        }

        return targetRotation;
    }

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
        
        double updatedPosX = mc.player.posX - mc.player.lastReportedPosX;
        double updatedPosY = mc.player.getEntityBoundingBox().minY - mc.player.lastReportedPosY;
        double updatedPosZ = mc.player.posZ - mc.player.lastReportedPosZ;
        
        double updatedRotationYaw = event.getYaw() - mc.player.lastReportedYaw;
        double updatedRotationPitch = event.getPitch() - mc.player.lastReportedPitch;
        
        mc.player.positionUpdateTicks++;
        
        boolean positionUpdate = updatedPosX * updatedPosX + updatedPosY * updatedPosY + updatedPosZ * updatedPosZ > 9.0E-4D || mc.player.positionUpdateTicks >= 20;
        boolean rotationUpdate = updatedRotationYaw != 0.0D || updatedRotationPitch != 0.0D;
        
        if (mc.player.isRiding()) {
            mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.motionX, -999.0D, mc.player.motionZ, event.getYaw(), event.getPitch(), mc.player.onGround));
            positionUpdate = false;
        }
        
        else if (positionUpdate && rotationUpdate) 
            mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.getEntityBoundingBox().minY, mc.player.posZ, event.getYaw(), event.getPitch(), mc.player.onGround));
        else if (positionUpdate)
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.getEntityBoundingBox().minY, mc.player.posZ, mc.player.onGround));
        else if (rotationUpdate) 
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(event.getYaw(), event.getPitch(), mc.player.onGround));
        else if (mc.player.prevOnGround != mc.player.onGround) 
            mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));

        if (positionUpdate) {
            mc.player.lastReportedPosX = mc.player.posX;
            mc.player.lastReportedPosY = mc.player.getEntityBoundingBox().minY;
            mc.player.lastReportedPosZ = mc.player.posZ;
            mc.player.positionUpdateTicks = 0;
        }

        if (rotationUpdate) {
            mc.player.lastReportedYaw = event.getYaw();
            mc.player.lastReportedPitch = event.getPitch();
        }

        mc.player.prevOnGround = mc.player.onGround;
        mc.player.autoJumpEnabled = mc.player.mc.gameSettings.autoJump;
    }

    public static float[] getAngles(Entity entity) {
       return calcAngle(EntityUtil.interpolateEntityTime(mc.player, mc.getRenderPartialTicks()), EntityUtil.interpolateEntityTime(entity, mc.getRenderPartialTicks()));
    }

    public static float[] getPositionAngles(BlockPos blockPos) {
        return calcAngle(EntityUtil.interpolateEntityTime(mc.player, mc.getRenderPartialTicks()), new Vec3d(blockPos));
    }

    public static RotationVector searchCenter(AxisAlignedBB axisAlignedBB, boolean outBorder, boolean random, boolean predict, boolean throughWalls) {
        Random randomDouble = new Random();
        
        double x = randomDouble.nextDouble();
        double y = randomDouble.nextDouble();
        double z = randomDouble.nextDouble();
        
        if (outBorder) {
            Vec3d outVector = new Vec3d(axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) * (x * 0.3 + 1.0), axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) * (y * 0.3 + 1.0), axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) * (z * 0.3 + 1.0));
            return new RotationVector(outVector, toRotation(outVector, predict));
        }
        
        Vec3d randomVector = new Vec3d(axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) * x * 0.8, axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) * y * 0.8, axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) * z * 0.8);
        float[] randomRotation = toRotation(randomVector, predict);

        RotationVector finalRotation = null;

        for (double xSearch = 0.15; xSearch < 0.85; xSearch += 0.1) {
            for (double ySearch = 0.15; ySearch < 1.0; ySearch += 0.1) {
                for (double zSearch = 0.15; zSearch < 0.85; zSearch += 0.1) {
                    Vec3d inVector = new Vec3d(axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) * xSearch, axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) * ySearch, axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) * zSearch);
                    float[] currentRotation = toRotation(inVector, predict);

                    if (throughWalls || isVisible(inVector)) {
                        RotationVector currentVector = new RotationVector(inVector, currentRotation);
                        if (finalRotation != null) {
                            if (random) {
                                if (getRotationDifference(currentVector.getRotation(), randomRotation) >= getRotationDifference(finalRotation.getRotation(), randomRotation))
                                    continue;
                            }

                            else if (getRotationDifference(currentVector.getRotation()) >= getRotationDifference(finalRotation.getRotation()))
                                continue;
                        }

                        finalRotation = currentVector;
                    }
                }
            }
        }
        
        return finalRotation;
    }

    public static RotationVector searchCenter(BlockPos blockPos) {
        RotationVector rotation = null;

        for (double xSearch = 0.1; xSearch < 0.9; xSearch += 0.1) {
            for (double ySearch = 0.1; ySearch < 0.9; ySearch += 0.1) {
                for (double zSearch = 0.1; zSearch < 0.9; zSearch += 0.1) {
                    Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.getEntityBoundingBox().minY + mc.player.getEyeHeight(), mc.player.posZ);
                    Vec3d posVec = new Vec3d(blockPos).add(xSearch, ySearch, zSearch);

                    double dist = eyesPos.distanceTo(posVec);

                    double diffX = posVec.x - eyesPos.x;
                    double diffY = posVec.y - eyesPos.y;
                    double diffZ = posVec.z - eyesPos.z;

                    double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

                    float[] calculatedRotation = new float[] {
                            MathHelper.wrapDegrees((float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F), MathHelper.wrapDegrees((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)))
                    };

                    Vec3d rotationVector = toVector(calculatedRotation);
                    Vec3d vector = eyesPos.add(rotationVector.x * dist, rotationVector.y * dist, rotationVector.z * dist);
                    RayTraceResult obj = mc.world.rayTraceBlocks(eyesPos, vector, false, false, true);

                    if (obj.typeOfHit == RayTraceResult.Type.BLOCK) {
                        RotationVector currentVector = new RotationVector(posVec, rotation.getRotation());

                        if (rotation == null || getRotationDifference(currentVector.getRotation()) < getRotationDifference(rotation.getRotation()))
                            rotation = currentVector;
                    }
                }
            }
        }

        return rotation;
    }

    public static float[] getTileAngles(TileEntity tileEntity) {
        return calcAngle(new Vec3d(tileEntity.getPos().x, tileEntity.getPos().y, tileEntity.getPos().z), new Vec3d(tileEntity.getPos().x, tileEntity.getPos().y, tileEntity.getPos().z));
    }

    public static void resetYaw(double rotation) {
        mc.player.rotationYaw = MathUtil.clamp(mc.player.rotationYaw, (float) -rotation, (float) rotation);
    }

    public static void resetPitch(double rotation) {
        mc.player.rotationPitch = MathUtil.clamp(mc.player.rotationPitch, (float) -rotation, (float) rotation);
    }

    public static float[] calcAngle(Vec3d from, Vec3d to) {
        return new float[] {
                (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(to.z - from.z, to.x - from.x)) - 90.0), (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2((to.y - from.y) * -1.0, MathHelper.sqrt(Math.pow(to.x - from.x, 2) + Math.pow(to.z - from.z, 2)))))
        };
    }

    public static double degToRad(double deg) {
        return deg * (float) (Math.PI / 180.0f);
    }

    public static float calculateAngle(float serverValue, float currentValue) {
        return ((currentValue - serverValue)) / 4;
    }

    public static boolean isVisible(Vec3d Vec3d) {
        Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.getEntityBoundingBox().minY + mc.player.getEyeHeight(), mc.player.posZ);
        return mc.world.rayTraceBlocks(eyesPos, Vec3d) == null;
    }

    public static double getRotationDifference(float[] rotation) {
        assert RotationManager.serverRotation != null;
        return getRotationDifference(rotation, new float[] { RotationManager.serverRotation.yaw, RotationManager.serverRotation.pitch });
    }

    public static double getRotationDifference(float[] from, float[] to) {
        return Math.hypot(getAngleDifference(from[0], to[0]), from[1] - to[1]);
    }

    public static float getAngleDifference(float from, float to) {
        return ((from - to) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }

    public static Vec3d toVector(float[] rotation) {
        float yawCos = MathHelper.cos(-rotation[0] * 0.017453292f - 3.1415927f);
        float yawSin = MathHelper.sin(-rotation[0] * 0.017453292f - 3.1415927f);
        float pitchCos = -MathHelper.cos(-rotation[1] * 0.017453292f);
        float pitchSin = MathHelper.sin(-rotation[1] * 0.017453292f);
        return new Vec3d((yawSin * pitchCos), pitchSin, (yawCos * pitchCos));
    }

    public static float[] toRotation(Vec3d vec, boolean predict) {
        Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.getEntityBoundingBox().minY + mc.player.getEyeHeight(), mc.player.posZ);
        
        if (predict) 
            eyesPos.add(mc.player.motionX, mc.player.motionY, mc.player.motionZ);

        return new float[] {
                MathHelper.wrapDegrees((float) Math.toDegrees(Math.atan2(vec.z - eyesPos.z, vec.x - eyesPos.x)) - 90.0f), MathHelper.wrapDegrees((float) (-Math.toDegrees(Math.atan2(vec.y - eyesPos.y, Math.sqrt(Math.pow(vec.x - eyesPos.x, 2) + Math.pow(vec.z - eyesPos.z, 2))))))
        };
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

        return "";
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

        return "";
    }
}

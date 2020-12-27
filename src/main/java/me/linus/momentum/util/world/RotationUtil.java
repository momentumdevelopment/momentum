package me.linus.momentum.util.world;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.system.MathUtil;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtil implements MixinInterface {

    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;

    /**
     * player directions
     */

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

    /**
     * client-side rotations
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

    public static void lookAtLegitTile(TileEntity tileEntity) {
        float[] angles = MathUtil.calcAngle(new Vec3d(tileEntity.getPos().x, tileEntity.getPos().y, tileEntity.getPos().z), new Vec3d(tileEntity.getPos().x, tileEntity.getPos().y, tileEntity.getPos().z));
        mc.player.rotationYaw = angles[0];
        mc.player.rotationPitch = angles[1];
    }

    public static void lookAtLegit(Entity entity) {
        float[] angles = MathUtil.calcAngle(EntityUtil.interpolateEntityTime(mc.player, mc.getRenderPartialTicks()), EntityUtil.interpolateEntityTime(entity, mc.getRenderPartialTicks()));
        mc.player.rotationYaw = angles[0];
        mc.player.rotationPitch = angles[1];
    }

    public static void lookAtPacket(double px, double py, double pz, EntityPlayer self) {
        double[] v = calculateLookAt(px, py, pz, self);
        setYawAndPitch((float) v[0], (float) v[1]);
    }

    public static void setYawAndPitch(float yaw1, float pitch1) {
        yaw = yaw1;
        pitch = pitch1;
        isSpoofingAngles = true;
    }

    public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer self) {
        double dirx = self.posX - px;
        double diry = self.posY - py;
        double dirz = self.posZ - pz;

        double len = Math.sqrt(dirx*dirx + diry*diry + dirz*dirz);

        dirx /= len;
        diry /= len;
        dirz /= len;

        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);

        pitch = pitch * 180.0d / Math.PI;
        yaw = yaw * 180.0d / Math.PI;
        yaw += 90f;

        return new double[] {
              yaw,pitch
        };
    }
    
    public static void resetRotation() {
        yaw = mc.player.rotationYaw;
        pitch = mc.player.rotationPitch;
    }
}

package me.linus.momentum.util.combat;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.system.MathUtil;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.Vec3d;

public class RotationUtil implements MixinInterface {

    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;



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

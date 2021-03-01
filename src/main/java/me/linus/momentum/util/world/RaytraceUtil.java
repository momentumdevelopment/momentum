package me.linus.momentum.util.world;

import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class RaytraceUtil implements MixinInterface {

    public static boolean raytraceBlock(BlockPos blockPos, double offset) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(blockPos.getX(), blockPos.getY() + offset, blockPos.getZ()), false, true, false) == null;
    }

    public static boolean raytraceQuill(BlockPos blockPos, double offset) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(blockPos.getX(), blockPos.getY() + offset + 1.5, blockPos.getZ()), false, true, false) == null;
    }

    public static boolean raytraceEntity(Entity entity) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(entity.posX, entity.posY, entity.posZ), false, true, false) == null;
    }

    public static boolean raytraceFeet(Entity entity) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(entity.posX, entity.posY, entity.posZ), false, true, false) == null;
    }
}

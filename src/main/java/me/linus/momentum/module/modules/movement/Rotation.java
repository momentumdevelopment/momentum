package me.linus.momentum.module.modules.movement;

import me.linus.momentum.event.events.render.Render3DEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.Objects;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class Rotation extends Module {
    public Rotation() {
        super("Rotation", Category.MOVEMENT, "Locks player rotation");
    }

    private static final Checkbox playerYaw = new Checkbox("Yaw", true);
    private static final Checkbox playerPitch = new Checkbox("Pitch", true);

    @Override
    public void setup() {
        addSetting(playerYaw);
        addSetting(playerPitch);
    }

    BlockPos northBlockPos;
    BlockPos southBlockPos;
    BlockPos eastBlockPos;
    BlockPos westBlockPos;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        final Vec3d vec3d = EntityUtil.getInterpolatedPos(mc.player, 0);
        northBlockPos = new BlockPos(vec3d).north();
        southBlockPos = new BlockPos(vec3d).south();
        eastBlockPos = new BlockPos(vec3d).east();
        westBlockPos = new BlockPos(vec3d).west();

        int angle = 360 / 4;

        float yaw = mc.player.rotationYaw;
        float pitch = mc.player.rotationPitch;

        yaw = Math.round(yaw / angle) * angle;
        pitch = Math.round(pitch / angle) * angle;

        if (playerYaw.getValue())
            mc.player.rotationYaw = yaw;

        if (playerPitch.getValue())
            mc.player.rotationPitch = pitch;

        if (mc.player.isRiding())
            Objects.requireNonNull(mc.player.getRidingEntity()).rotationYaw = yaw;
    }

    @Override
    public void onRender3D(Render3DEvent eventRender) {
        if (northBlockPos != null)
            RenderUtil.drawLine3D(northBlockPos.x, northBlockPos.y, northBlockPos.z, northBlockPos.x, northBlockPos.y + 3, northBlockPos.z, 1.5f, new Color(255, 0, 0).getRGB());

        if (westBlockPos != null)
            RenderUtil.drawLine3D(westBlockPos.x, westBlockPos.y, westBlockPos.z, westBlockPos.x, westBlockPos.y + 3, westBlockPos.z, 1.5f, new Color(255, 0, 0).getRGB());

        if (southBlockPos != null)
            RenderUtil.drawLine3D(southBlockPos.x, southBlockPos.y, southBlockPos.z, westBlockPos.x, southBlockPos.y + 3, southBlockPos.z, 1.5f, new Color(255, 0, 0).getRGB());

        if (eastBlockPos != null)
            RenderUtil.drawLine3D(eastBlockPos.x, eastBlockPos.y, eastBlockPos.z, eastBlockPos.x, eastBlockPos.y + 3, eastBlockPos.z, 1.5f, new Color(255, 0, 0).getRGB());
    }
}

package me.linus.momentum.module.modules.render;

import me.linus.momentum.event.events.render.Render3DEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.client.color.ColorUtil;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * @author linustouchtips
 * @since 11/29/2020
 */

public class Tracers extends Module {
    public Tracers() {
        super("Tracers", Category.RENDER, "Draws a line to entities");
    }

    private static final Checkbox players = new Checkbox("Players", true);
    private static final Checkbox animals = new Checkbox("Animals", true);
    private static final Checkbox mobs = new Checkbox("Mobs", true);
    private static final Checkbox items = new Checkbox("Items", false);

    private static final Slider lineWidth = new Slider("Line Width", 0.0D, 0.75D, 5.0D, 2);

    @Override
    public void setup() {
        addSetting(players);
        addSetting(animals);
        addSetting(mobs);
        addSetting(items);
        addSetting(lineWidth);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (nullCheck())
            return;

        if (mc.getRenderManager() == null || mc.getRenderManager().options == null)
            return;

        for (Entity entity : mc.world.loadedEntityList) {
            if (RenderUtil.shouldRenderTracer(entity, players.getValue(), mobs.getValue(), animals.getValue(), items.getValue())) {
                final Vec3d pos = EntityUtil.interpolateEntity(entity, event.getPartialTicks()).subtract(mc.getRenderManager().renderPosX, mc.getRenderManager().renderPosY, mc.getRenderManager().renderPosZ);

                if (pos != null) {
                    final boolean bobbing = mc.gameSettings.viewBobbing;
                    mc.gameSettings.viewBobbing = false;
                    mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
                    final Vec3d forward = new Vec3d(0, 0, 1).rotatePitch(-(float) Math.toRadians(mc.player.rotationPitch)).rotateYaw(-(float) Math.toRadians(mc.player.rotationYaw));
                    RenderUtil.drawLine3D((float) forward.x, (float) forward.y + mc.player.getEyeHeight(), (float) forward.z, (float) pos.x, (float) pos.y, (float) pos.z, (float) lineWidth.getValue(), ColorUtil.getEntityColor(entity).getRGB());
                    mc.gameSettings.viewBobbing = bobbing;
                    mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
                }
            }
        }
    }
}

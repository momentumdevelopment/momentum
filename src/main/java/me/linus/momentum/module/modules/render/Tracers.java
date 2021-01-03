package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.client.ColorUtil;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (nullCheck())
            return;

        if (mc.getRenderManager() == null || mc.getRenderManager().options == null)
            return;

        mc.world.loadedEntityList.stream().filter(entity -> mc.player != entity).forEach(entity -> {
            Vec3d pos = EntityUtil.interpolateEntity(entity, event.getPartialTicks()).subtract(mc.getRenderManager().renderPosX, mc.getRenderManager().renderPosY, mc.getRenderManager().renderPosZ);

            if (entity instanceof EntityPlayer && !(entity instanceof EntityPlayerSP) && players.getValue() || (EntityUtil.isPassive(entity) && animals.getValue()) || (EntityUtil.isHostileMob(entity) && mobs.getValue()) || entity instanceof EntityItem && items.getValue() && pos != null) {
                mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);

                Vec3d forward = new Vec3d(0, 0, 1).rotatePitch(-(float) Math.toRadians(mc.player.rotationPitch)).rotateYaw(-(float) Math.toRadians(mc.player.rotationYaw));
                RenderUtil.drawLine3D((float) forward.x, (float) forward.y + mc.player.getEyeHeight(), (float) forward.z, (float) pos.x, (float) pos.y, (float) pos.z, (float) lineWidth.getValue(), ColorUtil.getEntityColor(entity).getRGB());

                mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
            }
        });
    }
}

package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.color.SubColor;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.client.ColorUtil;
import me.linus.momentum.util.render.builder.RenderUtil;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 11/29/2020
 */

public class Tracers extends Module {
    public Tracers() {
        super("Tracers", Category.RENDER, "Draws a line to entities");
    }

    public static Checkbox players = new Checkbox("Players", true);
    public static SubColor playerPicker = new SubColor(players, new Color(215, 46, 46));

    public static Checkbox animals = new Checkbox("Animals", true);
    public static SubColor animalPicker = new SubColor(animals, new Color(0, 200, 0));

    public static Checkbox mobs = new Checkbox("Mobs", true);
    public static SubColor mobsPicker = new SubColor(mobs, new Color(131, 19, 199));

    public static Checkbox items = new Checkbox("Vehicles", true);
    public static SubColor itemsPicker = new SubColor(items, new Color(199, 103, 19));

    public static Checkbox crystals = new Checkbox("Crystals", true);
    public static SubColor crystalPicker = new SubColor(crystals, new Color(199, 19, 139));

    public static Slider lineWidth = new Slider("Line Width", 0.0D, 0.75D, 5.0D, 2);

    @Override
    public void setup() {
        addSetting(players);
        addSetting(animals);
        addSetting(mobs);
        addSetting(items);
        addSetting(crystals);
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
                RenderUtil.drawLine3D((float) forward.x, (float) forward.y + mc.player.getEyeHeight(), (float) forward.z, (float) pos.x, (float) pos.y, (float) pos.z, (float) lineWidth.getValue(), ColorUtil.getTracerColor(entity));

                mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
            }
        });
    }
}
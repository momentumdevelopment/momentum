package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.client.color.ColorUtil;
import me.linus.momentum.util.render.ESPUtil;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class ESP extends Module {
    public ESP() {
        super("ESP", Category.RENDER, "Highlights entities");
    }

    public static Mode mode = new Mode("Mode", "Outline", "Glow", "2D");
    private static SubCheckbox players = new SubCheckbox(mode, "Players", true);
    private static SubCheckbox animals = new SubCheckbox(mode, "Animals", true);
    private static SubCheckbox mobs = new SubCheckbox(mode, "Mobs", true);
    private static SubCheckbox vehicles = new SubCheckbox(mode, "Vehicles", true);
    private static SubCheckbox crystals = new SubCheckbox(mode, "Crystals", true);

    private static Slider lineWidth = new Slider("Line Width", 0.0D, 2.5D, 5.0D, 1);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(lineWidth);
    }

    public void onDisable() {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity.isGlowing())
                entity.setGlowing(false);
        }
    }

    @Override
    public void onUpdate() {
        for (Entity entity : mc.world.loadedEntityList) {
            if (!entity.isGlowing() && mode.getValue() == 1)
                entity.setGlowing(true);

            if (mode.getValue() != 1)
                entity.setGlowing(false);
        }
    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent eventRender) {
        if (mode.getValue() == 2)
            render2D();
    }

    public static void renderOutlines(ModelBase mainModel, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        if (entitylivingbaseIn == mc.player.getRidingEntity())
            return;

        RenderUtil.camera.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);

        if (!RenderUtil.camera.isBoundingBoxInFrustum(entitylivingbaseIn.getEntityBoundingBox()))
            return;

        if (entitylivingbaseIn instanceof EntityPlayer && !(entitylivingbaseIn instanceof EntityPlayerSP) && players.getValue() || (EntityUtil.isPassive(entitylivingbaseIn) && animals.getValue()) || (EntityUtil.isHostileMob(entitylivingbaseIn) && mobs.getValue()) || (EntityUtil.isVehicle(entitylivingbaseIn) && vehicles.getValue()) || (entitylivingbaseIn instanceof EntityEnderCrystal && crystals.getValue())) {
            GlStateManager.pushMatrix();
            Color color = ColorUtil.getEntityColor(entitylivingbaseIn);
            ESPUtil.setColor(color);
            mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            ESPUtil.renderOne((float) lineWidth.getValue());
            mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            ESPUtil.renderTwo();
            mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            ESPUtil.renderThree();
            ESPUtil.renderFour();
            ESPUtil.setColor(color);
            mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            ESPUtil.renderFive();
            ESPUtil.setColor(Color.WHITE);
            GL11.glColor4f(1f, 1f, 1f, 1f);
            GlStateManager.popMatrix();
        }
    }

    public void render2D() {
        if ((mc.getRenderManager()).options == null)
            return;

        boolean isThirdPersonFrontal = ((mc.getRenderManager()).options.thirdPersonView == 2);
        float viewerYaw = (mc.getRenderManager()).playerViewY;

        mc.world.loadedEntityList.stream().filter(entity -> (mc.player != entity)).forEach(entitylivingbaseIn -> {
            RenderUtil.prepareGL();
            GlStateManager.pushMatrix();
            Vec3d pos = EntityUtil.getInterpolatedPos(entitylivingbaseIn, mc.getRenderPartialTicks());
            GlStateManager.translate(pos.x - (mc.getRenderManager()).renderPosX, pos.y - (mc.getRenderManager()).renderPosY, pos.z - (mc.getRenderManager()).renderPosZ);
            GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((isThirdPersonFrontal ? -1 : 1), 1.0F, 0.0F, 0.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
            GL11.glLineWidth((float) lineWidth.getValue());
            GL11.glEnable(2848);

            if ((entitylivingbaseIn instanceof EntityPlayer && !(entitylivingbaseIn instanceof EntityPlayerSP) && players.getValue()) || (EntityUtil.isPassive(entitylivingbaseIn) && animals.getValue()) || (EntityUtil.isHostileMob(entitylivingbaseIn) && mobs.getValue()) || (EntityUtil.isVehicle(entitylivingbaseIn) && vehicles.getValue()) || (entitylivingbaseIn instanceof EntityEnderCrystal && crystals.getValue())) {
                Color color = ColorUtil.getEntityColor(entitylivingbaseIn);
                ESPUtil.setColor(color);
                ESPUtil.draw2D(entitylivingbaseIn);
            }

            RenderUtil.releaseGL();
            GlStateManager.popMatrix();
            ESPUtil.setColor(new Color(255, 255, 255, 255));
        });
    }


    @Override
    public String getHUDData() {
        return mode.getMode(mode.getValue());
    }
}

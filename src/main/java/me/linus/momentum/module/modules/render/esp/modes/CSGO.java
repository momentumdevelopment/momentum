package me.linus.momentum.module.modules.render.esp.modes;

import me.linus.momentum.module.modules.render.ESP;
import me.linus.momentum.module.modules.render.esp.ESPMode;
import me.linus.momentum.util.client.ColorUtil;
import me.linus.momentum.util.render.ESPUtil;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 01/06/2021
 */

public class CSGO extends ESPMode {

    @Override
    public void drawESPMixin(ModelBase mainModel, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        if (entitylivingbaseIn instanceof EntityPlayer && !(entitylivingbaseIn instanceof EntityPlayerSP) && ESP.players.getValue() || (EntityUtil.isPassive(entitylivingbaseIn) && ESP.animals.getValue()) || (EntityUtil.isHostileMob(entitylivingbaseIn) && ESP.mobs.getValue()) || (EntityUtil.isVehicle(entitylivingbaseIn) && ESP.vehicles.getValue()) || (entitylivingbaseIn instanceof EntityEnderCrystal && ESP.crystals.getValue())) {
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5f);
            GL11.glEnable(2960);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glEnable(10754);
            GL11.glColor4f(ColorUtil.getEntityColor(entitylivingbaseIn).getRed() / 255.0f, ColorUtil.getEntityColor(entitylivingbaseIn).getGreen() / 255.0f, ColorUtil.getEntityColor(entitylivingbaseIn).getBlue() / 255.0f, ColorUtil.getEntityColor(entitylivingbaseIn).getBlue() / 255.0f);
            mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
        }
    }

    @Override
    public void drawESPCrystal(ModelBase modelEnderCrystal, ModelBase modelEnderCrystalNoBase, EntityEnderCrystal entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo callback, ResourceLocation texture) {
        GL11.glPushMatrix();

        float rotation = entity.innerRotation + partialTicks;
        GlStateManager.translate(x, y, z);
        mc.renderManager.renderEngine.bindTexture(texture);
        float rotationMoved = MathHelper.sin(rotation * 0.2f) / 2.0f + 0.5f;
        rotationMoved += rotationMoved * rotationMoved;
        GL11.glEnable(32823);
        GL11.glPolygonOffset(1.0f, -1.0E7f);
        GL11.glPushAttrib(1048575);
        GL11.glPolygonMode(1028, 6914);

        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        ESPUtil.setColor(ColorUtil.getEntityColor(entity));

        if (entity.shouldShowBottom())
            modelEnderCrystal.render(entity, 0.0f, rotation * 3.0f, rotationMoved * 0.2f, 0.0f, 0.0f, 0.0625f);
        else
            modelEnderCrystalNoBase.render(entity, 0.0f, rotation * 3.0f, rotationMoved * 0.2f, 0.0f, 0.0f, 0.0625f);

        GL11.glPopAttrib();
        GL11.glPolygonOffset(1.0f, 100000.0f);
        GL11.glDisable(32823);
        GL11.glPopMatrix();
    }
}
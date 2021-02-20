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

import static org.lwjgl.opengl.GL11.*;

/**
 * @author linustouchtips
 * @since 01/06/2021
 */

public class WireFrame extends ESPMode {

    @Override
    public void drawESPMixin(ModelBase mainModel, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        if (entitylivingbaseIn instanceof EntityPlayer && !(entitylivingbaseIn instanceof EntityPlayerSP) && ESP.players.getValue() || (EntityUtil.isPassive(entitylivingbaseIn) && ESP.animals.getValue()) || (EntityUtil.isHostileMob(entitylivingbaseIn) && ESP.mobs.getValue()) || (EntityUtil.isVehicle(entitylivingbaseIn) && ESP.vehicles.getValue()) || (entitylivingbaseIn instanceof EntityEnderCrystal && ESP.crystals.getValue())) {
            GL11.glPushMatrix();
            GL11.glPushAttrib(GL_ALL_ATTRIB_BITS);
            GL11.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            GL11.glDisable(GL_TEXTURE_2D);
            GL11.glDisable(GL_LIGHTING);
            GL11.glDisable(GL_DEPTH_TEST);
            GL11.glEnable(GL_LINE_SMOOTH);
            GL11.glEnable(GL_BLEND);
            GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            ESPUtil.setColor(ColorUtil.getEntityColor(entitylivingbaseIn));
            mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            GL11.glLineWidth((float) ESP.lineWidth.getValue());
            GL11.glPopAttrib();
            GL11.glPopMatrix();
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
        GL11.glEnable(GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(1.0f, -1.0E7f);
        GL11.glPushAttrib(GL_ALL_ATTRIB_BITS);
        GL11.glPolygonMode(GL_FRONT, GL_LINE);

        GL11.glDisable(GL_TEXTURE_2D);
        GL11.glDisable(GL_LIGHTING);
        GL11.glDisable(GL_DEPTH_TEST);
        GL11.glEnable(GL_LINE_SMOOTH);
        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        ESPUtil.setColor(ColorUtil.getEntityColor(entity));

        if (entity.shouldShowBottom())
            modelEnderCrystal.render(entity, 0.0f, rotation * 3.0f, rotationMoved * 0.2f, 0.0f, 0.0f, 0.0625f);
        else
            modelEnderCrystalNoBase.render(entity, 0.0f, rotation * 3.0f, rotationMoved * 0.2f, 0.0f, 0.0f, 0.0625f);

        GL11.glPopAttrib();
        GL11.glPolygonOffset(1.0f, 100000.0f);
        GL11.glDisable(GL_POLYGON_OFFSET_FILL);
        GL11.glPopMatrix();
    }
}
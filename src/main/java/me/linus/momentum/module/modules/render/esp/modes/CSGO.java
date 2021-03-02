package me.linus.momentum.module.modules.render.esp.modes;

import me.linus.momentum.managers.social.friend.FriendManager;
import me.linus.momentum.module.modules.render.ESP;
import me.linus.momentum.module.modules.render.esp.ESPMode;
import me.linus.momentum.util.render.ESPUtil;
import me.linus.momentum.util.render.RenderUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.Profile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author linustouchtips
 * @since 01/06/2021
 */

public class CSGO extends ESPMode {
    public CSGO() {
        isMixin = true;
    }

    @Override
    public void drawESPMixin(ModelBase mainModel, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        RenderUtil.camera.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);

        if (!RenderUtil.camera.isBoundingBoxInFrustum(entitylivingbaseIn.getEntityBoundingBox()))
            return;

        if (ESP.colorManager.abstractColorRegistry.containsKey(entitylivingbaseIn.getClass())) {
            GlStateManager.enableBlendProfile(Profile.TRANSPARENT_MODEL);

            glPushMatrix();
            glPushAttrib(GL_ALL_ATTRIB_BITS);
            glDisable(GL_ALPHA_TEST);
            glDisable(GL_DEPTH_TEST);

            if (ESP.xqz.getValue())
                ESPUtil.setColor(FriendManager.isFriend(entitylivingbaseIn.getName()) ? ESP.colorManager.colorRegistry.get("Friend") : ESP.colorManager.colorRegistry.get("XQZ"));
            else
                ESPUtil.setColor(FriendManager.isFriend(entitylivingbaseIn.getName()) ? ESP.colorManager.colorRegistry.get("Friend") : ESP.colorManager.abstractColorRegistry.get(entitylivingbaseIn.getClass()));

            glDisable(GL_TEXTURE_2D);
            mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            glEnable(GL_DEPTH_TEST);
            ESPUtil.setColor(FriendManager.isFriend(entitylivingbaseIn.getName()) ? ESP.colorManager.colorRegistry.get("Friend") : ESP.colorManager.abstractColorRegistry.get(entitylivingbaseIn.getClass()));
            mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

            if (ESP.xqz.getValue())
                ESPUtil.setColor(FriendManager.isFriend(entitylivingbaseIn.getName()) ? ESP.colorManager.colorRegistry.get("Friend") : ESP.colorManager.colorRegistry.get("XQZ"));
            else
                ESPUtil.setColor(FriendManager.isFriend(entitylivingbaseIn.getName()) ? ESP.colorManager.colorRegistry.get("Friend") : ESP.colorManager.abstractColorRegistry.get(entitylivingbaseIn.getClass()));

            glEnable(GL_TEXTURE_2D);
            glEnable(GL_ALPHA_TEST);
            glPopAttrib();
            glPopMatrix();
        }
    }

    @Override
    public void drawESPCrystal(ModelBase modelEnderCrystal, ModelBase modelEnderCrystalNoBase, EntityEnderCrystal entityEnderCrystalIn, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo callback, ResourceLocation texture) {
        RenderUtil.camera.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);

        if (!RenderUtil.camera.isBoundingBoxInFrustum(entityEnderCrystalIn.getEntityBoundingBox()))
            return;

        if (ESP.colorManager.abstractColorRegistry.containsKey(entityEnderCrystalIn.getClass())) {
            GlStateManager.enableBlendProfile(Profile.TRANSPARENT_MODEL);

            glPushMatrix();

            float rotation = entityEnderCrystalIn.innerRotation + partialTicks;
            GlStateManager.translate(x, y, z);
            mc.renderManager.renderEngine.bindTexture(texture);
            float rotationMoved = MathHelper.sin(rotation * 0.2f) / 2.0f + 0.5f;
            rotationMoved += rotationMoved * rotationMoved;
            glDisable(GL_ALPHA_TEST);
            glDisable(GL_DEPTH_TEST);

            if (ESP.xqz.getValue())
                ESPUtil.setColor(ESP.colorManager.colorRegistry.get("XQZ"));
            else
                ESPUtil.setColor(ESP.colorManager.abstractColorRegistry.get(entityEnderCrystalIn.getClass()));

            glDisable(GL_TEXTURE_2D);

            if (entityEnderCrystalIn.shouldShowBottom())
                modelEnderCrystal.render(entityEnderCrystalIn, 0.0f, rotation * 3.0f, rotationMoved * 0.2f, 0.0f, 0.0f, 0.0625f);
            else
                modelEnderCrystalNoBase.render(entityEnderCrystalIn, 0.0f, rotation * 3.0f, rotationMoved * 0.2f, 0.0f, 0.0f, 0.0625f);

            glEnable(GL_DEPTH_TEST);
            ESPUtil.setColor(ESP.colorManager.abstractColorRegistry.get(entityEnderCrystalIn.getClass()));

            if (entityEnderCrystalIn.shouldShowBottom())
                modelEnderCrystal.render(entityEnderCrystalIn, 0.0f, rotation * 3.0f, rotationMoved * 0.2f, 0.0f, 0.0f, 0.0625f);
            else
                modelEnderCrystalNoBase.render(entityEnderCrystalIn, 0.0f, rotation * 3.0f, rotationMoved * 0.2f, 0.0f, 0.0f, 0.0625f);

            if (ESP.xqz.getValue())
                ESPUtil.setColor(ESP.colorManager.colorRegistry.get("XQZ"));
            else
                ESPUtil.setColor(ESP.colorManager.abstractColorRegistry.get(entityEnderCrystalIn.getClass()));

            glEnable(GL_TEXTURE_2D);
            glEnable(GL_ALPHA_TEST);
            glPopMatrix();
        }
    }
}
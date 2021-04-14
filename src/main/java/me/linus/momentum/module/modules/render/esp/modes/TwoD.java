package me.linus.momentum.module.modules.render.esp.modes;

import me.linus.momentum.managers.social.friend.FriendManager;
import me.linus.momentum.module.modules.render.ESP;
import me.linus.momentum.module.modules.render.esp.ESPMode;
import me.linus.momentum.util.render.ESPUtil;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;


/**
 * @author linustouchtips
 * @since 01/06/2021
 */

public class TwoD extends ESPMode {
    public TwoD() {
        isRender = true;
    }

    @Override
    public void drawESP() {
        if (mc.getRenderManager().options == null)
            return;

        mc.world.loadedEntityList.stream().filter(entity -> (mc.player != entity)).forEach(entitylivingbaseIn -> {
            RenderUtil.camera.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);

            if (!RenderUtil.camera.isBoundingBoxInFrustum(entitylivingbaseIn.getEntityBoundingBox()))
                return;

            if (ESP.colorManager.abstractColorRegistry.containsKey(entitylivingbaseIn.getClass()) && (entitylivingbaseIn instanceof EntityPlayer && !(entitylivingbaseIn instanceof EntityPlayerSP) && ESP.players.getValue() || (EntityUtil.isPassive(entitylivingbaseIn) && ESP.animals.getValue()) || (EntityUtil.isHostileMob(entitylivingbaseIn) && ESP.mobs.getValue()) || (EntityUtil.isVehicle(entitylivingbaseIn) && ESP.vehicles.getValue()) || (entitylivingbaseIn instanceof EntityEnderCrystal && ESP.crystals.getValue()))) {
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.glLineWidth(1.5F);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.disableLighting();
                GlStateManager.disableCull();
                GlStateManager.enableAlpha();
                ESPUtil.setColor(Color.WHITE);
                GlStateManager.pushMatrix();
                GlStateManager.translate(EntityUtil.getInterpolatedPos(entitylivingbaseIn, mc.getRenderPartialTicks()).x - (mc.getRenderManager()).renderPosX, EntityUtil.getInterpolatedPos(entitylivingbaseIn, mc.getRenderPartialTicks()).y - (mc.getRenderManager()).renderPosY, EntityUtil.getInterpolatedPos(entitylivingbaseIn, mc.getRenderPartialTicks()).z - (mc.getRenderManager()).renderPosZ);
                GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate((mc.getRenderManager().options.thirdPersonView == 2 ? -1 : 1), 1.0F, 0.0F, 0.0F);
                ESPUtil.setColor(Color.WHITE);
                glLineWidth(1.0f);
                glEnable(GL_LINE_SMOOTH);
                ESPUtil.setColor(FriendManager.isFriend(entitylivingbaseIn.getName()) ? ESP.colorManager.colorRegistry.get("Friend") : ESP.colorManager.abstractColorRegistry.get(entitylivingbaseIn.getClass()));
                ESPUtil.draw2D(entitylivingbaseIn);
                GlStateManager.enableCull();
                GlStateManager.depthMask(true);
                GlStateManager.enableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.enableDepth();
                ESPUtil.setColor(Color.WHITE);
                GlStateManager.popMatrix();
                ESPUtil.setColor(Color.WHITE);
            }
        });
    }
}

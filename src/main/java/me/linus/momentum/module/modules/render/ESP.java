package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.ColorUtil;
import me.linus.momentum.util.render.ESPUtil;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class ESP extends Module {
    public ESP() {
        super("ESP", Category.RENDER, "Highlights entities");
    }

    public static Mode mode = new Mode("Mode", "Outline", "Glow", "2D", "Wire-Frame", "CS:GO");
    private static SubCheckbox players = new SubCheckbox(mode, "Players", true);
    private static SubCheckbox animals = new SubCheckbox(mode, "Animals", true);
    private static SubCheckbox mobs = new SubCheckbox(mode, "Mobs", true);
    private static SubCheckbox vehicles = new SubCheckbox(mode, "Vehicles", true);
    public static SubCheckbox crystals = new SubCheckbox(mode, "Crystals", true);

    public static Slider lineWidth = new Slider("Line Width", 0.0D, 2.5D, 5.0D, 1);

    public static Checkbox color = new Checkbox("Color", true);
    public static SubSlider r = new SubSlider(color, "Red", 0.0D, 255.0D, 255.0D, 0);
    public static SubSlider g = new SubSlider(color, "Green", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider b = new SubSlider(color, "Blue", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider a = new SubSlider(color, "Alpha", 0.0D, 144.0D, 255.0D, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(lineWidth);
        addSetting(color);
    }

    @Override
    public void onDisable() {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity.isGlowing())
                entity.setGlowing(false);
        }
    }

    @Override
    public void onUpdate() {
        mc.gameSettings.fancyGraphics = true;

        renderGlow();
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
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
            if (mode.getValue() == 0) {
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
            }
        }
    }

    public static void renderChams(ModelBase mainModel, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        if (entitylivingbaseIn instanceof EntityPlayer && !(entitylivingbaseIn instanceof EntityPlayerSP) && players.getValue() || (EntityUtil.isPassive(entitylivingbaseIn) && animals.getValue()) || (EntityUtil.isHostileMob(entitylivingbaseIn) && mobs.getValue()) || (EntityUtil.isVehicle(entitylivingbaseIn) && vehicles.getValue()) || (entitylivingbaseIn instanceof EntityEnderCrystal && crystals.getValue())) {
            if (mode.getValue() == 3) {
                GL11.glPushMatrix();
                GL11.glPushAttrib(1048575);
                GL11.glPolygonMode(1032, 6913);
                GL11.glDisable(3553);
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                GL11.glEnable(2848);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                ESPUtil.setColor(ColorUtil.getEntityColor(entitylivingbaseIn));
                mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
                GL11.glLineWidth((float) lineWidth.getValue());
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }

            if (mode.getValue() == 4) {
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
                GL11.glColor4f(ColorUtil.getEntityColor(entitylivingbaseIn).getRed() / 255.0f, ColorUtil.getEntityColor(entitylivingbaseIn).getGreen() / 255.0f, ColorUtil.getEntityColor(entitylivingbaseIn).getBlue() / 255.0f, (int) a.getValue() / 255.0f);
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
    }

    public static void renderCrystal(ModelBase modelEnderCrystal, ModelBase modelEnderCrystalNoBase, EntityEnderCrystal entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo callback, ResourceLocation texture) {
        if (mode.getValue() == 0) {
            float rotation = entity.innerRotation + partialTicks;
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            mc.renderManager.renderEngine.bindTexture(texture);
            float rotationRounded = MathHelper.sin(rotation * 0.2f) / 2.0f + 0.5f;
            rotationRounded += rotationRounded * rotationRounded;
            GL11.glLineWidth((float) lineWidth.getValue());

            if (entity.shouldShowBottom())
                modelEnderCrystal.render(entity, 0.0f, rotation * 3.0f, rotationRounded * 0.2f, 0.0f, 0.0f, 0.0625f);
            else
                modelEnderCrystalNoBase.render(entity, 0.0f, rotation * 3.0f, rotationRounded * 0.2f, 0.0f, 0.0f, 0.0625f);

            ESPUtil.renderOne((float) lineWidth.getValue());

            if (entity.shouldShowBottom())
                modelEnderCrystal.render(entity, 0.0f, rotation * 3.0f, rotationRounded * 0.2f, 0.0f, 0.0f, 0.0625f);
            else
                modelEnderCrystalNoBase.render(entity, 0.0f, rotation * 3.0f, rotationRounded * 0.2f, 0.0f, 0.0f, 0.0625f);

            ESPUtil.renderTwo();

            if (entity.shouldShowBottom())
                modelEnderCrystal.render(entity, 0.0f, rotation * 3.0f, rotationRounded * 0.2f, 0.0f, 0.0f, 0.0625f);
            else
                modelEnderCrystalNoBase.render(entity, 0.0f, rotation * 3.0f, rotationRounded * 0.2f, 0.0f, 0.0f, 0.0625f);

            ESPUtil.renderThree();
            ESPUtil.renderFour();
            ESPUtil.setColor(new Color((int) r.getValue() / 255.0f, (int) g.getValue() / 255.0f, (int) b.getValue() / 255.0f, 1.0f));

            if (entity.shouldShowBottom())
                modelEnderCrystal.render(entity, 0.0f, rotation * 3.0f, rotationRounded * 0.2f, 0.0f, 0.0f, 0.0625f);
            else
                modelEnderCrystalNoBase.render(entity, 0.0f, rotation * 3.0f, rotationRounded * 0.2f, 0.0f, 0.0f, 0.0625f);

            ESPUtil.renderFive();
            GlStateManager.popMatrix();
        }

        if (mode.getValue() == 3 || mode.getValue() == 4)  {
            GL11.glPushMatrix();

            float rotation = entity.innerRotation + partialTicks;
            GlStateManager.translate(x, y, z);
            mc.renderManager.renderEngine.bindTexture(texture);
            float rotationMoved = MathHelper.sin(rotation * 0.2f) / 2.0f + 0.5f;
            rotationMoved += rotationMoved * rotationMoved;
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1.0E7f);
            GL11.glPushAttrib(1048575);

            if (mode.getValue() == 3)
                GL11.glPolygonMode(1028, 6913);
            else if (mode.getValue() == 4)
                GL11.glPolygonMode(1028, 6914);

            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glEnable(2848);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f((int) r.getValue() / 255.0f, (int) g.getValue() / 255.0f, (int) b.getValue() / 255.0f, (int) a.getValue() / 255.0f);

            if (mode.getValue() == 3)
                GL11.glLineWidth((float) ESP.lineWidth.getValue());

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

    public void renderGlow() {
        for (Entity entitylivingbaseIn : mc.world.loadedEntityList) {
            if (!entitylivingbaseIn.isGlowing() && mode.getValue() == 1 && (entitylivingbaseIn instanceof EntityPlayer && !(entitylivingbaseIn instanceof EntityPlayerSP) && players.getValue()) || (EntityUtil.isPassive(entitylivingbaseIn) && animals.getValue()) || (EntityUtil.isHostileMob(entitylivingbaseIn) && mobs.getValue()) || (EntityUtil.isVehicle(entitylivingbaseIn) && vehicles.getValue()) || (entitylivingbaseIn instanceof EntityEnderCrystal && crystals.getValue()))
                entitylivingbaseIn.setGlowing(true);

            if (mode.getValue() != 1)
                entitylivingbaseIn.setGlowing(false);
        }
    }

    public void render2D() {
        if ((mc.getRenderManager()).options == null)
            return;

        boolean isThirdPersonFrontal = ((mc.getRenderManager()).options.thirdPersonView == 2);
        float viewerYaw = (mc.getRenderManager()).playerViewY;

        mc.world.loadedEntityList.stream().filter(entity -> (mc.player != entity)).forEach(entitylivingbaseIn -> {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.glLineWidth(1.5F);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.enableAlpha();
            GlStateManager.color(1, 1, 1);
            GlStateManager.pushMatrix();
            Vec3d pos = EntityUtil.getInterpolatedPos(entitylivingbaseIn, mc.getRenderPartialTicks());
            GlStateManager.translate(pos.x - (mc.getRenderManager()).renderPosX, pos.y - (mc.getRenderManager()).renderPosY, pos.z - (mc.getRenderManager()).renderPosZ);
            GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((isThirdPersonFrontal ? -1 : 1), 1.0F, 0.0F, 0.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
            GL11.glLineWidth(1.0f);
            GL11.glEnable(2848);

            if ((entitylivingbaseIn instanceof EntityPlayer && !(entitylivingbaseIn instanceof EntityPlayerSP) && players.getValue()) || (EntityUtil.isPassive(entitylivingbaseIn) && animals.getValue()) || (EntityUtil.isHostileMob(entitylivingbaseIn) && mobs.getValue()) || (EntityUtil.isVehicle(entitylivingbaseIn) && vehicles.getValue()) || (entitylivingbaseIn instanceof EntityEnderCrystal && crystals.getValue())) {
                Color color = ColorUtil.getEntityColor(entitylivingbaseIn);
                ESPUtil.setColor(color);
                ESPUtil.draw2D(entitylivingbaseIn);
            }

            GlStateManager.enableCull();
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.enableDepth();
            GlStateManager.color(1, 1, 1);
            GL11.glColor4f(1, 1, 1, 1);
            GlStateManager.popMatrix();
            ESPUtil.setColor(new Color(255, 255, 255, 255));
        });
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}

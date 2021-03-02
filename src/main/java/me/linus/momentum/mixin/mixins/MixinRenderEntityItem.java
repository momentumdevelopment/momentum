package me.linus.momentum.mixin.mixins;

import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.modules.render.ESP;
import me.linus.momentum.util.render.ESPUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

/**
 * @author linustouchtips
 * @since 02/28/2021
 */

@Mixin(value = RenderEntityItem.class)
public abstract class MixinRenderEntityItem extends Render<EntityItem> implements MixinInterface {
    protected MixinRenderEntityItem() {
        super(null);
    }

    @Inject(method = "doRender", at = @At("INVOKE"), cancellable = true)
    public void doRenderInvoke(EntityItem entityItemIn, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if (ModuleManager.getModuleByName("ESP").isEnabled() && ESP.colorManager.abstractColorRegistry.containsKey(entityItemIn.getClass())) {
            GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);

            IBakedModel ibakedmodel = mc.itemRenderer.itemRenderer.getItemModelWithOverrides(entityItemIn.getItem(), mc.world, null);

            glPushMatrix();

            if (ESP.xqz.getValue())
                ESPUtil.setColor(ESP.colorManager.colorRegistry.get("XQZ"));
            else
                ESPUtil.setColor(ESP.colorManager.abstractColorRegistry.get(entityItemIn.getClass()));

            IBakedModel transformedModel = ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
            mc.itemRenderer.itemRenderer.renderItem(entityItemIn.getItem(), transformedModel);
            ESPUtil.setColor(ESP.colorManager.abstractColorRegistry.get(entityItemIn.getClass()));
            mc.itemRenderer.itemRenderer.renderItem(entityItemIn.getItem(), transformedModel);

            if (ESP.xqz.getValue())
                ESPUtil.setColor(ESP.colorManager.colorRegistry.get("XQZ"));
            else
                ESPUtil.setColor(ESP.colorManager.abstractColorRegistry.get(entityItemIn.getClass()));

            glPopMatrix();
        }
    }
}

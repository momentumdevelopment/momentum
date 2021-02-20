package me.linus.momentum.module.modules.render.esp;

import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author linustouchtips
 * @since 01/06/2021
 */

public class ESPMode implements MixinInterface {

    public void drawESP() {

    }

    public void drawESPPre(double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {

    }

    public void drawESPPost(double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {

    }

    public void drawESPMixin(ModelBase mainModel, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {

    }

    public void drawESPCrystal(ModelBase modelEnderCrystal, ModelBase modelEnderCrystalNoBase, EntityEnderCrystal entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo callback, ResourceLocation texture) {

    }
}

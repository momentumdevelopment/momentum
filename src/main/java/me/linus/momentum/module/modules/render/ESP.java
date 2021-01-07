package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.module.modules.render.esp.ESPMode;
import me.linus.momentum.module.modules.render.esp.modes.*;
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
 * @updated 01/06/2021
 */

public class ESP extends Module {
    public ESP() {
        super("ESP", Category.RENDER, "Highlights entities");
    }

    public static Mode mode = new Mode("Mode", "Outline", "Glow", "2D", "Wire-Frame", "CS:GO");
    public static SubCheckbox players = new SubCheckbox(mode, "Players", true);
    public static SubCheckbox animals = new SubCheckbox(mode, "Animals", true);
    public static SubCheckbox mobs = new SubCheckbox(mode, "Mobs", true);
    public static SubCheckbox vehicles = new SubCheckbox(mode, "Vehicles", true);
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

    public static ESPMode espMode;

    @Override
    public void onDisable() {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity.isGlowing())
                entity.setGlowing(false);
        }
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        mc.gameSettings.fancyGraphics = true;

        switch (mode.getValue()) {
            case 0:
                espMode = new Outline();
                break;
            case 1:
                espMode = new Glow();
                break;
            case 2:
                espMode = new TwoD();
                break;
            case 3:
                espMode = new WireFrame();
                break;
            case 4:
                espMode = new CSGO();
                break;
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (mode.getValue() == 1 || mode.getValue() == 2)
            espMode.drawESP();
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}

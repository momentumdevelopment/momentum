package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.render.builder.RenderBuilder;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.Timer;
import me.linus.momentum.util.player.InventoryUtil;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Comparator;

/**
 * @author olliem5 & linustouchtips
 * @since 7/01/20
 *
 * Credit - Max (java!) for the idea
 *
 * TODO: Checks for all types of pressure plates
 * TODO: Stop pressure plates from being placed right after crystal explodes
 */

public class AntiCrystal extends Module {
    public AntiCrystal() {
        super("AntiCrystal", Category.COMBAT, "Minimises crystal damage with pressure plates");
    }

    public static Slider placeRange = new Slider("Place Range", 0.0, 5.5, 10.0, 1);
    public static Slider placeDelay = new Slider("Place Delay", 0, 2, 20, 1);

    public static Checkbox renderPlacement = new Checkbox("Render Placement", true);
    public static ColorPicker colorPicker = new ColorPicker(renderPlacement, "Color Picker", new Color(0, 217, 255, 55));

    @Override
    public void setup() {
        addSetting(placeRange);
        addSetting(placeDelay);
        addSetting(renderPlacement);
    }
    
    Timer placeTimer = new Timer();
    BlockPos renderBlock;

    public void onUpdate() {
        if (nullCheck()) 
            return;

        EntityEnderCrystal entityEnderCrystal = (EntityEnderCrystal) mc.world.loadedEntityList.stream().filter(entity -> entity != null).filter(entity -> entity instanceof EntityEnderCrystal).filter(entity -> mc.player.getDistance(entity) <= placeRange.getValue()).filter(entity -> !mc.world.getBlockState(entity.getPosition()).getBlock().equals(Blocks.WOODEN_PRESSURE_PLATE)).min(Comparator.comparing(entity -> mc.player.getDistance(entity))).orElse(null);

        if (entityEnderCrystal != null) {
            InventoryUtil.switchToSlot(Blocks.WOODEN_PRESSURE_PLATE);

            if (placeTimer.passed((long) (placeDelay.getValue() * 100), Timer.Format.System)) {
                if (InventoryUtil.getHeldItem(Item.getItemFromBlock(Blocks.WOODEN_PRESSURE_PLATE))) {
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(entityEnderCrystal.getPosition(), EnumFacing.UP, EnumHand.MAIN_HAND, 0, 0, 0));
                    renderBlock = entityEnderCrystal.getPosition();
                }

                placeTimer.reset();
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (renderBlock != null && renderPlacement.getValue())
            RenderUtil.drawBoxBlockPos(renderBlock, -0.9, colorPicker.getColor(), RenderBuilder.RenderMode.Fill);
    }
}

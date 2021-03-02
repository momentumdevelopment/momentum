package me.linus.momentum.module.modules.player;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.render.builder.RenderBuilder.RenderMode;
import me.linus.momentum.util.world.BlockUtil;
import me.linus.momentum.util.world.BlockUtil.BlockResistance;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class SpeedMine extends Module {
    public SpeedMine() {
        super("SpeedMine", Category.PLAYER, "Allows you to mine faster");
    }

    public static Mode mode = new Mode("Mode", "Packet", "Damage", "Creative", "Instant", "Delay", "Vanilla", "Fake");
    public static Mode renderMode = new Mode("Render Mode",  "Claw", "Fill", "Outline", "Both");

    public static Checkbox color = new Checkbox("Color", true);
    public static ColorPicker colorPicker = new ColorPicker(color, "Color Picker", new Color(119, 0, 255, 255));

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(renderMode);
        addSetting(color);
    }

    List<BlockPos> renderBlocks = new ArrayList<>();

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        renderBlocks.removeIf(blockPos -> BlockUtil.getBlockResistance(blockPos) == BlockResistance.Blank);

        if ((BlockUtil.getBlockResistance(mc.objectMouseOver.getBlockPos()) == BlockResistance.Breakable || BlockUtil.getBlockResistance(mc.objectMouseOver.getBlockPos()) == BlockResistance.Resistant) && mc.playerController.isHittingBlock) {
            switch (mode.getValue()) {
                case 0:
                    renderBlocks.add(mc.objectMouseOver.getBlockPos());
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), mc.objectMouseOver.sideHit));
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), mc.objectMouseOver.sideHit));
                    break;
                case 1:
                    if (mc.playerController.curBlockDamageMP >= 0.5f)
                        mc.playerController.curBlockDamageMP = 1.0f;
                    break;
                case 2:
                    mc.playerController.curBlockDamageMP = 1.0f;
                    break;
                case 3:
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), mc.objectMouseOver.sideHit));
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), mc.objectMouseOver.sideHit));
                    mc.playerController.onPlayerDestroyBlock(mc.objectMouseOver.getBlockPos());
                    mc.world.setBlockToAir(mc.objectMouseOver.getBlockPos());
                    break;
                case 4:
                    mc.playerController.blockHitDelay = 0;
                    break;
                case 5:
                    mc.player.addPotionEffect(new PotionEffect(new PotionEffect(MobEffects.HASTE, 10000, 1, false, false)));
                    break;
                case 6:
                    mc.world.setBlockToAir(mc.objectMouseOver.getBlockPos());
                    break;
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (nullCheck())
            return;

        for (BlockPos renderBlock : renderBlocks) {
            switch (renderMode.getValue()) {
                case 0:
                    RenderUtil.drawBoxBlockPos(renderBlock, 0, colorPicker.getColor(), RenderMode.Claw);
                    break;
                case 1:
                    RenderUtil.drawBoxBlockPos(renderBlock, 0, new Color(colorPicker.getRed(), colorPicker.getGreen(), colorPicker.getBlue(), 144), RenderMode.Outline);
                    break;
                case 2:
                    RenderUtil.drawBoxBlockPos(renderBlock, 0, colorPicker.getColor(), RenderMode.Fill);
                    break;
                case 3:
                    RenderUtil.drawBoxBlockPos(renderBlock, 0, colorPicker.getColor(), RenderMode.Both);
                    break;
            }
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}

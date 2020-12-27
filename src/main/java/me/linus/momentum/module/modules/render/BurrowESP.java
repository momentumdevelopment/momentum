package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.friend.FriendManager;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.WorldUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class BurrowESP extends Module {
    public BurrowESP() {
        super("BurrowESP", Category.RENDER, "Highlights players that are burrowed");
    }

    public static Slider range = new Slider("Range", 0.0D, 7.0D, 10.0D, 0);

    public static Checkbox color = new Checkbox("Color", true);
    public static SubSlider r = new SubSlider(color, "Red", 0.0D, 255.0D, 255.0D, 0);
    public static SubSlider g = new SubSlider(color, "Green", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider b = new SubSlider(color, "Blue", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider a = new SubSlider(color, "Alpha", 0.0D, 30.0D, 255.0D, 0);

    @Override
    public void setup() {
        addSetting(range);
        addSetting(color);
    }

    private final List<BlockPos> burrowList = new ArrayList<>();

    @Override
    public void onUpdate() {
        burrowList.clear();

        WorldUtil.getNearbyPlayers(20).stream().forEach(entityPlayer -> {
            if (mc.world.getBlockState(new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ)).getBlock().equals(Blocks.OBSIDIAN))
                burrowList.add(new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ));
        });
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        for (BlockPos burrowPos : burrowList)
            RenderUtil.drawBoxBlockPos(burrowPos, new Color((int) r.getValue(), (int) g.getValue(),  (int) b.getValue(), (int) a.getValue()));
    }
}

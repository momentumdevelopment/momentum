package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.friend.FriendManager;
import me.linus.momentum.util.combat.EnemyUtil;
import me.linus.momentum.util.render.RenderUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips
 * @since 11/29/2020
 */

public class CityESP extends Module {
    public CityESP() {
        super("CityESP", Category.RENDER, "Highlights blocks where nearby players can be citied");
    }

    public static Checkbox color = new Checkbox("Color", true);
    public static SubSlider r = new SubSlider(color, "Red", 0.0D, 250.0D, 255.0D, 0);
    public static SubSlider g = new SubSlider(color, "Green", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider b = new SubSlider(color, "Blue", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider a = new SubSlider(color, "Alpha", 0.0D, 30.0D, 255.0D, 0);

    public static Slider range = new Slider("Range", 0.0D, 7.0D, 10.0D, 0);

    @Override
    public void setup() {
        addSetting(color);
        addSetting(range);
    }

    List<BlockPos> cityBlocks = new ArrayList<>();

    @Override
    public void onUpdate() {
        cityBlocks.clear();

        for (EntityPlayer enemy : mc.world.playerEntities) {
            if (mc.player.getDistance(enemy) > range.getValue() || mc.player == enemy)
                continue;

            if (FriendManager.isFriend(enemy.getName()) && FriendManager.isFriendModuleEnabled())
                continue;

            for (BlockPos blocks : EnemyUtil.getCityBlocks(enemy, false)) {
                cityBlocks.add(blocks);
            }
        }
    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent eventRender) {
        for (BlockPos cityPos : cityBlocks) {
            RenderUtil.drawVanillaBoxFromBlockPos(cityPos, (float) r.getValue() / 255f, (float) g.getValue() / 255f, (float) b.getValue() / 255f, (float) a.getValue() / 255f);
        }
    }
}

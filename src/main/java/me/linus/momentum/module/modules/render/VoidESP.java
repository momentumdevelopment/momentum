package me.linus.momentum.module.modules.render;

import me.linus.momentum.event.events.render.Render3DEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.BlockUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips
 * @since 11/29/2020
 */

public class VoidESP extends Module {
    public VoidESP() {
        super("VoidESP", Category.RENDER, "Highlights void holes");
    }

    public static Checkbox color = new Checkbox("Color", true);
    public static SubSlider r = new SubSlider(color, "Red", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider g = new SubSlider(color, "Green", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider b = new SubSlider(color, "Blue", 0.0D, 255.0D, 255.0D, 0);
    public static SubSlider a = new SubSlider(color, "Alpha", 0.0D, 30.0D, 255.0D, 0);

    public static Slider range = new Slider("Range", 0.0D, 7.0D, 10.0D, 0);

    @Override
    public void setup() {
        addSetting(color);
        addSetting(range);
    }

    public final List<BlockPos> voidBlocks = new ArrayList<>();

    @Override
    public void onUpdate() {
        if (mc.player == null) return;

        voidBlocks.clear();

        final Vec3i playerPos = new Vec3i(mc.player.posX, mc.player.posY, mc.player.posZ);

        for (int x = (int) (playerPos.getX() - range.getValue()); x < playerPos.getX() + range.getValue(); x++) {
            for (int z = (int) (playerPos.getZ() - range.getValue()); z < playerPos.getZ() + range.getValue(); z++) {
                for (int y = (int) (playerPos.getY() + range.getValue()); y > playerPos.getY() - range.getValue(); y--) {
                    final BlockPos blockPos = new BlockPos(x, y, z);

                    if (BlockUtils.isVoidHole(blockPos))
                        voidBlocks.add(blockPos);
                }
            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent eventRender) {
        for (BlockPos voidPos : voidBlocks) {
            RenderUtil.drawVanillaBoxFromBlockPos(voidPos, (float) r.getValue() / 255f, (float) g.getValue() / 255f, (float) b.getValue() / 255f, (float) a.getValue() / 255f);
        }
    }
}

package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.EntityUtil;
import me.linus.momentum.util.world.InventoryUtil;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 12/13/2020
 */

public class Web extends Module {
    public Web() {
        super("Web", Category.COMBAT, "Webs yourself");
    }

    public static Slider range = new Slider("Range", 0.0D, 7.0D, 10.0D, 0);
    private static final Checkbox rotate = new Checkbox("Rotate", true);
    private static final Checkbox disable = new Checkbox("Disables", true);

    public static Checkbox color = new Checkbox("Color", true);
    public static SubSlider r = new SubSlider(color, "Red", 0.0D, 255.0D, 255.0D, 0);
    public static SubSlider g = new SubSlider(color, "Green", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider b = new SubSlider(color, "Blue", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider a = new SubSlider(color, "Alpha", 0.0D, 30.0D, 255.0D, 0);

    @Override
    public void setup() {
        addSetting(range);
        addSetting(rotate);
        addSetting(disable);
        addSetting(color);
    }

    private boolean hasPlaced;
    BlockPos playerBlock;

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        hasPlaced = false;
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (hasPlaced && disable.getValue())
            this.disable();

        EntityPlayer closestPlayer = EntityUtil.getClosestPlayer(range.getValue());
        playerBlock = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

        if (closestPlayer != null && playerBlock != null) {
            mc.player.inventory.currentItem = InventoryUtil.getBlockInHotbar(Blocks.WEB);
            PlayerUtil.placeBlock(playerBlock, rotate.getValue());
            hasPlaced = true;
        }
    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent eventRender) {
        if (playerBlock != null)
            RenderUtil.drawVanillaBoxFromBlockPos(playerBlock, (float) r.getValue() / 255f, (float) g.getValue() / 255f, (float) b.getValue() / 255f, (float) a.getValue() / 255f);
    }
}

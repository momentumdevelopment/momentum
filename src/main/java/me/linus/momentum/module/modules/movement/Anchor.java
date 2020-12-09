package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.SubSlider;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class Anchor extends Module {
    public Anchor() {
        super("Anchor", Category.MOVEMENT, "Stops all movement above a hole");
    }

    private static Checkbox pull = new Checkbox("Pull", false);
    private static SubSlider speed = new SubSlider(pull, "Pull Speed", 0.0D, 3.0D, 10.0D, 1);

    @Override
    public void setup() {
        addSetting(pull);
    }

    BlockPos playerPos;

    @Override
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }

        double newX;
        double newZ;

        if (mc.player.posX > Math.round(mc.player.posX)) {
            newX = Math.round(mc.player.posX) + 0.5;
        } else if (mc.player.posX < Math.round(mc.player.posX)) {
            newX = Math.round(mc.player.posX) - 0.5;
        } else {
            newX = mc.player.posX;
        }

        if (mc.player.posZ > Math.round(mc.player.posZ)) {
            newZ = Math.round(mc.player.posZ) + 0.5;
        } else if (mc.player.posZ < Math.round(mc.player.posZ)) {
            newZ = Math.round(mc.player.posZ) - 0.5;
        } else {
            newZ = mc.player.posZ;
        }

        playerPos = new BlockPos(newX, mc.player.posY, newZ);

        if (mc.world.getBlockState(playerPos).getBlock() != Blocks.AIR) {
            return;
        }

        if (mc.world.getBlockState(playerPos.down()).getBlock() == Blocks.AIR
                && mc.world.getBlockState(playerPos.down().east()).getBlock() != Blocks.AIR
                && mc.world.getBlockState(playerPos.down().west()).getBlock() != Blocks.AIR
                && mc.world.getBlockState(playerPos.down().north()).getBlock() != Blocks.AIR
                && mc.world.getBlockState(playerPos.down().south()).getBlock() != Blocks.AIR
                && mc.world.getBlockState(playerPos.down(2)).getBlock() != Blocks.AIR) {

            mc.player.motionX = 0;
            mc.player.motionZ = 0;

            if (pull.getValue())
                mc.player.motionY -= speed.getValue();
        }

        else if (mc.world.getBlockState(playerPos.down()).getBlock() == Blocks.AIR
                && mc.world.getBlockState(playerPos.down(2)).getBlock() == Blocks.AIR
                && mc.world.getBlockState(playerPos.down(2).east()).getBlock() != Blocks.AIR
                && mc.world.getBlockState(playerPos.down(2).west()).getBlock() != Blocks.AIR
                && mc.world.getBlockState(playerPos.down(2).north()).getBlock() != Blocks.AIR
                && mc.world.getBlockState(playerPos.down(2).south()).getBlock() != Blocks.AIR
                && mc.world.getBlockState(playerPos.down(3)).getBlock() != Blocks.AIR) {

            mc.player.motionX = 0;
            mc.player.motionZ = 0;

            if (pull.getValue())
                mc.player.motionY -= speed.getValue();
        }
    }
}

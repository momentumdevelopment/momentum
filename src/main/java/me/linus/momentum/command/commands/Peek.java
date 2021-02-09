package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.MessageUtil;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityShulkerBox;

/**
 * @author linustouchtips
 * @since 12/22/2020
 */

// TODO: fix the rendering on screen
public class Peek extends Command implements MixinInterface {
    public Peek() {
        super("peek");
    }

    public static TileEntityShulkerBox shulkerBox;

    @Override
    public void onCommand(String[] args) {
        ItemStack shulker = mc.player.getHeldItemMainhand();
        if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox)) {
            MessageUtil.addOutput("You are not holding a shulker box!");
            return;
        }

        TileEntityShulkerBox entityShulkerBox = new TileEntityShulkerBox();
        entityShulkerBox.blockType = ((ItemShulkerBox) shulker.getItem()).getBlock();
        entityShulkerBox.setWorld(mc.world);
        entityShulkerBox.readFromNBT(shulker.getTagCompound().getCompoundTag("BlockEntityTag"));
        shulkerBox = entityShulkerBox;
    }

    @Override
    public String getDescription() {
        return "Allows you to preview shulker contents";
    }
}

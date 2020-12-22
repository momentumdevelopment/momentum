package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import me.linus.momentum.util.client.external.MessageUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityShulkerBox;

/**
 * @author linustouchtips
 * @since 12/22/2020
 */

// TODO: fix the rendering on screen
public class Peek extends Command {
    public Peek() {
        super("peek");
    }

    @Override
    public void onCommand(String[] args) {
        ItemStack shulker = mc.player.getHeldItemMainhand();
        if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox)) {
            MessageUtil.sendClientMessage("You are not holding a shulker box!");
            return;
        }

        BlockShulkerBox shulkerBox = (BlockShulkerBox) Block.getBlockFromItem(shulker.getItem());
        if (shulkerBox != null) {
            NBTTagCompound tag = shulker.getTagCompound();
            if (tag != null && tag.hasKey("BlockEntityTag", 10)) {
                NBTTagCompound entityTag = tag.getCompoundTag("BlockEntityTag");

                TileEntityShulkerBox entityShulkerBox = new TileEntityShulkerBox();
                entityShulkerBox.setWorld(mc.world);
                entityShulkerBox.readFromNBT(entityTag);
                mc.displayGuiScreen(new GuiShulkerBox(mc.player.inventory, entityShulkerBox));
            }
        }
    }

    @Override
    public String getDescription() {
        return "Allows you to preview shulker contents";
    }
}

package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

public class Peek extends Command implements MixinInterface {
    public Peek() {
        super("Peek", new String[] {"peek"});
    }

    @Override
    public void onCommand(String[] args) {

    }
}

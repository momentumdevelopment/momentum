package me.linus.momentum.command.commands;

import me.linus.momentum.Momentum;
import me.linus.momentum.command.Command;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.module.Module;
import me.linus.momentum.util.client.MessageUtil;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class Panic extends Command {
    public Panic () {
        super("panic",  "", "Toggles off everything enabled");
    }

    @Override
    public void onCommand(String[] args) {
        for (Module m: Momentum.moduleManager.getModules()) {
            if (m.isEnabled()) {
                m.disable();
                MessageUtil.addOutput(TextFormatting.LIGHT_PURPLE + "All modules toggled off!");
            }

            else
                MessageUtil.addOutput(TextFormatting.LIGHT_PURPLE + "No modules to disable!");
        }

        for (HUDComponent hud : Momentum.componentManager.getComponents()) {
            if (hud.isEnabled()) {
                hud.toggle();
                MessageUtil.addOutput(TextFormatting.LIGHT_PURPLE + "All HUD elements toggled off!");
            }

            else
                MessageUtil.addOutput(TextFormatting.LIGHT_PURPLE + "No HUD elements to disable!");
        }
    }
}

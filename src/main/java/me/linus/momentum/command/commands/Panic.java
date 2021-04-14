package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import me.linus.momentum.gui.hud.element.HUDElement;
import me.linus.momentum.managers.HUDElementManager;
import me.linus.momentum.managers.ModuleManager;
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
        for (Module m: ModuleManager.getModules()) {
            if (m.isEnabled())
                m.disable();
        }

        MessageUtil.addOutput("All modules toggled off!");

        for (HUDElement hud : HUDElementManager.getComponents()) {
            if (hud.isDrawn())
                hud.toggleElement();
        }

        MessageUtil.addOutput("All HUD elements toggled off!");
    }
}

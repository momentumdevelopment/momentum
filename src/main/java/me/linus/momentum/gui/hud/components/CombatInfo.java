package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.module.Module;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.client.HUDEditor;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/26/2020
 */

public class CombatInfo extends HUDComponent {
    public CombatInfo() {
        super("CombatInfo", 2, 70);
    }

    @Override
    public void renderComponent() {
        FontUtil.drawString("AC:" + getEnabled(ModuleManager.getModuleByName("AutoCrystal")), this.x, this.y, HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        FontUtil.drawString("AB:" + getEnabled(ModuleManager.getModuleByName("AutoBed")), this.x, this.y + 11, HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        FontUtil.drawString("KA:" + getEnabled(ModuleManager.getModuleByName("Aura")), this.x, this.y + 22, HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        FontUtil.drawString("AT:" + getEnabled(ModuleManager.getModuleByName("AutoTrap")), this.x, this.y + 33, HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        FontUtil.drawString("SP:" + getEnabled(ModuleManager.getModuleByName("Speed")), this.x, this.y + 44, HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);

        width = Momentum.fontManager.getCustomFont().getStringWidth("AC: OFF") + 2;
        height = 56;
    }

    public String getEnabled(Module module) {
        if (module.isEnabled())
            return TextFormatting.GREEN + " ON";
        else
            return TextFormatting.RED + " OFF";
    }
}

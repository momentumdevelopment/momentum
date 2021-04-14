package me.linus.momentum.gui.hud.element.elements;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.element.AnchorPoint;
import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.module.Module;
import me.linus.momentum.util.client.color.ThemeColor;
import me.linus.momentum.module.modules.client.HUD;
import me.linus.momentum.gui.hud.element.HUDElement;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/26/2020
 */

public class CombatInfo extends HUDElement {
    public CombatInfo() {
        super("CombatInfo", 2, 70, Category.COMBAT, AnchorPoint.None);
    }

    @Override
    public void renderElement() {
        FontUtil.drawString("AC:" + getEnabled(ModuleManager.getModuleByName("AutoCrystal")), this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        FontUtil.drawString("AB:" + getEnabled(ModuleManager.getModuleByName("AutoBed")), this.x, this.y + 11, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        FontUtil.drawString("KA:" + getEnabled(ModuleManager.getModuleByName("Aura")), this.x, this.y + 22, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        FontUtil.drawString("AT:" + getEnabled(ModuleManager.getModuleByName("AutoTrap")), this.x, this.y + 33, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        FontUtil.drawString("SP:" + getEnabled(ModuleManager.getModuleByName("Speed")), this.x, this.y + 44, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);

        width = Momentum.fontManager.getCustomFont().getStringWidth("AC: OFF") + 2;
        height = 70;
    }

    public String getEnabled(Module module) {
        if (module.isEnabled())
            return TextFormatting.GREEN + " ON";
        else
            return TextFormatting.RED + " OFF";
    }
}

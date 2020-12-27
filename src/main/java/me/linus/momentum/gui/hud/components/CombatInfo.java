package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.module.Module;
import me.linus.momentum.module.ModuleManager;
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
        FontUtil.drawStringWithShadow("AC:" + getEnabled(ModuleManager.getModuleByName("AutoCrystal")), this.x, this.y, -1);
        FontUtil.drawStringWithShadow("AB:" + getEnabled(ModuleManager.getModuleByName("AutoBed")), this.x, this.y + 11, -1);
        FontUtil.drawStringWithShadow("KA:" + getEnabled(ModuleManager.getModuleByName("Aura")), this.x, this.y + 22, -1);
        FontUtil.drawStringWithShadow("AT:" + getEnabled(ModuleManager.getModuleByName("AutoTrap")), this.x, this.y + 33, -1);
        FontUtil.drawStringWithShadow("SP:" + getEnabled(ModuleManager.getModuleByName("Speed")), this.x, this.y + 44, -1);

        width = (int) FontUtil.getStringWidth("AC: OFF") + 2;
        height = 56;
    }

    public String getEnabled(Module module) {
        if (module.isEnabled())
            return TextFormatting.GREEN + " ON";
        else
            return TextFormatting.RED + " OFF";
    }
}

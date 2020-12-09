package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.module.Module;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.client.ClickGui;
import me.linus.momentum.module.modules.hud.ArrayListModule;
import me.linus.momentum.util.client.Animation2D;
import me.linus.momentum.util.client.color.ColorUtil;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.Comparator;

public class ArrayList<N> extends HUDComponent<ArrayListModule> {
    public ArrayList() {
        super("ArrayList", 200, 2, ArrayListModule.INSTANCE);
    }

    private int count;

    @Override
    public void render() {
        count = 0;
        ModuleManager.getModules()
            .stream()
                .filter(Module::isEnabled)
                .sorted(Comparator.comparing(module -> FontUtil.getStringWidth(module.getName() + " " + module.getHUDData()) * (-1)))
                .forEach(module -> {
                    float modWidth = FontUtil.getStringWidth(module.getName() + "" + TextFormatting.RESET + module.getHUDData());

                    if (module.remainingAnimation < modWidth && module.isEnabled()) {
                        module.remainingAnimation = Animation2D.moveTowards(module.remainingAnimation, modWidth + 1, (float) (0.01f + ClickGui.speed.getValue() / 30), 0.1f);
                    }
                    
                    else if (module.remainingAnimation > 1.5f && !module.isEnabled()) {
                        module.remainingAnimation = Animation2D.moveTowards(module.remainingAnimation, -1.5f, (float) (0.01f + ClickGui.speed.getValue() / 30), 0.1f);
                    }
                    
                    else if (module.remainingAnimation <= 1.5f && !module.isEnabled()) {
                        module.remainingAnimation = -1f;
                    }
                    
                    if (module.remainingAnimation > modWidth && module.isEnabled()) {
                        module.remainingAnimation = modWidth;
                    }

                    GuiScreen.drawRect((int) (Momentum.componentManager.getComponentByName("ArrayList").getX() - module.remainingAnimation - 14), Momentum.componentManager.getComponentByName("ArrayList").getY() + 1 + ((mc.fontRenderer.FONT_HEIGHT + 1) * count), (int) (Momentum.componentManager.getComponentByName("ArrayList").getX() - 17 - module.remainingAnimation + modWidth), Momentum.componentManager.getComponentByName("ArrayList").getY() + 11 + ((mc.fontRenderer.FONT_HEIGHT + 1) * count), new Color(0, 0,0 , 80).getRGB());
                    FontUtil.drawStringWithShadow(module.getName(), (float) (Momentum.componentManager.getComponentByName("ArrayList").getX() - module.remainingAnimation - 12.6), Momentum.componentManager.getComponentByName("ArrayList").getY() + ((mc.fontRenderer.FONT_HEIGHT + 1) * count), ColorUtil.getColorByCategory(module));
                    FontUtil.drawStringWithShadow(" " + module.getHUDData(), (float) (Momentum.componentManager.getComponentByName("ArrayList").getX() - module.remainingAnimation - 15.0 + FontUtil.getStringWidth(module.getName() + " ")), Momentum.componentManager.getComponentByName("ArrayList").getY() + ((mc.fontRenderer.FONT_HEIGHT + 1) * count), new Color(255, 255, 255).getRGB());
                    count++;
                });

        width = -75;
        height = ((mc.fontRenderer.FONT_HEIGHT + 1) * count);
    }
}

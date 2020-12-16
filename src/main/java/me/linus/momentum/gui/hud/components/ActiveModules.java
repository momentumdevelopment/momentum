package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.module.Module;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.client.ClickGui;
import me.linus.momentum.module.modules.client.Colors;
import me.linus.momentum.module.modules.hud.ArrayListModule;
import me.linus.momentum.util.client.Animation2D;
import me.linus.momentum.util.client.color.ColorUtil;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.Comparator;

public class ActiveModules extends HUDComponent {
    public ActiveModules() {
        super("ActiveModules", 200, 2, ArrayListModule.INSTANCE);
    }

    private int count;

    @Override
    public void render() {
        count = 0;
        int screenWidth = new ScaledResolution(mc).getScaledWidth();
        ModuleManager.getModules()
                .stream()
                .filter(Module::isEnabled)
                .sorted(Comparator.comparing(module -> FontUtil.getStringWidth(module.getName() + " " + module.getHUDData()) * (-1)))
                .forEach(module -> {
                    int screenWidthScaled = new ScaledResolution(mc).getScaledWidth();
                    float modWidth = FontUtil.getStringWidth(module.getName() + TextFormatting.WHITE + module.getHUDData());
                    String modText = module.getName() + TextFormatting.WHITE + module.getHUDData();

                    if (module.remainingAnimation < modWidth && module.isEnabled())
                        module.remainingAnimation = Animation2D.moveTowards(module.remainingAnimation, modWidth + 1, (float) (0.01f + ClickGui.speed.getValue() / 30), 0.1f);

                    else if (module.remainingAnimation > 1.5f && !module.isEnabled())
                        module.remainingAnimation = Animation2D.moveTowards(module.remainingAnimation, -1.5f, (float) (0.01f + ClickGui.speed.getValue() / 30), 0.1f);

                    else if (module.remainingAnimation <= 1.5f && !module.isEnabled())
                        module.remainingAnimation = -1f;

                    if (module.remainingAnimation > modWidth && module.isEnabled())
                        module.remainingAnimation = modWidth;

                    if (Momentum.componentManager.getComponentByName("ActiveModules").getX() < (screenWidthScaled / 2))
                        FontUtil.drawStringWithShadow(modText, Momentum.componentManager.getComponentByName("ActiveModules").getX() - 2 - modWidth + module.remainingAnimation, Momentum.componentManager.getComponentByName("ActiveModules").getY() + (10 * count), new Color((int) Colors.r.getValue(), (int) Colors.g.getValue(), (int) Colors.b.getValue(), 255).getRGB());
                    else
                        FontUtil.drawStringWithShadow(modText, Momentum.componentManager.getComponentByName("ActiveModules").getX() - 2 - modWidth - module.remainingAnimation, Momentum.componentManager.getComponentByName("ActiveModules").getY() + (10 * count), new Color((int) Colors.r.getValue(), (int) Colors.g.getValue(), (int) Colors.b.getValue(), 255).getRGB());

                    count++;
                });

        if (Momentum.componentManager.getComponentByName("ActiveModules").getX() < (screenWidth / 2))
            width = 75;
        else
            width = -75;

        height = ((mc.fontRenderer.FONT_HEIGHT + 1) * count);
    }
}

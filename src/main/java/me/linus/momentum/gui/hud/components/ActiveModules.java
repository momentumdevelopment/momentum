package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.hud.HUDComponentManager;
import me.linus.momentum.module.Module;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.module.modules.client.Colors;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.render.AnimationUtil;
import me.linus.momentum.util.client.color.ColorUtil;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.Comparator;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class ActiveModules extends HUDComponent {
    public ActiveModules() {
        super("ActiveModules", 200, 2);
    }

    private static final Mode mode = new Mode("Mode", "AlphaStep", "Rainbow", "Category", "Static");
    private static final Checkbox background = new Checkbox("Background", false);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(background);
    }

    private int count;

    @Override
    public void renderComponent() {
        count = 0;
        int screenWidth = new ScaledResolution(mc).getScaledWidth();

        ModuleManager.getModules().stream().filter(Module::isEnabled).sorted(Comparator.comparing(module -> FontUtil.getStringWidth(module.getName() + " " + module.getHUDData()) * (-1))).forEach(module -> {
            int screenWidthScaled = new ScaledResolution(mc).getScaledWidth();
            float modWidth = FontUtil.getStringWidth(module.getName() + TextFormatting.WHITE + module.getHUDData());
            String modText = module.getName() + TextFormatting.WHITE + module.getHUDData();

            if (module.remainingAnimation < modWidth && module.isEnabled())
                module.remainingAnimation = AnimationUtil.moveTowards(module.remainingAnimation, modWidth + 1, (float) (0.01f + ClickGUI.speed.getValue() / 30), 0.1f);

            else if (module.remainingAnimation > 1.5f && !module.isEnabled())
                module.remainingAnimation = AnimationUtil.moveTowards(module.remainingAnimation, -1.5f, (float) (0.01f + ClickGUI.speed.getValue() / 30), 0.1f);

            else if (module.remainingAnimation <= 1.5f && !module.isEnabled())
                module.remainingAnimation = -1f;

            if (module.remainingAnimation > modWidth && module.isEnabled())
                module.remainingAnimation = modWidth;

            if (HUDComponentManager.getComponentByName("ActiveModules").getX() < (screenWidthScaled / 2)) {
                if (background.getValue())
                    GuiScreen.drawRect((int) (HUDComponentManager.getComponentByName("ActiveModules").getX() - 1 - modWidth + module.remainingAnimation), HUDComponentManager.getComponentByName("ActiveModules").getY() + (10 * count), (int) (HUDComponentManager.getComponentByName("ActiveModules").getX() - 2 + module.remainingAnimation), HUDComponentManager.getComponentByName("ActiveModules").getY() + (10 * count) + 10, new Color(0, 0, 0, 70).getRGB());

                FontUtil.drawStringWithShadow(modText, HUDComponentManager.getComponentByName("ActiveModules").getX() - 2 - modWidth + module.remainingAnimation, HUDComponentManager.getComponentByName("ActiveModules").getY() + (10 * count), generateColor(module));
            }

            else {
                if (background.getValue())
                    GuiScreen.drawRect((int) (HUDComponentManager.getComponentByName("ActiveModules").getX() - module.remainingAnimation - 2), HUDComponentManager.getComponentByName("ActiveModules").getY() + (10 * count), (int) (HUDComponentManager.getComponentByName("ActiveModules").getX() - module.remainingAnimation + modWidth), HUDComponentManager.getComponentByName("ActiveModules").getY() + (10 * count) + 10, new Color(0, 0, 0, 70).getRGB());

                FontUtil.drawStringWithShadow(modText, HUDComponentManager.getComponentByName("ActiveModules").getX() - module.remainingAnimation, HUDComponentManager.getComponentByName("ActiveModules").getY() + (10 * count), generateColor(module));
            }

            count++;
        });

        if (HUDComponentManager.getComponentByName("ActiveModules").getX() < (screenWidth / 2))
            width = 75;

        else
            width = -75;

        height = ((mc.fontRenderer.FONT_HEIGHT + 1) * count);
    }

    public int generateColor(Module mod) {
        switch (mode.getValue()) {
            case 0:
                return ColorUtil.alphaStep(new Color((int) Colors.r.getValue(), (int) Colors.g.getValue(), (int) Colors.b.getValue()), 50, (count * 2) + 10).getRGB();
            case 1:
                return ColorUtil.rainbow(1);
            case 2:
                return ColorUtil.getColorByCategory(mod);
            case 3:
                return new Color((int) Colors.r.getValue(), (int) Colors.g.getValue(), (int) Colors.b.getValue()).getRGB();
        }

        return -1;
    }

    @Override
    public boolean getBackground() {
        return false;
    }
}

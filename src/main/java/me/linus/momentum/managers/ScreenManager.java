package me.linus.momentum.managers;

import me.linus.momentum.gui.main.GUIScreen;
import me.linus.momentum.gui.main.HUDScreen;
import me.linus.momentum.gui.main.WindowScreen;
import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

/**
 * @author linustouchtips
 * @since 03/12/2021
 */

public class ScreenManager implements MixinInterface {

    public static Screen screen = Screen.Click;

    public static void setScreen(GuiScreen guiScreen) {
        mc.displayGuiScreen(guiScreen);

        if (guiScreen instanceof GUIScreen)
            screen = Screen.Click;
        else if (guiScreen instanceof HUDScreen)
            screen = Screen.Hud;
        else if (guiScreen instanceof WindowScreen)
            screen = Screen.Console;

        mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
    }

    public enum Screen {
        Click,
        Hud,
        Console
    }
}

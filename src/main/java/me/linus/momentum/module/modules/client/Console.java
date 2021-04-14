package me.linus.momentum.module.modules.client;

import me.linus.momentum.gui.main.WindowScreen;
import me.linus.momentum.managers.ScreenManager;
import me.linus.momentum.module.Module;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

/**
 * @author linustouchtips
 * @since 02/07/2021
 */

public class Console extends Module {
    public Console() {
        super("Console", Category.CLIENT, "Opens the console window");
        this.getKeybind().setKeyCode(Keyboard.KEY_SEMICOLON);
    }

    WindowScreen windowScreen = new WindowScreen();

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        super.onEnable();
        ScreenManager.setScreen(windowScreen);
    }

    @Override
    public void onUpdate() {
        if (nullCheck()) {
            this.disable();
        }
    }
}

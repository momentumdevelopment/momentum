package me.linus.momentum.module.modules.client;

import me.linus.momentum.module.Module;
import net.minecraft.util.ResourceLocation;
import me.linus.momentum.gui.main.console.Console;
import org.lwjgl.input.Keyboard;

/**
 * @author linustouchtips
 * @since 02/07/2021
 */

public class ConsoleScreen extends Module {
    public ConsoleScreen() {
        super("Console", Category.CLIENT, "Opens the console window");
        this.getKeybind().setKeyCode(Keyboard.KEY_SLASH);
    }

     Console console = new Console();

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        super.onEnable();
        mc.displayGuiScreen(console);

        mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
    }
}

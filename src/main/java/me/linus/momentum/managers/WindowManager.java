package me.linus.momentum.managers;

import com.google.common.collect.Lists;
import me.linus.momentum.gui.window.Window;
import me.linus.momentum.gui.window.windows.ConsoleWindow;
import me.linus.momentum.gui.window.windows.SocialWindow;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

public class WindowManager {
    public WindowManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    static List<Window> windows = Lists.newArrayList(
            new ConsoleWindow(),
            new SocialWindow()
    );

    public static List<Window> getWindows() {
        return windows;
    }
}

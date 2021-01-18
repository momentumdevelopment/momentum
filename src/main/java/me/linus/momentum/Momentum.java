package me.linus.momentum;

import me.linus.momentum.command.CommandManager;
import me.linus.momentum.gui.hud.HUDComponentManager;
import me.linus.momentum.gui.main.gui.Window;
import me.linus.momentum.gui.theme.Theme;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.util.client.CapeAPI;
import me.linus.momentum.util.client.friend.FriendManager;
import me.linus.momentum.util.config.ConfigManager;
import me.linus.momentum.util.config.ShutdownHook;
import me.linus.momentum.util.render.FontUtil;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

/**
 * @author bon & linustouchtips
 * @since 11/12/20
 */

@Mod (
	modid = Momentum.MODID,
	name = Momentum.NAME,
	version = Momentum.VERSION,
	acceptedMinecraftVersions = "[1.12.2]"
)

public class Momentum {
	
    public static final String MODID = "momentum";
    public static final String NAME = "Momentum";
    public static final String VERSION = "1.1.8";
    public static String PREFIX = "!";
    public static final Logger LOGGER;
    
    public static ModuleManager moduleManager;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static HUDComponentManager componentManager;
    public static FontUtil fontManager = new FontUtil();
    public static CapeAPI capeAPI;

    @Mod.Instance
    private static Momentum INSTANCE;

    public Momentum() {
    	INSTANCE = this;
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        fontManager.load();
        LOGGER.info("Fonts Loaded!");
        moduleManager = new ModuleManager();
        LOGGER.info("Modules Initialized!");
        MinecraftForge.EVENT_BUS.register(moduleManager);
    	LOGGER.info("Mod Initialized!");
    	Window.initGui();
    	LOGGER.info("ClickGui Initialized!");
    	Theme.initThemes();
    	LOGGER.info("GUI Themes Initialized!");
        friendManager = new FriendManager();
        LOGGER.info("Friends System Initialized!");
        commandManager = new CommandManager();
        LOGGER.info("Commands Initialized!");
        componentManager = new HUDComponentManager();
        LOGGER.info("HUD System Initialized!");
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
        LOGGER.info("Config System Saved!");
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        capeAPI = new CapeAPI();
        LOGGER.info("Cape API Initialized!");
        ConfigManager.loadConfig();
        LOGGER.info("Config System Loaded!");
        Display.setTitle(NAME + " Utility Mod " + VERSION);
        LOGGER.info("Changed Display Name!");
    }
    
    static {
    	LOGGER = LogManager.getLogger("Momentum");
    }
}
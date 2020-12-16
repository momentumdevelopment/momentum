package me.linus.momentum;

import me.linus.momentum.command.CommandManager;
import me.linus.momentum.gui.hud.HUDComponentManager;
import me.linus.momentum.gui.main.Window;
import me.linus.momentum.util.client.CapeAPI;
import me.linus.momentum.util.client.CustomFont;
import me.linus.momentum.util.client.friend.FriendManager;
import me.linus.momentum.util.config.ConfigManager;
import me.linus.momentum.util.config.ShutdownHook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import me.linus.momentum.gui.theme.Theme;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.ModuleManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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

public class Momentum implements MixinInterface {
	
    public static final String MODID = "momentum";
    public static final String NAME = "Momentum";
    public static final String VERSION = "1.1.2";
    public static String PREFIX = "!";
    public static final Logger LOGGER;

    public static CustomFont verdanaFont = new CustomFont("Verdana", 18.0f);
    public static CustomFont latoFont = new CustomFont("Lato", 18.0f);
    public static CustomFont comicFont = new CustomFont("comic-sans", 18.0f);
    public static CustomFont comfortaaFont = new CustomFont("comfortaa", 18.0f);
    public static CustomFont ubuntuFont = new CustomFont("Ubuntu", 18.0f);
    
    public static ModuleManager moduleManager;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static HUDComponentManager componentManager;
    public static CapeAPI capeAPI;

    @Mod.Instance
    private static Momentum INSTANCE;

    public Momentum() {
    	INSTANCE = this;
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	Display.setTitle(NAME + " Utility Mod " + VERSION);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        moduleManager = new ModuleManager();
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
    }
    
    static {
    	LOGGER = LogManager.getLogger("Momentum");
    }
}

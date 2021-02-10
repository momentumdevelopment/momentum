package me.linus.momentum;

import me.linus.momentum.managers.*;
import me.linus.momentum.gui.main.console.ConsoleWindow;
import me.linus.momentum.gui.main.gui.Window;
import me.linus.momentum.gui.theme.Theme;
import me.linus.momentum.managers.config.ConfigManagerJSON;
import me.linus.momentum.managers.social.enemy.EnemyManager;
import me.linus.momentum.managers.social.friend.FriendManager;
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
	name = Momentum.CLIENTNAME,
	version = Momentum.VERSION,
	acceptedMinecraftVersions = "[1.12.2]"
)

public class Momentum {
	
    public static final String MODID = "momentum";
    public static final String CLIENTNAME = "Momentum";
    public static final String VERSION = "1.2.0";
    public static String NAME = "Momentum";
    public static String PREFIX = "!";
    public static final Logger LOGGER;
    
    public static ModuleManager moduleManager;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static EnemyManager enemyManager;
    public static HUDComponentManager componentManager;
    public static RotationManager rotationManager;
    public static CrystalManager crystalManager;
    public static TickManager tickManager;
    public static FontUtil fontManager;
    public static CapeManager capeManager;
    public static ReloadManager reloadManager;

    @Mod.Instance
    private static Momentum INSTANCE;

    public Momentum() {
    	INSTANCE = this;
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        fontManager = new FontUtil();
        fontManager.load();
        LOGGER.info("Fonts Loaded!");

        moduleManager = new ModuleManager();
        LOGGER.info("Modules Initialized!");

        ConfigManagerJSON.loadConfig();
        LOGGER.info("Config System Loaded!");

    	rotationManager = new RotationManager();
        LOGGER.info("Client Rotations Initialized!");

        crystalManager = new CrystalManager();
        LOGGER.info("AutoCrystal Manager Initialized!");

    	Window.initGui();
    	LOGGER.info("ClickGUI Windows Initialized!");

        ConsoleWindow.initConsole();
        LOGGER.info("Console Windows Initialized!");

    	Theme.initThemes();
    	LOGGER.info("GUI Themes Initialized!");

        componentManager = new HUDComponentManager();
        LOGGER.info("HUD System Initialized!");

        friendManager = new FriendManager();
        LOGGER.info("Friends System Initialized!");

        enemyManager = new EnemyManager();
        LOGGER.info("Enemy System Initialized!");

        commandManager = new CommandManager();
        LOGGER.info("Commands Initialized!");

        tickManager = new TickManager();
        LOGGER.info("Tick System Initialized!");

        reloadManager = new ReloadManager();
        LOGGER.info("Reload System Initialized!");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ConfigManagerJSON.saveConfig();
            Momentum.LOGGER.info("Saving Config!");
        }));

        LOGGER.info("Config System Saved!");
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        capeManager = new CapeManager();
        LOGGER.info("Cape System Initialized!");

        Display.setTitle(NAME + " Utility Mod " + VERSION);
        LOGGER.info("Changed Display Name!");
    }
    
    static {
    	LOGGER = LogManager.getLogger("Momentum");
    }
}
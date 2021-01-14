package me.linus.momentum.module;

import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.Module.Category;
import me.linus.momentum.module.modules.bot.Milo;
import me.linus.momentum.module.modules.client.*;
import me.linus.momentum.module.modules.combat.*;
import me.linus.momentum.module.modules.misc.*;
import me.linus.momentum.module.modules.movement.*;
import me.linus.momentum.module.modules.player.*;
import me.linus.momentum.module.modules.render.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author bon & linustouchtips
 * @since 11/13/20
 */

public class ModuleManager implements MixinInterface {

	static final List<Module> modules = Arrays.asList(
			// client
			new ClickGUI(),
			new Colors(),
			new HUDEditor(),
			new Baritone(),
			new ClientFont(),
			new Friends(),
			new Capes(),

			// combat
			new AimBot(),
			new AntiCrystal(),
			new Aura(),
			new AutoArmor(),
			new AutoBed(),
			new AutoCity(),
			new AutoCrystal(),
			new AutoTotem(),
			new AutoTrap(),
			new Burrow(),
			new Criticals(),
			new HoleFill(),
			new Offhand(),
			new QuickBow(),
			new QuickEXP(),
			new Quiver(),
			new SelfTrap(),
			new Surround(),
			new Trigger(),
			new Web(),

			// player
			new AntiHunger(),
			new AutoMine(),
			new Blink(),
			new ExtraSlots(),
			new FastPlace(),
			new HandProgress(),
			new LiquidInteract(),
			new NoEntityTrace(),
			new NoFall(),
			new NoRotate(),
			new PacketEat(),
			new PortalGodMode(),
			new Reach(),
			new Rubberband(),
			new SpeedMine(),
			new Swing(),

			// misc
			new AntiAFK(),
			new AntiLag(),
			new AntiPacketKick(),
			new AutoDisconnect(),
			new AutoFish(),
			new BuildHeight(),
			new ChatLogger(),
			new ChatSuffix(),
			new ColoredText(),
			new DiscordRPC(),
			new EnableMessage(),
			new FakeGameMode(),
			new FakePlayer(),
			new MiddleClickFriend(),
			new NoHandShake(),
			new Notifier(),
			new Portal(),
			new Refill(),
			new SkinBlinker(),
			new StashFinder(),
			new Timer(),

			// movement
			new AirJump(),
			new Anchor(),
			new AntiLevitation(),
			new AntiVoid(),
			new AutoJump(),
			new AutoWalk(),
			new BoatFlight(),
			new ElytraFlight(),
			new Flight(),
			new HighJump(),
			new IceSpeed(),
			new Jesus(),
			new LongJump(),
			new NoSlow(),
			new Parkour(),
			new ReverseStep(),
			new Rotation(),
			new SafeWalk(),
			new Scaffold(),
			new Speed(),
			new Sprint(),
			new Step(),
			new Velocity(),
			new WebTeleport(),

			// render
			new BlockHighlight(),
			new BreakESP(),
			new BurrowESP(),
			new CityESP(),
			new CrossHairs(),
			new CustomFOV(),
			new ESP(),
			new FullBright(),
			new HoleESP(),
			new ItemPreview(),
			new NameTags(),
			new NoBob(),
			new NoRender(),
			new Skeleton(),
			new SkyColor(),
		 	new StorageESP(),
		 	new Tracers(),
			new Trajectories(),
			new VoidESP(),
			new Weather(),

			// bot
			new Milo()
		);
	
	public static List<Module> getModules(){
		return new ArrayList<>(modules);
	}
	
	public static List<Module> getModulesInCategory(Category cat){
		List<Module> module = new ArrayList<>();

		for (Module m : modules) {
			if (m.getCategory().equals(cat))
				module.add(m);
		}

		return module;
	}
	
	public static Module getModuleByName(String name) {
		return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}
	
	public static Module getModuleByClass(Class<?> clazz) {
		return modules.stream().filter(module -> module.getClass().equals(clazz)).findFirst().orElse(null);
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		ModuleManager.onUpdate();
		ModuleManager.keyListen();
		ThemeColor.updateColors();
	}

	@SubscribeEvent
	public void onFastTick(TickEvent event) {
		ModuleManager.onFastUpdate();
	}

	// TODO: add null checks to every override so the try catch in this is unnecessary
	public static void onUpdate() {
		for (Module m : modules) {
			try {
				if (m.isEnabled())
					m.onUpdate();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}
	
	public static void onFastUpdate() {
		for (Module m : modules) {
			try {
				if (m.isEnabled())
					m.onFastUpdate();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}
	
	public static void keyListen() {
		if (mc.currentScreen == null) {
			for (Module m : modules) {
				try {
					if (Keyboard.isKeyDown(Keyboard.KEY_NONE) || Keyboard.isKeyDown(Keyboard.CHAR_NONE))
						return;

					if (Keyboard.isKeyDown(m.getKeybind().getKeyCode()) && !m.isKeyDown) {
						m.isKeyDown = true;
						m.toggle();
					}

					else if (!Keyboard.isKeyDown(m.getKeybind().getKeyCode()))
						m.isKeyDown = false;

				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
	}
}

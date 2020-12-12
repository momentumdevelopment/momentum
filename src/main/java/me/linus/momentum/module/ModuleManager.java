package me.linus.momentum.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.linus.momentum.event.events.render.Render3DEvent;
import me.linus.momentum.gui.theme.Color;
import me.linus.momentum.module.modules.bot.Milo;
import me.linus.momentum.module.modules.combat.*;
import me.linus.momentum.module.modules.misc.*;
import me.linus.momentum.module.modules.player.*;
import me.linus.momentum.module.modules.render.*;
import me.linus.momentum.module.modules.client.*;
import me.linus.momentum.module.modules.movement.*;
import me.linus.momentum.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.input.Keyboard;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.Module.Category;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * @author bon & linustouchtips
 * @since 11/13/20
 */

public class ModuleManager implements MixinInterface {

	private static final List<Module> modules = Arrays.asList
		(
			//Client
			new ClickGui(),
			new Colors(),
			new HudEditor(),
			new Baritone(),
			new Friends(),
			new Capes(),

			//Combat
			new AimBot(),
			new Aura(),
			new AutoArmor(),
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

			//Player
			new AntiHunger(),
			new AutoMine(),
			new Blink(),
			new EntityMine(),
			new ExtraSlots(),
			new FastPlace(),
			new FreeCam(),
			new HandProgress(),
			new LiquidInteract(),
			new NoFall(),
			new NoRotate(),
			new PacketEat(),
			new PortalGodMode(),
			new QuickMine(),
			new Reach(),
			new Swing(),
			new SwingPrevent(),

			//Misc
			new AntiAFK(),
			new AntiLag(),
			new AntiPacketKick(),
			new AutoDisconnect(),
			new AutoFish(),
			new BuildHeight(),
			new ChatSuffix(),
			new ColoredText(),
			new DiscordRPC(),
			new EnableMessage(),
			new EntityAlert(),
			new FakeCreative(),
			new FakePlayer(),
			new MiddleClickFriend(),
			new NoHandShake(),
			new Notifier(),
			new Portal(),
			new Refill(),
			new SkinBlinker(),
			new StashFinder(),
			new Timer(),

			//Movement
			new Anchor(),
			new AntiLevitation(),
			new AntiVoid(),
			new AutoJump(),
			new AutoWalk(),
			new BoatFlight(),
			new Boost(),
			new ElytraFlight(),
			new Flight(),
			new HighJump(),
			new IceSpeed(),
			new LongJump(),
			new NoSlow(),
			new PacketFlight(),
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

			//Render
			new BreakESP(),
			new BurrowESP(),
			new Chams(),
			new CityESP(),
			new CrossHairs(),
			new CustomFOV(),
			new ESP(),
			new FullBright(),
			new HoleESP(),
			new NoBob(),
			new NoRender(),
			new NoWeather(),
			new Skeleton(),
			new SkyColor(),
		 	new Tracers(),
			new Trajectories(),
			new ViewModel(),
			new VoidESP(),

			//Bot
			new Milo()
		);
	
	public static List<Module> getModules(){
		return new ArrayList<>(modules);
	}
	
	public static List<Module> getModulesInCategory(Category cat){
		List<Module> module = new ArrayList<>();
		for(Module m : modules) {
			if(m.getCategory().equals(cat)) {
				module.add(m);
			}
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
		Color.updateColors();
	}
	
	/**
	 * this method runs extremely fast
	 * @see Module
	 **/

	@SubscribeEvent
	public void onFastTick(TickEvent event) {
		ModuleManager.onFastUpdate();
	}
	
	public static void onUpdate() {
		for(Module m : modules) {
			try {
				if (m.isEnabled()) {
					m.onUpdate();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void onFastUpdate() {
		for(Module m : modules) {
			try {
				if (m.isEnabled()) {
					m.onFastUpdate();
				}
			} catch(Exception e) {
				e.printStackTrace();
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
					} else if (!Keyboard.isKeyDown(m.getKeybind().getKeyCode())) {
						m.isKeyDown = false;
					}
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
		}
	}

	@SubscribeEvent
	public static void onRender3D(RenderWorldLastEvent event) {
		HudEditor.boost = 0;
		Minecraft.getMinecraft().mcProfiler.startSection("velocity");
		Minecraft.getMinecraft().mcProfiler.startSection("setup");
		RenderUtil.prepareProfiler();
		Render3DEvent e = new Render3DEvent(event.getPartialTicks());
		Minecraft.getMinecraft().mcProfiler.endSection();

		modules.stream().filter(module -> module.isEnabled()).forEach(module -> {
			Minecraft.getMinecraft().mcProfiler.startSection(module.getName());
			module.onRender3D(e);
			Minecraft.getMinecraft().mcProfiler.endSection();
		});

		Minecraft.getMinecraft().mcProfiler.startSection("release");
		RenderUtil.releaseRender();
		Minecraft.getMinecraft().mcProfiler.endSection();
		Minecraft.getMinecraft().mcProfiler.endSection();
	}
}

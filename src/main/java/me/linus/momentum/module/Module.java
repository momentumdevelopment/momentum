package me.linus.momentum.module;

import java.util.ArrayList;
import java.util.List;

import me.linus.momentum.Momentum;
import me.linus.momentum.event.events.render.Render3DEvent;
import me.linus.momentum.module.modules.misc.EnableMessage;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import com.mojang.realmsclient.gui.ChatFormatting;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.setting.Setting;
import me.linus.momentum.util.client.MessageUtil;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import javax.annotation.Nullable;

/**
 * @author bon
 * @since 11/13/20
 */

public abstract class Module implements MixinInterface {

	private final String name;
	private final Category category;
	private final String description;
	private KeyBinding key;
	
	private boolean enabled;
	private boolean opened;
	protected boolean isKeyDown = false;
	private boolean isBinding;
	public float remainingAnimation = 0.0f;
	
	public List<Setting> settingsList = new ArrayList<>();
	
	public Module(String name, Category category, @Nullable String description) {
		this.name = name;
		this.category = category;
		this.description = description;
		this.enabled = false;
		this.opened = false;
		
		this.key = new KeyBinding(name, Keyboard.KEY_NONE, Momentum.NAME);
		ClientRegistry.registerKeyBinding(this.key);
		
		this.setup();
	}
	
	public void setup() {
		
	}

	public void addSetting(Setting s) {
		settingsList.add(s);
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public boolean isKeyDown() {
		return this.isKeyDown;
	}
	
	public void setKeyDown(boolean b) {
		this.isKeyDown = b;
	}

	public void onEnable() {
		remainingAnimation = 0.0f;
		if (ModuleManager.getModuleByClass(EnableMessage.class).isEnabled() && !this.name.equalsIgnoreCase("clickgui")) {
			MessageUtil.sendClientMessage(this.name + ChatFormatting.GREEN + " ENABLED");
		}

		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void onDisable() {
		remainingAnimation = 0.0f;
		if (ModuleManager.getModuleByClass(EnableMessage.class).isEnabled() && !this.name.equalsIgnoreCase("clickgui")) {
			MessageUtil.sendClientMessage(this.name + ChatFormatting.RED + " DISABLED");
		}

		MinecraftForge.EVENT_BUS.unregister(this);
	}

	public void onToggle() {
		remainingAnimation = 0.0f;
	}
	
	public void onUpdate() {
		
	}

	public void onFastUpdate() {
		
	}

	public void onRender3D(Render3DEvent eventRender) {

	}
	
	public void toggle() {
		onToggle();
		this.enabled = !this.enabled;
		try {
			if (this.isEnabled()) {
				this.onEnable();
			} else {
				this.onDisable();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void enable() {
		if (!this.isEnabled()) {
			this.enabled = true;
			try {
				this.onEnable();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void disable() {
		if (this.isEnabled()) {
			this.enabled = false;
			try {
				this.onDisable();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SubscribeEvent
	public void onWorldRender(RenderWorldLastEvent event) {
		if (event.isCanceled())
			return;

		ModuleManager.onRender3D(event);
	}

	public boolean nullCheck() {
		return (mc.player == null || mc.world == null);
	}
	
	public String getName() {
		return this.name;
	}
	
	public Category getCategory() {
		return this.category;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public KeyBinding getKeybind() {
		return this.key;
	}

	public String getHUDData() {
		return "";
	}
	
	public boolean hasSettings() {
		return this.settingsList.size() > 0;
	}
	
	public List<Setting> getSettings(){
		return this.settingsList;
	}
	
	public void toggleState() {
		this.opened = !this.opened;
	}
	
	public boolean isOpened() {
		return this.opened;
	}
	
	public boolean isBinding() {
		return this.isBinding;
	}
	
	public void setBinding(boolean b) {
		this.isBinding = b;
	}

	public enum Category {
		CLIENT("Client"),
		COMBAT("Combat"),
		PLAYER("Player"),
		MISC("Miscellaneous"),
		MOVEMENT("Movement"),
		RENDER("Render"),
		HUD("Hud"),
		BOT("Bot");
		
		private final String name;

		private Category(final String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
	}
	
}

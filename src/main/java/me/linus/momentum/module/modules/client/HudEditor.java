package me.linus.momentum.module.modules.client;

import me.linus.momentum.gui.main.HUD;
import me.linus.momentum.module.Module;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HudEditor extends Module {
	public HudEditor() {
		super("HUD", Category.CLIENT, "The in-game hud editor");
	}
	
	public static HUD hudEditor = new HUD();
	
	public static int boost = 0;
	
	@Override
	public void onEnable() {
		if (nullCheck())
			return;

		super.onEnable();
		mc.displayGuiScreen(hudEditor);
	}
	
	@SubscribeEvent
	public void onRender(RenderGameOverlayEvent.Text event) {
		//boost = 0;
	}
	
}

package me.linus.momentum.event;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author bon & linustouchtips
 * @since 11/21/2020
 */

@Cancelable
public class MomentumEvent extends Event {
	
	Stage stage;
	
	public MomentumEvent() {
		
	}
	
	public MomentumEvent(Stage stage) {
		this.stage = stage;
	}
	
	public Stage getStage() {
		return this.stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
		this.setCanceled(false);
	}
	
	public enum Stage {
		PRE,
		POST
	}
}

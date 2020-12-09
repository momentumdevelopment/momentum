package me.linus.momentum.event;

import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author bon
 * @since 11/21/20
 */

public class MomentumEvent extends Event {
	
	private Stage s;
	
	public MomentumEvent() {
		
	}
	
	public MomentumEvent(Stage s) {
		this.s = s;
	}
	
	public Stage getStage() {
		return this.s;
	}
	
	public enum Stage {
		PRE,
		POST
	}
}

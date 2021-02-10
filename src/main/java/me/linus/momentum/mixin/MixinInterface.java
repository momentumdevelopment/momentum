package me.linus.momentum.mixin;

import net.minecraft.client.Minecraft;

/**
 * @author bon & linustouchtips
 * @since 11/12/20
 */

public interface MixinInterface {
	
	/*
	 * Generic mixin interface for anything in mixins (or anywhere) that
	 * you might want to access statically.
	 * 
	 * This was intended to only be used for mixins but I basically use it
	 * everywhere else.
	 */
	
	Minecraft mc = Minecraft.getMinecraft();
	boolean nullCheck = mc.player == null || mc.world == null;
}

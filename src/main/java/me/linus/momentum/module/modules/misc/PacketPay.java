package me.linus.momentum.module.modules.misc;

import me.linus.momentum.module.Module;

/**
 * @author CrawLeyYou
 */

public class PacketPay extends Module {
    public PacketPay() {
        super("PacketPay", Category.MISC, "awsom module");
    }
        @Override
        public void toggle(){
                mc.player.sendChatMessage(">I bought momentum thanks to Linustouchtips!");
        }


    }

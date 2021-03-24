package me.linus.momentum.module.modules.misc;


import me.linus.momentum.module.Module;


public class Pay extends Module {
    public Pay() {
        super("PacketPay", Category.MISC, "awsom module");
    }
        @Override
        public void toggle(){
                mc.player.sendChatMessage(">I bought momentum thanks to Linustouchtips!");
        }


    }

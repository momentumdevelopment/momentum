package me.linus.momentum.module.modules.player;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.minecraft.network.play.client.CPacketEntityAction.Action.START_SPRINTING;
import static net.minecraft.network.play.client.CPacketEntityAction.Action.STOP_SPRINTING;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class AntiHunger extends Module {
    public AntiHunger() {
        super("AntiHunger", Category.PLAYER, "Allows you to lose no hunger");
    }

    private static final Mode mode = new Mode("Mode", "Packet", "Vanilla");

    @Override
    public void setup() {
        addSetting(mode);
    }

    public void onUpdate() {
        if (mode.getValue() == 1)
            mc.player.getFoodStats().setFoodLevel(20);
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (nullCheck())
            return;

        if (mode.getValue() != 0)
            return;

        if (event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer) event.getPacket();
            packet.onGround = (mc.player.fallDistance > 0 || mc.playerController.isHittingBlock);
        }

        if (event.getPacket() instanceof CPacketEntityAction) {
            CPacketEntityAction packet = (CPacketEntityAction) event.getPacket();
            if (packet.getAction() == START_SPRINTING || packet.getAction() == STOP_SPRINTING)
                event.setCanceled(true);
        }
    }
}

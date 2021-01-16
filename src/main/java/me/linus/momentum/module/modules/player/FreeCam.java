package me.linus.momentum.module.modules.player;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.player.FlightUtil;
import me.linus.momentum.util.world.WorldUtil;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 12/11/2020
 */

public class FreeCam extends Module {
    public FreeCam() {
        super("FreeCam", Category.PLAYER, "Allows you to fly out of your body");
    }

    public static Slider speed = new Slider("Speed", 0.0D, 1.0D, 3.0D, 1);
    public static Checkbox playerModel = new Checkbox("Player Model", true);
    public static Checkbox cancelPackets = new Checkbox("Cancel Packets", true);
    public static Checkbox exploitFreecam = new Checkbox("Exploit", true);
    public static Checkbox noClip = new Checkbox("NoClip", true);

    @Override
    public void setup() {
        addSetting(speed);
        addSetting(playerModel);
        addSetting(cancelPackets);
        addSetting(exploitFreecam);
        addSetting(noClip);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (nullCheck())
            return;

        WorldUtil.createFakePlayer(true, null, true, true, true);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        mc.player.noClip = noClip.getValue();
        mc.player.onGround = false;
        mc.player.fallDistance = 0;

        if (mc.gameSettings.keyBindJump.isKeyDown())
            mc.player.motionY = speed.getValue();
        else if (mc.gameSettings.keyBindSneak.isKeyDown())
            mc.player.motionY = -speed.getValue();
        else
            mc.player.motionY = 0;

        FlightUtil.horizontalEntityFlight(speed.getValue());
    }

    @Override
    public void onDisable() {
        super.onDisable();

        mc.world.removeEntityFromWorld(69420);
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if ((event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketInput) && cancelPackets.getValue())
            event.setCanceled(true);
    }
}

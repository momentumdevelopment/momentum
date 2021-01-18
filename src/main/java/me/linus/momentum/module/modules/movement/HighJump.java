package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import net.minecraft.network.play.client.CPacketPlayer;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class HighJump extends Module {
    public HighJump() {
        super("HighJump", Category.MOVEMENT, "Allows you to jump higher");
    }

    private static final Checkbox packet = new Checkbox("Packet", true);
    public static Slider height = new Slider("Height", 0.0D, 1.5D, 10.0D, 1);

    @Override
    public void setup() {
        addSetting(packet);
        addSetting(height);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.gameSettings.keyBindJump.isPressed()) {
            mc.player.motionY = height.getValue();

            if (packet.getValue())
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.onGround));
        }
    }
}

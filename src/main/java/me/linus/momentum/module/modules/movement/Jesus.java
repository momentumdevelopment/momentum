package me.linus.momentum.module.modules.movement;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.event.events.player.MoveEvent;
import me.linus.momentum.event.events.world.LiquidCollisionEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 12/19/2020
 */

public class Jesus extends Module {
    public Jesus() {
        super("Jesus", Category.MOVEMENT, "Allows you to walk on water");
    }

    private static final Mode mode = new Mode("Mode", "Normal", "Packet", "Push", "Freeze");
    public static Slider offset = new Slider("Offset", 0.0D, 0.2D, 1.0D, 2);
    private static final Slider delay = new Slider("Delay", 0.0D, 2.0D, 10.0D, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(offset);
        addSetting(delay);
    }

    @SubscribeEvent
    public void onLiquidCollision(LiquidCollisionEvent event) {
        if (nullCheck())
            return;

        if (!(mc.player.fallDistance >= 3) && !mc.player.isSneaking() && !mc.gameSettings.keyBindJump.isPressed() && event.getBlockPos().y < mc.player.posY - offset.getValue()) {
            switch (mode.getValue()) {
                case 0:
                case 1:
                    event.setBoundingBox(Block.FULL_BLOCK_AABB);
                    break;
                case 2:
                    event.setBoundingBox(new AxisAlignedBB(0, 0, 0, 1, 1 - offset.getValue(), 1));
                    break;
            }

            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onMoveEvent(MoveEvent event) {
        if (nullCheck())
            return;

        if (PlayerUtil.isInLiquid() && !mc.gameSettings.keyBindJump.isPressed() && mode.getValue() == 3)
            event.setY(0);
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketPlayer) {
            if (!mc.gameSettings.keyBindJump.isPressed() && !(mc.player.fallDistance >= 3) && !mc.player.isSneaking() && !PlayerUtil.isInLiquid() && PlayerUtil.isOnLiquid(offset.getValue()) && mc.player.ticksExisted % delay.getValue() == 0 && mode.getValue() == 1)
                ((CPacketPlayer) event.getPacket()).y -= offset.getValue();
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}

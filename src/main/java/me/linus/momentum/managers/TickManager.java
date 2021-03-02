package me.linus.momentum.managers;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.util.client.MathUtil;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author rigamortis
 * @since 11/26/2020
 */

public class TickManager {

    public long prevTime;
    public static float[] TPS = new float[20];
    public int currentTick;

    public TickManager() {
        prevTime = -1;

        for (int i = 0, len = TPS.length; i < len; i++) {
            TPS[i] = 0.0f;
        }

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static float getTPS() {
        int tickCount = 0;
        float tickRate = 0.0f;

        for (float tick : TPS) {
            if (tick > 0.0f) {
                tickRate += tick;
                tickCount++;
            }
        }

        return (float) MathUtil.roundAvoid(MathUtil.clamp((tickRate / tickCount), 0.0f, 20.0f), 2);
    }

    @SubscribeEvent
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            if (prevTime != -1) {
                TPS[currentTick % TPS.length] = MathHelper.clamp((20.0f / ((float) (System.currentTimeMillis() - prevTime) / 1000.0f)), 0.0f, 20.0f);
                currentTick++;
            }

            prevTime = System.currentTimeMillis();
        }
    }
}

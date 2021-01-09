package me.linus.momentum.util.world;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * @author ionar2 & linustouchtips
 * @since 11/26/2020
 */

public class TickUtil {

    public static float TPS = 20.0f;
    public static long lastUpdate = -1;
    public static float[] tpsCounts = new float[10];
    public static DecimalFormat format = new DecimalFormat("##.0#");

    public TickUtil() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPacketRecieve(PacketReceiveEvent event) {
        if (!(event.getPacket() instanceof SPacketTimeUpdate))
            return;

        long currentTime = System.currentTimeMillis();

        if (lastUpdate == -1) {
            lastUpdate = currentTime;
            return;
        }

        long timeDiff = currentTime - lastUpdate;
        float tickTime = timeDiff / 20;
        if (tickTime == 0)
            tickTime = 50;

        float tps = 1000 / tickTime;
        if (tps > 20.0f)
            tps = 20.0f;

        if (tpsCounts.length - 1 >= 0) System.arraycopy(tpsCounts, 0, tpsCounts, 1, tpsCounts.length - 1);
        tpsCounts[0] = tps;

        double total = 0.0;
        for (float f : tpsCounts)
            total += f;

        total /= tpsCounts.length;

        if (total > 20.0)
            total = 20.0;

        TPS = Float.parseFloat(format.format(total));
        lastUpdate = currentTime;
    }

    public static void reset() {
        Arrays.fill(tpsCounts, 20.0f);
        TPS = 20.0f;
    }
}

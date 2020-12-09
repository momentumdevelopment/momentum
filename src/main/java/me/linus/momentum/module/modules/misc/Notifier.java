package me.linus.momentum.module.modules.misc;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.util.client.MessageUtil;
import me.linus.momentum.util.client.Timer;
import me.linus.momentum.util.client.notification.Notification;
import me.linus.momentum.util.client.notification.NotificationManager;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

/**
 * @author linustouchtips
 * @since 12/07/2020
 */

public class Notifier extends Module {
    public Notifier() {
        super("Notifier", Category.MISC, "Notifies you for various things");
    }

    private static Checkbox totem = new Checkbox("Totem Pop", true);
    private static Checkbox armor = new Checkbox("Armor", true);
    private static Checkbox visualRange = new Checkbox("Visual Range", false);

    @Override
    public void setup() {
        addSetting(totem);
        addSetting(armor);
        addSetting(visualRange);
    }

    private HashMap<String, Integer> totemPopContainer = new HashMap<String, Integer>();
    Timer visualTimer = new Timer();

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (armor.getValue() && PlayerUtil.getArmor(mc.player, 15)) {
            NotificationManager.notifications.add(new Notification("ArmorNotifier", "Your armor durability is getting low!"));
            MessageUtil.sendClientMessage("Your armor durability is getting low!");
        }

        for (EntityPlayer player : mc.world.playerEntities) {
            if (totem.getValue()) {
                if (!totemPopContainer.containsKey(player.getName()))
                    continue;

                if (player.isDead || player.getHealth() <= 0.0f) {
                    int count = totemPopContainer.get(player.getName()).intValue();

                    totemPopContainer.remove(player.getName());

                    NotificationManager.notifications.add(new Notification("TotemPopNotifier", player.getName() + " died after popping " + count + " totems!"));
                    MessageUtil.sendClientMessage(player.getName() + " died after popping " + count + " totems!");
                }
            }

            if (player != mc.player && visualTimer.passed(10000)) {
                NotificationManager.notifications.add(new Notification("RangeNotifier", player.getName() + "has entered your visual range!"));
                MessageUtil.sendClientMessage(player.getName() + "has entered your visual range!");
            }

            visualTimer.reset();
        }
    }

    @SubscribeEvent
    public void onPacketRecieve(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketEntityStatus && totem.getValue()) {
            SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            if (packet.getOpCode() == 35) {
                Entity entity = packet.getEntity(mc.world);

                if (entity == null)
                    return;

                int count = 1;
                if (totemPopContainer.containsKey(entity.getName())) {
                    count = totemPopContainer.get(entity.getName()).intValue();
                    totemPopContainer.put(entity.getName(), count++);
                }

                else
                    totemPopContainer.put(entity.getName(), count);

                NotificationManager.notifications.add(new Notification("TotemPopNotifier", entity.getName() + " popped " + count + " totems!"));
                MessageUtil.sendClientMessage(entity.getName() + " popped " + count + " totems!");
            }
        }
    }
}

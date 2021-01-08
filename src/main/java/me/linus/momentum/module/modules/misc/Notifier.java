package me.linus.momentum.module.modules.misc;

import me.linus.momentum.Momentum;
import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.MessageUtil;
import me.linus.momentum.util.client.Timer;
import me.linus.momentum.util.client.notification.Notification;
import me.linus.momentum.util.client.notification.NotificationManager;
import me.linus.momentum.util.player.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

/**
 * @author linustouchtips & olliem5
 * @since 12/07/2020
 */

public class Notifier extends Module {
    public Notifier() {
        super("Notifier", Category.MISC, "Notifies you for various things");
    }

    private static final Checkbox events = new Checkbox("Events", true);
    private static final SubCheckbox totem = new SubCheckbox(events,"Totem Pop", true);
    private static final SubCheckbox armor = new SubCheckbox(events,"Armor", true);
    private static final SubCheckbox visualRange = new SubCheckbox(events,"Visual Range", false);

    private static final Checkbox delays = new Checkbox("Delays", true);
    private static final SubSlider armorDelay = new SubSlider(delays, "Armor Delay", 5000, 8000, 10000, 0);
    private static final SubSlider visualRangeDelay = new SubSlider(delays, "VisualRng Delay", 5000, 8000, 10000, 0);

    @Override
    public void setup() {
        addSetting(events);
        addSetting(delays);
    }

    private final HashMap<String, Integer> totemPopContainer = new HashMap<>();

    Timer visualRangeTimer = new Timer();
    Timer armorTimer = new Timer();

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (armor.getValue() && PlayerUtil.getArmor(mc.player, 15)) {
            if (armorTimer.passed((long) armorDelay.getValue())) {
                armorTimer.reset();

                NotificationManager.notifications.add(new Notification("Your armor durability is getting low!"));
                MessageUtil.sendClientMessage("Your armor durability is getting low!");
            }
        }

        for (EntityPlayer player : mc.world.playerEntities) {
            if (totem.getValue()) {
                if (!totemPopContainer.containsKey(player.getName()))
                    continue;

                if (player.isDead || player.getHealth() <= 0.0f) {
                    int count = totemPopContainer.get(player.getName()).intValue();

                    totemPopContainer.remove(player.getName());

                    NotificationManager.notifications.add(new Notification(player.getName() + " died after popping " + count + " totems!"));
                    MessageUtil.sendClientMessage(player.getName() + " died after popping " + count + " totems!");
                }
            }

            if (player != mc.player && visualRangeTimer.passed((long) visualRangeDelay.getValue()) && visualRange.getValue()) {
                visualRangeTimer.reset();

                if (Momentum.friendManager.isFriend(player.getName())) {
                    NotificationManager.notifications.add(new Notification("Your friend, " + player.getName() + ", has entered your visual range!"));
                    MessageUtil.sendClientMessage("Your friend, " + player.getName() + ", has entered your visual range!");
                }

                else {
                    NotificationManager.notifications.add(new Notification(player.getName() + "has entered your visual range!"));
                    MessageUtil.sendClientMessage(player.getName() + "has entered your visual range!");
                }
            }
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

                if (Momentum.friendManager.isFriend(entity.getName())) {
                    NotificationManager.notifications.add(new Notification("Your friend, " + entity.getName() + ", popped " + count + " totems!"));
                    MessageUtil.sendClientMessage("Your friend, " + entity.getName() + ", popped " + count + " totems!");
                }

                else {
                    NotificationManager.notifications.add(new Notification(entity.getName() + " popped " + count + " totems!"));
                    MessageUtil.sendClientMessage(entity.getName() + " popped " + count + " totems!");
                }
            }
        }
    }
}

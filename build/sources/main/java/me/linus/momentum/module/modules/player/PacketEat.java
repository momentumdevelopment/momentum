package me.linus.momentum.module.modules.player;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.client.Timer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class PacketEat extends Module {
    public PacketEat() {
        super("PacketEat", Category.PLAYER, "Allows you to eat instantly");
    }

    private static Mode mode = new Mode("Mode", "Packet", "DeSync");
    public static Slider delay = new Slider("Delay", 0.0D, 6.0D, 10.0D, 0);

    Timer timer = new Timer();

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(delay);
    }

    @SubscribeEvent
    public void onPlayerRightClick(PlayerInteractEvent.RightClickItem event) {
        ItemStack itemStack = event.getItemStack();
        Item item = itemStack.getItem();

        if (item.equals(Items.GOLDEN_APPLE)) {
            if (timer.passed((long) (delay.getValue() * 10))) {
                event.setCanceled(true);
                item.onItemUseFinish(itemStack, event.getWorld(), event.getEntityPlayer());
            }

            timer.reset();
        }
    }
}

package me.linus.momentum.module.modules.player;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.Timer;
import me.linus.momentum.util.world.InventoryUtil;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
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

    private static Mode mode = new Mode("Mode", "Packet", "DeSync", "Auto");
    public static SubSlider health = new SubSlider(mode, "Health", 0.0D, 28.0D, 36.0D, 0);
    public static Slider delay = new Slider("Delay", 0.0D, 6.0D, 10.0D, 0);

    Timer timer = new Timer();

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(delay);
    }

    @Override
    public void onUpdate() {
        if (mode.getValue() == 3) {
            if (PlayerUtil.getHealth() <= health.getValue()) {
                InventoryUtil.switchToSlotGhost(findGappleInHotbar());
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            }
        }

        if (mc.player.isHandActive() && mc.player.getHeldItemMainhand().getItem() instanceof ItemAppleGold) {
            for (int i = 0; i < 20; i++) {
                mc.player.connection.sendPacket(new CPacketPlayer());
            }

            mc.player.stopActiveHand();
        }
    }

    @SubscribeEvent
    public void onPlayerRightClick(PlayerInteractEvent.RightClickItem event) {
        ItemStack itemStack = event.getItemStack();
        Item item = itemStack.getItem();

        if (item.equals(Items.GOLDEN_APPLE) && mode.getValue() == 1) {
            if (timer.passed((long) (delay.getValue() * 10))) {
                event.setCanceled(true);
                item.onItemUseFinish(itemStack, event.getWorld(), event.getEntityPlayer());
            }

            timer.reset();
        }
    }

    private int findGappleInHotbar() {
        int slot = 0;
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.GOLDEN_APPLE) {
                slot = i;
                break;
            }
        }

        return slot;
    }
}

package me.linus.momentum.module.modules.player;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.world.Timer;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.player.PlayerUtil;
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

    public static Mode mode = new Mode("Mode", "Packet", "DeSync", "Auto");
    public static SubSlider health = new SubSlider(mode, "Health", 0.0D, 28.0D, 36.0D, 0);
    public static Slider packetSize = new Slider("Packet Iteration", 0.0D, 20.0D, 40.0D, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(packetSize);
    }

    @Override
    public void onUpdate() {
        if (mc.player.isHandActive() && mc.player.getHeldItemMainhand().getItem() instanceof ItemAppleGold && (mode.getValue() == 0 || mode.getValue() == 2)) {
            for (int i = 0; i < packetSize.getValue(); i++)
                mc.player.connection.sendPacket(new CPacketPlayer());

            mc.player.stopActiveHand();
        }

        if (mode.getValue() == 2) {
            if (PlayerUtil.getHealth() <= health.getValue()) {
                InventoryUtil.switchToSlotGhost(InventoryUtil.getHotbarItemSlot(Items.GOLDEN_APPLE));
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRightClick(PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().getItem().equals(Items.GOLDEN_APPLE) && mode.getValue() == 1) {
            event.setCanceled(true);
            event.getItemStack().getItem().onItemUseFinish(event.getItemStack(), event.getWorld(), event.getEntityPlayer());
        }
    }
}

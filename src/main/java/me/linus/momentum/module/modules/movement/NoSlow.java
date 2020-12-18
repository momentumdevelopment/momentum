package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author novola & olliem5
 * @since 12/03/2020
 */

public class NoSlow extends Module {
    public NoSlow() {
        super("NoSlow", Category.MOVEMENT, "Allows you to move at normal speeds when using an item");
    }

    private static final Mode mode = new Mode("Mode", "Normal", "2b2t");

    @Override
    public void setup() {
        addSetting(mode);
    }

    private boolean sneaking;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mode.getValue() == 1) {
            Item item = mc.player.getActiveItemStack().getItem();
            if (sneaking && ((!mc.player.isHandActive() && item instanceof ItemFood || item instanceof ItemBow || item instanceof ItemPotion) || (!(item instanceof ItemFood) || !(item instanceof ItemBow) || !(item instanceof ItemPotion)))) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                sneaking = false;
            }
        }
    }

    @SubscribeEvent
    public void onUseItem(LivingEntityUseItemEvent event) {
        if (mode.getValue() == 1) {
            if (!sneaking) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                sneaking = true;
            }
        }
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event) {
        if (mode.getValue() == 0) {
            if (mc.player.isHandActive() && !mc.player.isRiding()) {
                event.getMovementInput().moveStrafe *= 5;
                event.getMovementInput().moveForward *= 5;
            }
        }
    }
}

package me.linus.momentum.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.client.MessageUtil;
import me.linus.momentum.util.world.Timer;
import me.linus.momentum.util.player.InventoryUtil;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

import java.util.Comparator;

/**
 * @author olliem5
 * @since 7/01/20
 *
 * Credit - Max (java!) for the idea
 *
 * TODO: Checks for all types of pressure plates
 * TODO: Stop pressure plates from being placed right after crystal explodes
 */

public class AntiCrystal extends Module {
    public AntiCrystal() {
        super("AntiCrystal", Category.COMBAT, "Minimises crystal damage with pressure plates");
    }

    public static final Slider placeRange = new Slider("Place Range", 0.0, 5.5, 10.0, 1);
    public static final Slider placeDelay = new Slider("Place Delay", 0, 2, 20, 1);

    @Override
    public void setup() {
        this.addSetting(placeRange);
        this.addSetting(placeDelay);
    }

    private int pressurePlateSlot;

    private Timer placeTimer = new Timer();

    @Override
    public void onEnable() {
        pressurePlateSlot = InventoryUtil.getBlockInHotbar(Blocks.WOODEN_PRESSURE_PLATE);

        if (pressurePlateSlot == -1) {
            MessageUtil.sendClientMessage("No Pressure Plate, " + ChatFormatting.RED + "Disabling!");
            this.toggle();
        }
    }

    public void onUpdate() {
        if (nullCheck()) return;

        EntityEnderCrystal entityEnderCrystal = (EntityEnderCrystal) mc.world.loadedEntityList.stream()
                .filter(entity -> entity != null)
                .filter(entity -> entity instanceof EntityEnderCrystal)
                .filter(entity -> mc.player.getDistance(entity) <= placeRange.getValue())
                .filter(entity -> !hasPressurePlate((EntityEnderCrystal) entity))
                .min(Comparator.comparing(entity -> mc.player.getDistance(entity)))
                .orElse(null);

        if (entityEnderCrystal != null) {
            if (pressurePlateSlot != -1) {
                mc.player.inventory.currentItem = pressurePlateSlot;
            }

            if (placeTimer.passed((long) (placeDelay.getValue() * 100), Timer.Format.System)) {
                if (mc.player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(Blocks.WOODEN_PRESSURE_PLATE)) {
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(entityEnderCrystal.getPosition(), EnumFacing.UP, EnumHand.MAIN_HAND, 0, 0, 0));
                }

                placeTimer.reset();
            }
        }
    }

    private boolean hasPressurePlate(EntityEnderCrystal entityEnderCrystal) {
        return mc.world.getBlockState(entityEnderCrystal.getPosition()).getBlock() == Blocks.WOODEN_PRESSURE_PLATE;
    }
}

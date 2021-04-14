package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.keybind.Keybind;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.world.Timer;
import me.linus.momentum.util.world.Timer.Format;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class AutoArmor extends Module {
    public AutoArmor() {
        super("AutoArmor", Category.COMBAT, "Automatically replaces armor");
    }

    public static Slider delay = new Slider("Delay", 0.0D, 200.0D, 1000.0D, 0);
    public static Checkbox curse = new Checkbox("Ignore Curse", true);
    public static Checkbox elytra = new Checkbox("Prefer Elytra", false);
    public static Keybind elytraKey = new Keybind("Elytra Key", Keyboard.KEY_NONE);

    @Override
    public void setup() {
        addSetting(delay);
        addSetting(curse);
        addSetting(elytra);
        addSetting(elytraKey);
    }

    Timer delayTimer = new Timer();
    boolean elytraPressed = false;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (Keyboard.isKeyDown(elytraKey.getKey()))
            elytraPressed = !elytraPressed;

        if (mc.player.inventoryContainer.getInventory().get(6).getItem().equals(Items.ELYTRA))
            elytraPressed = false;

        switchArmor(5, findBestArmor(EntityEquipmentSlot.HEAD));
        switchArmor(6, findBestArmor(EntityEquipmentSlot.CHEST));
        switchArmor(7, findBestArmor(EntityEquipmentSlot.LEGS));
        switchArmor(8, findBestArmor(EntityEquipmentSlot.FEET));
    }

    public void switchArmor(int outgoing, int incoming) {
        if (delayTimer.passed((long) delay.getValue(), Format.System)) {
            delayTimer.reset();
            return;
        }

        if (mc.currentScreen instanceof GuiContainer && !(mc.currentScreen instanceof InventoryEffectRenderer))
            return;

        if (!mc.player.inventoryContainer.getInventory().get(outgoing).getItem().equals(Items.AIR) && !elytraPressed)
            return;

        if (incoming != -1 && outgoing != -1) {
            if (incoming <= 4) {
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, incoming, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, outgoing, 0, ClickType.PICKUP, mc.player);
            }

            else
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, incoming, 0, ClickType.QUICK_MOVE, mc.player);
        }
    }

    public int findBestArmor(EntityEquipmentSlot armorType) {
        int slot = -1;
        List<Armor> armorList = new ArrayList<>();

        for (int i = 0; i < mc.player.inventoryContainer.getInventory().size(); i++) {
            if (i == 0 || i == 5 || i == 6 || i == 7 || i == 8)
                continue;

            if (curse.getValue() && EnchantmentHelper.hasBindingCurse(mc.player.inventoryContainer.getInventory().get(i)))
                continue;

            if (mc.player.inventoryContainer.getInventory().get(i).getItem() instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor) mc.player.inventoryContainer.getInventory().get(i).getItem();

                if (armorType.equals(armor.armorType))
                    armorList.add(new Armor(i, armor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, mc.player.inventoryContainer.getInventory().get(i))));
            }
        }

        if (armorType.equals(EntityEquipmentSlot.CHEST) && elytra.getValue() || elytraPressed)
            return InventoryUtil.getInventoryItemSlot(Items.ELYTRA, true);
        else
            slot = armorList.stream().max(Comparator.comparing(armor -> armor.getDurability())).orElse(null).getSlot();

        return slot;
    }

    public class Armor {
        int slot;
        int durability;

        public Armor(int slot, int durability) {
            this.slot = slot;
            this.durability = durability;
        }

        public int getSlot() {
            return this.slot;
        }

        public int getDurability() {
            return this.durability;
        }
    }
}

package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.AnchorPoint;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Armor extends HUDComponent {
    public Armor() {
        super("Armor", 200, 200, AnchorPoint.None);
    }

    public static Checkbox durability = new Checkbox("Durability", false);

    @Override
    public void setup() {
        addSetting(durability);
    }

    @Override
    public void renderComponent() {
        GlStateManager.enableTexture2D();
        int offset = 0;
        for (ItemStack itemStack : mc.player.inventory.armorInventory) {
            offset++;

            if (itemStack.isEmpty())
                continue;

            int x = ((9 - offset) * 14);

            float durabilityScaled = ((float) (itemStack.getMaxDamage() - itemStack.getItemDamage()) / (float) itemStack.getMaxDamage()) * 100.0f;

            int color = 0x1FFF00;

            if (durabilityScaled > 30 && durabilityScaled < 70)
                color = 0xFFFF00;
            else if (durabilityScaled <= 30)
                color = 0xFF0000;

            if (durability.getValue())
                FontUtil.drawString(new StringBuilder().insert(0, ((int) (durabilityScaled))).append('%').toString(), (this.x - 70) + x, this.y - 16, color);

            mc.getRenderItem().zLevel = 200.0f;
            mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, (this.x - 70) + x , this.y);
            mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, (this.x - 70) + x, this.y, "");
            mc.getRenderItem().zLevel = 0.0f;
            width = x - 8;
            height = 17;
        }

        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }
}
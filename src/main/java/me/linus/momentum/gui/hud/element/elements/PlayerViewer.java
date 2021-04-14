package me.linus.momentum.gui.hud.element.elements;

import me.linus.momentum.gui.hud.element.AnchorPoint;
import me.linus.momentum.util.client.color.ThemeColor;
import me.linus.momentum.gui.hud.element.HUDElement;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.render.Render2DUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class PlayerViewer extends HUDElement {
    public PlayerViewer() {
        super("PlayerViewer", 400, 70, Category.MISC, AnchorPoint.None);
        height = 80;
        width = 50;
    }

    public static Slider scale = new Slider("Scale", 0.0D, 30.0D, 100.0D, 0);

    @Override
    public void setup() {
        addSetting(scale);
    }

    @Override
    public void renderElement() {
        if (mc.player == null || mc.world  == null)
            return;

        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

        Render2DUtil.drawEntityOnScreen(this.x + 28, this.y + 67, (int) scale.getValue(), this.y + 13, mc.player);

        GlStateManager.enableRescaleNormal();
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();

        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    }
}

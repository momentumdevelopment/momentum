package me.linus.momentum.gui.hud.components;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.world.TickUtil;
import net.minecraft.client.resources.I18n;

import java.text.DecimalFormat;

/**
 * @author linustouchtips
 * @since 12/23/2020
 */

public class PotionEffects extends HUDComponent {
    public PotionEffects() {
        super("PotionEffects", 400, 400);
    }

    int count;

    @Override
    public void renderComponent() {
        DecimalFormat minuteFormatter = new DecimalFormat("0");
        DecimalFormat secondsFormatter = new DecimalFormat("00");

        count = 0;
        try {
            mc.player.getActivePotionEffects().forEach(effect -> {
                String name = I18n.format(effect.getPotion().getName());
                
                int amplifier = effect.getAmplifier() + 1;
                double duration = effect.getDuration() / TickUtil.TPS;
                double timeSeconds = duration % 60;
                double timeMinutes = duration / 60;
                double timeMinutesRounded = timeMinutes % 60;
                
                String minutes = minuteFormatter.format(timeMinutesRounded);
                String seconds = secondsFormatter.format(timeSeconds);
                String potionString = name + " " + amplifier + ChatFormatting.WHITE + " " + minutes + ":" + seconds;

                FontUtil.drawStringWithShadow(potionString, this.x, this.y + (count * 10), effect.getPotion().getLiquidColor());
                count++;

                width = (int) FontUtil.getStringWidth(potionString);
                height = (mc.fontRenderer.FONT_HEIGHT + 3) * count;
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

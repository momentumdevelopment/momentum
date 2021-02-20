package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.AnchorPoint;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.managers.social.friend.FriendManager;
import me.linus.momentum.util.client.ColorUtil;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.combat.EnemyUtil;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.world.WorldUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;

public class TextRadar extends HUDComponent {
    public TextRadar() {
        super("TextRadar", 2, 80, AnchorPoint.TopLeft);
    }

    int count;

    @Override
    public void renderComponent() {
        count = 0;
        int screenWidth = new ScaledResolution(mc).getScaledWidth();
        WorldUtil.getNearbyPlayers(20).forEach(entityPlayer -> {
            int screenWidthScaled = new ScaledResolution(mc).getScaledWidth();
            float modWidth = FontUtil.getStringWidth(ColorUtil.getHealthText(EnemyUtil.getHealth(entityPlayer)) + String.valueOf(EnemyUtil.getHealth(entityPlayer)) + (FriendManager.isFriend(entityPlayer.getName()) ? TextFormatting.AQUA : TextFormatting.RESET) + entityPlayer.getName() + TextFormatting.WHITE + mc.player.getDistance(entityPlayer));
            String modText = ColorUtil.getHealthText(EnemyUtil.getHealth(entityPlayer)) + String.valueOf(MathUtil.roundAvoid(EnemyUtil.getHealth(entityPlayer), 1)) +  " " + (FriendManager.isFriend(entityPlayer.getName()) ? TextFormatting.AQUA : TextFormatting.RESET) + entityPlayer.getName() + TextFormatting.WHITE + " " + MathUtil.roundAvoid(mc.player.getDistance(entityPlayer), 1);

            if (this.x < (screenWidthScaled / 2))
                FontUtil.drawString(modText, this.x - 2, this.y + (10 * count), ThemeColor.BRIGHT);
            else
                FontUtil.drawString(modText, this.x, this.y + (10 * count), ThemeColor.BRIGHT);

            count++;

            if (this.x < (screenWidth / 2))
                width = (int) (modWidth + 5);
            else
                width = (int) (modWidth - 5);
        });

        height = ((mc.fontRenderer.FONT_HEIGHT + 1) * count);
    }
}

package me.linus.momentum.module.modules.render;

import me.linus.momentum.event.events.render.Render3DEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.util.combat.EnemyUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class NameTags extends Module {
    public NameTags() {
        super("NameTags", Category.RENDER, "Draws useful information at player's heads");
    }

    private static final Checkbox health = new Checkbox("Health", true);
    private static final Checkbox ping = new Checkbox("Ping", true);
    private static final Checkbox gamemode = new Checkbox("GameMode", false);

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent renderEvent) {

    }

    public String generateNameTag(EntityPlayer entityPlayer) {
        return generateName(entityPlayer) + generateGamemode(entityPlayer) + generatePing(entityPlayer) + TextFormatting.GREEN + generateHealth(entityPlayer);
    }

    public String generateHealth(EntityPlayer entityPlayer) {
        return String.valueOf(EnemyUtil.getHealth(entityPlayer));
    }

    public String generateGamemode(EntityPlayer entityPlayer) {
        if (entityPlayer.isCreative())
            return "[C]";
        if (entityPlayer.isSpectator())
            return "[I]";
        else
            return "[S]";
    }

    public String generatePing(EntityPlayer entityPlayer) {
        if (!mc.isSingleplayer())
            return String.valueOf(mc.getConnection().getPlayerInfo(entityPlayer.getUniqueID()).getResponseTime());
        else
            return "-1";
    }

    public String generateName(EntityPlayer entityPlayer) {
        return entityPlayer.getName();
    }
}

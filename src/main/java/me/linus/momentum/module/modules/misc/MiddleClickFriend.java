package me.linus.momentum.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.linus.momentum.Momentum;
import me.linus.momentum.module.Module;
import me.linus.momentum.util.client.MessageUtil;
import me.linus.momentum.util.client.friend.FriendManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

/**
 * @author linustouchtips & olliem5
 * @since 11/30/2020
 */

public class MiddleClickFriend extends Module {
    public MiddleClickFriend() {
        super("MiddleClickFriend", Category.MISC, "Adds players to your friends list when you middle click them");
    }

    private boolean hasClicked = false;

    public void onUpdate() {
        if (nullCheck()) return;

        if (!Mouse.isButtonDown(2)) {
            hasClicked = false;
            return;
        }

        if (!hasClicked) {
            hasClicked = true;

            final RayTraceResult result = mc.objectMouseOver;

            if (result == null || result.typeOfHit != RayTraceResult.Type.ENTITY || !(result.entityHit instanceof EntityPlayer)) return;

            if (Momentum.friendManager.isFriend(mc.objectMouseOver.entityHit.getName())) {
                Momentum.friendManager.removeFriend(mc.objectMouseOver.entityHit.getName());
                MessageUtil.sendClientMessage(ChatFormatting.RED + "Removed " + ChatFormatting.LIGHT_PURPLE + mc.objectMouseOver.entityHit.getName() + ChatFormatting.WHITE + " from friends list");
            } else {
                Momentum.friendManager.addFriend(mc.objectMouseOver.entityHit.getName());
                MessageUtil.sendClientMessage(ChatFormatting.GREEN + "Added " + ChatFormatting.LIGHT_PURPLE + mc.objectMouseOver.entityHit.getName() + ChatFormatting.WHITE + " to friends list");
            }
        }
    }
}

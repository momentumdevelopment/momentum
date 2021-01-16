package me.linus.momentum.util.world;

import com.mojang.authlib.GameProfile;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.Module;
import me.linus.momentum.util.client.friend.FriendManager;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.GameType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author linustouchtips
 * @since 12/26/2020
 */

public class WorldUtil implements MixinInterface {

    public static void createFakePlayer(boolean player, @Nullable String name, boolean copyInventory, boolean copyAngles, boolean health) {
        EntityOtherPlayerMP entity = player ? new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile()) : new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("873e2766-9254-49bc-89d7-5d4d585ad29d"), name));
        entity.copyLocationAndAnglesFrom(mc.player);

        if (copyInventory)
            entity.inventory.copyInventory(mc.player.inventory);

        if (copyAngles) {
            entity.rotationYaw = mc.player.rotationYaw;
            entity.rotationYawHead = mc.player.rotationYawHead;
        }

        if (health)
            entity.setHealth(mc.player.getHealth() + mc.player.getAbsorptionAmount());

        mc.world.addEntityToWorld(69420, entity);
        entity.setGameType(GameType.SURVIVAL);
    }

    public static EntityPlayer getClosestPlayer(double range) {
        if (mc.world.getLoadedEntityList().size() == 0)
            return null;

        EntityPlayer closestPlayer = mc.world.playerEntities.stream().filter(entityPlayer -> mc.player != entityPlayer).filter(entityPlayer -> mc.player.getDistance(entityPlayer) <= range).filter(entityPlayer -> !entityPlayer.isDead).findFirst().orElse(null);

        if (FriendManager.isFriend(closestPlayer.getName()) && FriendManager.isFriendModuleEnabled())
            return null;

        return closestPlayer;
    }

    public static List<EntityPlayer> getNearbyPlayers(double range) {
        if (mc.world.getLoadedEntityList().size() == 0)
            return null;

        List<EntityPlayer> nearbyPlayers = mc.world.playerEntities.stream().filter(entityPlayer -> mc.player != entityPlayer).filter(entityPlayer -> mc.player.getDistance(entityPlayer) <= range).filter(entityPlayer -> !entityPlayer.isDead).collect(Collectors.toList());

        for (EntityPlayer closestPlayer : nearbyPlayers)
            if (FriendManager.isFriend(closestPlayer.getName()) && FriendManager.isFriendModuleEnabled())
                nearbyPlayers.remove(closestPlayer);

        return nearbyPlayers;
    }

    public static void disconnectFromWorld(Module module) {
        module.disable();
        mc.world.sendQuittingDisconnectingPacket();
        mc.loadWorld(null);
        mc.displayGuiScreen(new GuiMainMenu());
    }
}

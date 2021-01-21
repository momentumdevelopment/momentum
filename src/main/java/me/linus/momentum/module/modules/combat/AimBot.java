package me.linus.momentum.module.modules.combat;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.event.events.player.RotationEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.client.friend.FriendManager;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.player.rotation.Rotation;
import me.linus.momentum.util.player.rotation.RotationUtil;
import me.linus.momentum.util.world.WorldUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 12/07/2020
 */

public class AimBot extends Module {
    public AimBot() {
        super("AimBot", Category.COMBAT, "Automatically rotates to nearby entities");
    }

    public static Mode mode = new Mode("Rotate","Packet", "Legit", "None");
    public static Slider range = new Slider("Range", 0.0D, 8.0D, 20.0D, 0);
    public static Checkbox onlyBow = new Checkbox("Bow Only", true);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(range);
        addSetting(onlyBow);
    }

    EntityPlayer target = null;
    Rotation aimbotRotation = null;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (!(InventoryUtil.getHeldItem(Items.BOW)) && !mc.player.isHandActive() && !(mc.player.getItemInUseMaxCount() >= 3) && onlyBow.getValue())
            return;

        target = WorldUtil.getClosestPlayer(range.getValue());

        if (target != null && (!FriendManager.isFriend(target.getName()) && FriendManager.isFriendModuleEnabled())) {
            aimbotRotation = new Rotation(RotationUtil.getAngles(target)[0], RotationUtil.getAngles(target)[1]);
            aimbotRotation.updateRotations(mode.getValue());
        }
    }

    @SubscribeEvent
    public void onRotation(RotationEvent event) {
        if (aimbotRotation != null && mode.getValue() == 0) {
            event.setCanceled(true);
            event.setPitch(aimbotRotation.yaw);
            event.setYaw(aimbotRotation.pitch);
        }

        if (target != null && event.isCanceled())
            RotationUtil.resetRotation(event);
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketPlayer.Rotation) {
            ((CPacketPlayer.Rotation) event.getPacket()).yaw = aimbotRotation.yaw;
            ((CPacketPlayer.Rotation) event.getPacket()).pitch = aimbotRotation.pitch;
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}

package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.managers.social.friend.FriendManager;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.player.rotation.Rotation;
import me.linus.momentum.managers.RotationManager;
import me.linus.momentum.util.player.rotation.RotationUtil;
import me.linus.momentum.util.world.WorldUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;

/**
 * @author linustouchtips
 * @since 12/07/2020
 */

public class AimBot extends Module {
    public AimBot() {
        super("AimBot", Category.COMBAT, "Automatically rotates to nearby entities");
    }

    public static Mode mode = new Mode("Rotate","Legit", "Packet", "None");
    public static Slider range = new Slider("Range", 0.0D, 8.0D, 20.0D, 0);
    public static Checkbox onlyBow = new Checkbox("Bow Only", true);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(range);
        addSetting(onlyBow);
    }

    EntityPlayer aimTarget = null;
    Rotation aimbotRotation = null;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (!(InventoryUtil.getHeldItem(Items.BOW)) && !mc.player.isHandActive() && !(mc.player.getItemInUseMaxCount() >= 3) && onlyBow.getValue())
            return;

        aimTarget = WorldUtil.getClosestPlayer(range.getValue());

        if (aimTarget != null && (!FriendManager.isFriend(aimTarget.getName()) && FriendManager.isFriendModuleEnabled())) {
            switch (mode.getValue()) {
                case 0:
                    aimbotRotation = new Rotation(RotationUtil.getAngles(aimTarget)[0], RotationUtil.getAngles(aimTarget)[1], Rotation.RotationMode.Legit);
                    break;
                case 1:
                    aimbotRotation = new Rotation(RotationUtil.getAngles(aimTarget)[0], RotationUtil.getAngles(aimTarget)[1], Rotation.RotationMode.Packet);
                    break;
            }

            RotationManager.rotationQueue.add(aimbotRotation);
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}

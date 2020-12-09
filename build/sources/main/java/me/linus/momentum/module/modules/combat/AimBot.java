package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.combat.CrystalUtil;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.util.math.Vec3d;

public class AimBot extends Module {
    public AimBot() {
        super("AimBot", Category.COMBAT, "Automatically rotates to nearby entities");
    }

    private static Mode mode = new Mode("Mode", "Rotate", "Spoof");
    public static Slider range = new Slider("Range", 0.0D, 8.0D, 20.0D, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(range);
    }

    @Override
    public void onUpdate() {
        if (this.mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && this.mc.player.isHandActive() && this.mc.player.getItemInUseMaxCount() >= 3) {
            EntityPlayer target = EntityUtil.getClosestPlayer(range.getValue());

            if (target != null) {
                Vec3d pos = EntityUtil.interpolateEntityTime(target, this.mc.getRenderPartialTicks());
                float[] angles = MathUtil.calcAngle(EntityUtil.interpolateEntityTime(this.mc.player, this.mc.getRenderPartialTicks()), pos);

                if (mode.getValue() == 0) {
                    mc.player.rotationYaw = angles[0];
                    mc.player.rotationPitch = angles[1];
                }

                if (mode.getValue() == 1)
                    CrystalUtil.lookAtPacket(pos.x, pos.y, pos.z, mc.player);
            }
        }
    }

    @Override
    public String getHUDData() {
        return mode.getMode(mode.getValue());
    }
}

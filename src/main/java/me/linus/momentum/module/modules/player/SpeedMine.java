package me.linus.momentum.module.modules.player;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.world.BlockUtil;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class SpeedMine extends Module {
    public SpeedMine() {
        super("SpeedMine", Category.PLAYER, "Allows you to mine faster");
    }

    public static Mode mode = new Mode("Mode", "Packet", "Damage", "Creative", "Instant", "Delay", "Vanilla");

    @Override
    public void setup() {
        addSetting(mode);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (!this.isEnabled())
            return;

        if (mode.getValue() == 3)
            mc.player.addPotionEffect(new PotionEffect(new PotionEffect(MobEffects.HASTE, 80950, 1, false, false)));

        if (BlockUtil.canBreak(mc.objectMouseOver.getBlockPos()) && mc.objectMouseOver.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
            switch (mode.getValue()) {
                case 0:
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), mc.objectMouseOver.sideHit));
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), mc.objectMouseOver.sideHit));
                    break;
                case 1:
                    if (mc.playerController.curBlockDamageMP >= 0.5f)
                        mc.playerController.curBlockDamageMP = 1.0f;
                    break;
                case 2:
                    mc.playerController.curBlockDamageMP = 1.0f;
                    break;
                case 3:
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), mc.objectMouseOver.sideHit));
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), mc.objectMouseOver.sideHit));
                    mc.playerController.onPlayerDestroyBlock(mc.objectMouseOver.getBlockPos());
                    mc.world.setBlockToAir(mc.objectMouseOver.getBlockPos());
                    break;
                case 4:
                    mc.playerController.blockHitDelay = 0;
                    break;
            }
        }
    }

    @Override
    public void onDisable() {
        if (mode.getValue() == 3)
            mc.player.removePotionEffect(MobEffects.HASTE);
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}

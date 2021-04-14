package me.linus.momentum.module.modules.combat;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.world.Timer;
import me.linus.momentum.util.world.Timer.Format;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class Criticals extends Module {
    public Criticals() {
        super("Criticals", Category.COMBAT, "Makes every attack a critical");
    }

    public static Mode mode = new Mode("Mode", "Packet", "Strict", "Jump", "MiniJump");

    public static Checkbox pause = new Checkbox("Pause", true);
    public static SubCheckbox crystalTarget = new SubCheckbox(pause, "Crystal", false);
    public static SubCheckbox auraNotEnabled = new SubCheckbox(pause, "Aura Only", false);
    public static SubCheckbox whenFalling = new SubCheckbox(pause, "When Falling", true);
    public static SubCheckbox waterPause = new SubCheckbox(pause, "When in Liquid", true);

    Timer critTimer = new Timer();

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(pause);
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            if (((CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && mc.player.onGround && ((CPacketUseEntity) event.getPacket()).getEntityFromWorld(mc.world) != null) {
                if (mc.player.fallDistance > 4 && whenFalling.getValue())
                    return;

                if ((mc.player.isInLava() || mc.player.isInWater()) && waterPause.getValue())
                    return;

                if (auraNotEnabled.getValue() && !ModuleManager.getModuleByName("Aura").isEnabled())
                    return;

                if (((CPacketUseEntity) event.getPacket()).getEntityFromWorld(mc.world) instanceof EntityEnderCrystal && crystalTarget.getValue())
                    return;

                switch (mode.getValue()) {
                    case 0:
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.10000000149011612, mc.player.posZ, false));
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                        mc.player.connection.sendPacket(new CPacketPlayer());
                        mc.player.onCriticalHit(Objects.requireNonNull(((CPacketUseEntity) event.getPacket()).getEntityFromWorld(mc.world)));
                        break;
                    case 2:
                        if (critTimer.passed(1000, Format.System)) {
                            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.11, mc.player.posZ, false));
                            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1100013579, mc.player.posZ, false));
                            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.3579E-6, mc.player.posZ, false));
                            mc.player.connection.sendPacket(new CPacketPlayer());
                            mc.player.onCriticalHit(Objects.requireNonNull(((CPacketUseEntity) event.getPacket()).getEntityFromWorld(mc.world)));
                            critTimer.reset();
                        }

                        break;
                    case 8:
                        mc.player.jump();
                        break;
                    case 9:
                        mc.player.jump();
                        mc.player.motionY /= 2;
                        break;
                }
            }
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}

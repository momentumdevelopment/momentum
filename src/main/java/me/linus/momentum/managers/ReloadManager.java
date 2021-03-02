package me.linus.momentum.managers;

import me.linus.momentum.managers.notification.Notification;
import me.linus.momentum.managers.notification.Notification.Type;
import me.linus.momentum.managers.notification.NotificationManager;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.Module;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Reloads all modules when a player joins a new world
 *
 * @author linustouchtips
 * @since 02/10/2021
 */

public class ReloadManager implements MixinInterface {
    public ReloadManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    List<Module> enabledModules = new ArrayList<>();
    boolean reloaded = false;
    boolean guiTip = false;

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (mc.player == null)
            return;

        if (!reloaded) {
            if (mc.player.ticksExisted == 20) {
                if (!Files.exists(Paths.get("momentum/")) && !guiTip) {
                    NotificationManager.addNotification(new Notification("The Default GUI bind is comma (,)", Type.Info));
                    NotificationManager.addNotification(new Notification("The Default Console bind is semicolon (;)", Type.Info));
                    NotificationManager.addNotification(new Notification("The Default HUD bind is period (.)", Type.Info));
                    guiTip = true;
                }

                for (Module module : ModuleManager.getModules()) {
                    if (module.isEnabled()) {
                        enabledModules.add(module);
                        module.disable();
                    }
                }
            }

            else if (mc.player.ticksExisted == 25) {
                for (Module module : enabledModules) {
                    module.enable();
                }

                reloaded = true;
            }
        }
    }
}

package me.linus.momentum.mixin.mixins;

import com.mojang.authlib.GameProfile;
import me.linus.momentum.event.MomentumEvent;
import me.linus.momentum.event.events.player.MoveEvent;
import me.linus.momentum.event.events.player.PushEvent;
import me.linus.momentum.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.MoverType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityPlayerSP.class/*, priority = 634756347*/)
public class MixinEntityPlayerSP extends AbstractClientPlayer {
    public MixinEntityPlayerSP(final World worldIn, final GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
    public void move(final AbstractClientPlayer player, final MoverType moverType, final double x, final double y, final double z) {
        final MoveEvent event = new MoveEvent(moverType, x, y, z);
        MinecraftForge.EVENT_BUS.post(event);

        if (!event.isCanceled())
            super.move(event.getType(), event.getX(), event.getY(), event.getZ());
    }

    @Inject(method = { "pushOutOfBlocks" }, at = { @At("HEAD") }, cancellable = true)
    private void pushOutOfBlocksHook(final double x, final double y, final double z, final CallbackInfoReturnable<Boolean> info) {
        final PushEvent event = new PushEvent();
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            info.setReturnValue(false);
        }
    }

    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;closeScreen()V"))
    public void closeScreen(EntityPlayerSP entityPlayerSP) {
        if (ModuleManager.getModuleByName("Portal").isEnabled())
            return;
    }

    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"))
    public void closeScreen(Minecraft minecraft, GuiScreen screen) {
        if (ModuleManager.getModuleByName("Portal").isEnabled())
            return;
    }
}

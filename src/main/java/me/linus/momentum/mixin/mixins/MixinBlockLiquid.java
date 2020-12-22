package me.linus.momentum.mixin.mixins;

import me.linus.momentum.event.events.world.LiquidCollisionEvent;
import me.linus.momentum.module.ModuleManager;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

@Mixin(BlockLiquid.class)
public class MixinBlockLiquid {

    @Inject(method = "canCollideCheck", at = @At("HEAD"), cancellable = true)
    public void canCollideCheck(final IBlockState blockState, final boolean b, final CallbackInfoReturnable<Boolean> callbackInfoReturnable){
        callbackInfoReturnable.setReturnValue(ModuleManager.getModuleByName("LiquidInteract").isEnabled() || (b && (int) blockState.getValue((IProperty) BlockLiquid.LEVEL) == 0));
    }

    @Inject(method = "getCollisionBoundingBox", at = @At("HEAD"), cancellable = true)
    public void getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, final CallbackInfoReturnable<AxisAlignedBB> callbackInfoReturnable) {
        LiquidCollisionEvent event = new LiquidCollisionEvent(pos);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            callbackInfoReturnable.setReturnValue(event.getBoundingBox());
            callbackInfoReturnable.cancel();
        }
    }
}

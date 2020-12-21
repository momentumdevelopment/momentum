package me.linus.momentum.module.modules.player;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class EntityMine extends Module {
    public EntityMine() {
        super("EntityMine", Category.PLAYER, "Allows you to mine through entities");
    }

    public static Checkbox noReset = new Checkbox("No Reset", true);

    @Override
    public void setup() {
        addSetting(noReset);
    }

    private boolean focus = false;

    @Override
    public void onUpdate() {
        mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityLivingBase).filter(entity -> mc.player == entity).map(entity -> (EntityLivingBase) entity).filter(entity -> !(entity.isDead)).forEach(this::processHit);
        RayTraceResult normalResult = mc.objectMouseOver;

        if (normalResult != null)
            focus = normalResult.typeOfHit == RayTraceResult.Type.ENTITY;
    }

    private void processHit(EntityLivingBase event) {
        RayTraceResult bypassResult = event.rayTrace(6, mc.getRenderPartialTicks());

        if (bypassResult != null && focus) {
            if (bypassResult.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos pos = bypassResult.getBlockPos();

                if (mc.gameSettings.keyBindAttack.isKeyDown())
                    mc.playerController.onPlayerDamageBlock(pos, EnumFacing.UP);
            }
        }
    }
}

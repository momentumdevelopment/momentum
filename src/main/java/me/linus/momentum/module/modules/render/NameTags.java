package me.linus.momentum.module.modules.render;

import me.linus.momentum.event.events.render.Render3DEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.combat.EnemyUtil;
import me.linus.momentum.util.render.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;

public class NameTags extends Module {
    public NameTags() {
        super("NameTags", Category.RENDER, "Draws useful information at player's heads");
    }

    public static Slider range = new Slider("Range", 0.0D, 7.0D, 10.0D, 0);

    @Override
    public void setup() {
        addSetting(range);
    }

    @Override
    public void onRender3D(Render3DEvent renderEvent) {
        for (Object o : mc.world.playerEntities) {
            final Entity entity = (Entity) o;
            if (entity instanceof EntityPlayer && entity != mc.player && entity.isEntityAlive() && entity.getDistance(mc.player) <= range.getValue()) {
                Vec3d vectorEntity = vectorEntity(entity);
                drawNametag((EntityPlayer) entity, vectorEntity.x, vectorEntity.y, vectorEntity.z);
            }
        }
    }

    public void drawNametag(EntityPlayer entityPlayer, double start, double distance, double end) {
        double tempY = distance;
        tempY += (entityPlayer.isSneaking() ? 0.5 : 0.7);

        RenderUtil.drawNametag(start, tempY + 1.4, end, generateNameTag(entityPlayer), new Color(0, 0, 0, 85), 2);
    }

    public static Vec3d vectorEntity(Entity entity) {
        return new Vec3d(interpolatePartialTick(entity.posX, entity.lastTickPosX), interpolatePartialTick(entity.posY, entity.lastTickPosY), interpolatePartialTick(entity.posZ, entity.lastTickPosZ));
    }

    public static double interpolatePartialTick(double start, double end) {
        return end + (start - end) * mc.timer.renderPartialTicks;
    }

    public String[] generateNameTag(EntityPlayer entityPlayer) {
        return new String[] {
                generateName(entityPlayer) + generateGamemode(entityPlayer) + generatePing(entityPlayer) + TextFormatting.GREEN + generateHealth(entityPlayer)
        };
    }

    public String generateHealth(EntityPlayer entityPlayer) {
        return String.valueOf(EnemyUtil.getHealth(entityPlayer));
    }

    public String generateGamemode(EntityPlayer entityPlayer) {
        if (entityPlayer.isCreative())
            return "[C]";
        if (entityPlayer.isSpectator())
            return "[I]";
        else
            return "[S]";
    }

    public String generatePing(EntityPlayer entityPlayer) {
        if (!mc.isSingleplayer())
            return String.valueOf(mc.getConnection().getPlayerInfo(entityPlayer.getUniqueID()).getResponseTime());
        else
            return "-1";
    }

    public String generateName(EntityPlayer entityPlayer) {
        return entityPlayer.getName();
    }
}

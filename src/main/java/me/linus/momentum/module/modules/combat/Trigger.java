package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.client.Timer;
import me.linus.momentum.util.client.friend.FriendManager;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class Trigger extends Module {
    public Trigger() {
        super("Trigger", Category.COMBAT, "Attacks entities in your crosshair");
    }

    Timer timer = new Timer();

    public static Slider attackSpeed = new Slider("Attack Delay", 0.0D, 0.0D, 10.0D, 0);
    private static final Checkbox players = new Checkbox("Players", true);
    private static final Checkbox animals = new Checkbox("Animals", false);
    private static final Checkbox mobs = new Checkbox("Mobs", false);

    @Override
    public void setup() {
        addSetting(attackSpeed);
        addSetting(players);
        addSetting(animals);
        addSetting(mobs);
    }

    @Override
    public void onUpdate() {
        if (mc.objectMouseOver.typeOfHit.equals(RayTraceResult.Type.ENTITY) && mc.objectMouseOver.entityHit instanceof EntityPlayer && players.getValue() && !(FriendManager.isFriend(mc.objectMouseOver.entityHit.getName()) && FriendManager.isFriendModuleEnabled())) {
            if (timer.passed((long) attackSpeed.getValue() * 100)) {
                mc.playerController.attackEntity(mc.player, mc.objectMouseOver.entityHit);
            } timer.reset();
        } if (mc.objectMouseOver.typeOfHit.equals(RayTraceResult.Type.ENTITY) && mc.objectMouseOver.entityHit instanceof EntityMob && mobs.getValue()) {
            if (timer.passed(((long) attackSpeed.getValue() * 100))) {
                mc.playerController.attackEntity(mc.player, mc.objectMouseOver.entityHit);
            } timer.reset();
        } if (mc.objectMouseOver.typeOfHit.equals(RayTraceResult.Type.ENTITY) && mc.objectMouseOver.entityHit instanceof EntityAnimal && animals.getValue()) {
            if (timer.passed(((long) attackSpeed.getValue() * 100))) {
                mc.playerController.attackEntity(mc.player, mc.objectMouseOver.entityHit);
            } timer.reset();
        }
    }
}

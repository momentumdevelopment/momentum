package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.SubMode;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.MessageUtil;
import me.linus.momentum.util.client.Timer;
import me.linus.momentum.util.client.friend.FriendManager;
import me.linus.momentum.util.combat.BedUtil;
import me.linus.momentum.util.combat.EnemyUtil;
import me.linus.momentum.util.world.EntityUtil;
import me.linus.momentum.util.world.InventoryUtil;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linustouchtips
 * @since 12/20/2020
 */

public class AutoBed extends Module {
    public AutoBed() {
        super("AutoBed", Category.COMBAT, "Automatically places and explodes beds");
    }

    public static Checkbox explode = new Checkbox("Break", true);
    public static SubSlider breakDelay = new SubSlider(explode, "Break Delay", 0.0D, 20.0D, 60.0D, 0);
    public static SubSlider breakRange = new SubSlider(explode, "Break Range", 0.0D, 5.0D, 7.0D, 1);

    public static Checkbox place = new Checkbox("Place", true);
    public static SubSlider placeDelay = new SubSlider(place, "Place Delay", 0.0D, 40.0D, 600.0D, 0);
    public static SubSlider placeRange = new SubSlider(place, "Place Range", 0.0D, 5.0D, 7.0D, 1);
    public static SubSlider enemyRange = new SubSlider(place, "Enemy Range", 0.0D, 5.0D, 15.0D, 1);
    public static SubCheckbox autoSwitch = new SubCheckbox(place, "Auto-Switch", false);
    public static SubCheckbox rotate = new SubCheckbox(place, "Rotate", true);

    public static Checkbox pause = new Checkbox("Pause", true);
    public static SubSlider pauseHealth = new SubSlider(pause, "Pause Health", 0.0D, 7.0D, 36.0D, 0);
    public static SubCheckbox whenOverworld = new SubCheckbox(pause, "Nether Check", true);

    public static Checkbox logic = new Checkbox("Logic", true);
    public static SubMode logicMode = new SubMode(logic, "Logic", "Break -> Place", "Place -> Break");

    @Override
    public void setup() {
        addSetting(explode);
        addSetting(place);
        addSetting(pause);
        addSetting(logic);
    }

    Timer breakTimer = new Timer();
    Timer placeTimer = new Timer();
    Entity currentTarget;
    BlockPos currentBlock;
    double diffXZ;
    float rotVar;
    boolean nowTop = false;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.dimension == 0 & whenOverworld.getValue()) {
            MessageUtil.sendClientMessage("You are not in the nether!");
            return;
        }

        currentTarget = EntityUtil.getClosestPlayer(enemyRange.getValue());
        diffXZ = mc.player.getPositionVector().distanceTo(currentTarget.getPositionVector());

        switch (logicMode.getValue()) {
            case 0:
                breakBed();
                placeBed();
                break;
            case 1:
                placeBed();
                breakBed();
                break;
        }
    }

    public void breakBed() {
        TileEntity bed = mc.world.loadedTileEntityList.stream().filter(e -> e instanceof TileEntityBed).filter(e -> mc.player.getDistance(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ()) <= breakRange.getValue()).sorted(Comparator.comparing(e -> mc.player.getDistance(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ()))).findFirst().orElse(null);

        if (bed != null && breakTimer.passed((long) breakDelay.getValue())) {
            if (pause.getValue() && PlayerUtil.getHealth() <= pauseHealth.getValue())
                return;

            if (explode.getValue())
                BedUtil.attackBed(bed.getPos());
        }

        breakTimer.reset();
    }

    public void placeBed() {
        if (!(diffXZ <= placeRange.getValue()))
            return;

        if (autoSwitch.getValue())
            mc.player.inventory.currentItem = InventoryUtil.getBlockInHotbar(Blocks.BED);

        Entity entity = null;
        List<Entity> entities = new ArrayList<>();
        entities.addAll(mc.world.playerEntities.stream().collect(Collectors.toList()));

        for (Entity entityTarget : entities) {
            if (entityTarget != mc.player) {
                if (EnemyUtil.getHealth((EntityPlayer) entityTarget) <= 0)
                    return;

                if (mc.player.getDistanceSq(entityTarget) > enemyRange.getValue() * enemyRange.getValue())
                    continue;

                if (FriendManager.isFriend(entityTarget.getName()) && FriendManager.isFriendModuleEnabled())
                    continue;

                entity = entityTarget;
            }
        }

        currentTarget = entity;
        currentBlock = BedUtil.getBedPosition((EntityPlayer) currentTarget, nowTop, rotVar);

        if (place.getValue() && placeTimer.passed((long) placeDelay.getValue()))
            BedUtil.placeBed(currentBlock, EnumFacing.DOWN, rotVar, nowTop, rotate.getValue());
    }
}

package me.linus.momentum.module.modules.combat;

import me.linus.momentum.managers.RotationManager;
import me.linus.momentum.managers.notification.Notification;
import me.linus.momentum.managers.notification.NotificationManager;
import me.linus.momentum.managers.social.friend.FriendManager;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.mode.SubMode;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.combat.BedUtil;
import me.linus.momentum.util.combat.EnemyUtil;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.player.PlayerUtil;
import me.linus.momentum.util.player.rotation.Rotation;
import me.linus.momentum.util.player.rotation.RotationPriority;
import me.linus.momentum.util.player.rotation.RotationUtil;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.render.builder.RenderBuilder;
import me.linus.momentum.util.world.Timer;
import me.linus.momentum.util.world.Timer.Format;
import me.linus.momentum.util.world.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBed;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author linustouchtips & bon
 * @since 12/20/2020
 * idk if it works well, prob not :P - linus
 */

// TODO: fix placements & render
public class AutoBed extends Module {
    public AutoBed() {
        super("AutoBed", Category.COMBAT, "Automatically places and explodes beds");
    }

    public static Checkbox explode = new Checkbox("Break", true);
    public static SubSlider breakDelay = new SubSlider(explode, "Break Delay", 0.0D, 20.0D, 60.0D, 0);
    public static SubSlider breakRange = new SubSlider(explode, "Break Range", 0.0D, 5.0D, 7.0D, 1);
    public static SubCheckbox unload = new SubCheckbox(explode, "Unload Bed", false);
    public static SubMode rotate = new SubMode(explode, "Rotate", "Packet", "Legit", "None");

    public static Checkbox place = new Checkbox("Place", true);
    public static SubMode placeTimerMode = new SubMode(place, "Delay Mode", "Custom", "Ticks");
    public static SubSlider placeTickDelay = new SubSlider(place, "Tick Delay", 0.0D, 3.0D, 50.0D, 0);
    public static SubSlider placeDelay = new SubSlider(place, "Place Delay", 0.0D, 1000.0D, 2000.0D, 0);
    public static SubSlider placeRange = new SubSlider(place, "Place Range", 0.0D, 5.0D, 7.0D, 1);
    public static SubSlider enemyRange = new SubSlider(place, "Enemy Range", 0.0D, 5.0D, 15.0D, 1);
    public static SubCheckbox autoSwitch = new SubCheckbox(place, "Auto-Switch", false);
    public static SubCheckbox spoofAngles = new SubCheckbox(place, "Spoof Angles", true);

    public static Checkbox pause = new Checkbox("Pause", true);
    public static SubSlider pauseHealth = new SubSlider(pause, "Pause Health", 0.0D, 7.0D, 36.0D, 0);
    public static SubCheckbox whenOverworld = new SubCheckbox(pause, "Nether Check", true);

    public static Checkbox logic = new Checkbox("Logic", true);
    public static SubMode logicMode = new SubMode(logic, "Logic", "Break -> Place", "Place -> Break");

    public static Checkbox renderBed = new Checkbox("Render", true);
    public static ColorPicker colorPicker = new ColorPicker(renderBed, "Color Picker", new Color(255, 0, 0, 55));

    @Override
    public void setup() {
        addSetting(explode);
        addSetting(place);
        addSetting(pause);
        addSetting(logic);
        addSetting(renderBed);
    }

    Timer breakTimer = new Timer();
    Timer placeTimer = new Timer();
    Entity currentTarget = null;
    Rotation bedRotation = null;
    BlockPos currentBlock = null;
    double diffXZ;
    float rotVar;
    boolean nowTop = false;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.dimension == 0 & whenOverworld.getValue()) {
            NotificationManager.addNotification(new Notification("You are not in the nether!", Notification.Type.Warning));
            return;
        }

        currentTarget = WorldUtil.getClosestPlayer(enemyRange.getValue());
        diffXZ = mc.player.getPositionVector().distanceTo(currentTarget.getPositionVector());

        if (!InventoryUtil.getHeldItem(Items.BED))
            bedRotation.restoreRotation();

        if (currentTarget != null && (!FriendManager.isFriend(currentTarget.getName()) && FriendManager.isFriendModuleEnabled())) {
            switch (rotate.getValue()) {
                case 0:
                    bedRotation = new Rotation(RotationUtil.getAngles(currentTarget)[0], RotationUtil.getAngles(currentTarget)[1], Rotation.RotationMode.Packet, RotationPriority.Highest);
                    break;
                case 1:
                    bedRotation = new Rotation(RotationUtil.getAngles(currentTarget)[0], RotationUtil.getAngles(currentTarget)[1], Rotation.RotationMode.Legit, RotationPriority.Highest);
                    break;
            }

            RotationManager.rotationQueue.add(bedRotation);
        }

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
        TileEntityBed bed = (TileEntityBed) mc.world.loadedTileEntityList.stream().filter(e -> e instanceof TileEntityBed).filter(e -> mc.player.getDistance(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ()) <= breakRange.getValue()).min(Comparator.comparing(e -> mc.player.getDistance(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ()))).orElse(null);

        if (bed != null && breakTimer.passed((long) breakDelay.getValue(), Format.System)) {
            if (pause.getValue() && PlayerUtil.getHealth() <= pauseHealth.getValue())
                return;

            if (explode.getValue())
                BedUtil.attackBed(bed.getPos());

            if (unload.getValue()) {
                mc.world.removeAllEntities();
                mc.world.getLoadedEntityList();
            }
        }

        breakTimer.reset();
    }

    public void placeBed() {
        if (autoSwitch.getValue())
            InventoryUtil.switchToSlot(InventoryUtil.getHotbarItemSlot(Items.BED));

        if (diffXZ >= placeRange.getValue())
            return;

        if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemBed))
            return;

        Entity entity = null;
        List<Entity> entities = new ArrayList<>(new ArrayList<>(mc.world.playerEntities));

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

        if (currentTarget != null)
            currentBlock = BedUtil.getBedPosition((EntityPlayer) currentTarget);

        if (place.getValue()) {
            switch (placeTimerMode.getValue()) {
                case 0:
                    if (placeTimer.passed((long) (750 + placeDelay.getValue()), Format.System))
                        BedUtil.placeBed(currentBlock, EnumFacing.DOWN, rotVar, spoofAngles.getValue());
                    break;
                case 1:
                    if (placeTimer.passed((long) placeTickDelay.getValue(), Format.Ticks))
                        BedUtil.placeBed(currentBlock, EnumFacing.DOWN, rotVar, spoofAngles.getValue());
                    break;
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (currentBlock != null && currentTarget != null && renderBed.getValue()) {
            RenderUtil.drawBoxBlockPos(currentBlock, -0.5, colorPicker.getColor(), RenderBuilder.RenderMode.Outline);
            RenderUtil.drawBoxBlockPos(new BlockPos(currentBlock.x + 1, currentBlock.y, currentBlock.z), -0.5, colorPicker.getColor(), RenderBuilder.RenderMode.Outline);
        }
    }

    @Override
    public String getHUDData() {
        return (currentTarget != null) ? " " + currentTarget.getName() : " None";
    }
}

package me.linus.momentum.module.modules.bot;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalBlock;
import me.linus.momentum.module.Module;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.combat.Aura;
import me.linus.momentum.util.world.BlockUtil;
import me.linus.momentum.util.world.HoleUtil;
import me.linus.momentum.util.player.PlayerUtil;
import me.linus.momentum.util.world.WorldUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemSword;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linustouchtips
 * @since 12/09/2020
 * massive wip
 */

public class Milo extends Module {
    public Milo() {
        super("Milo", Category.BOT, "A bot made for anarchy, named after my dog");
    }

    private boolean lookingForHoles = true;
    EntityPlayer targetPlayer;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        targetPlayer = WorldUtil.getClosestPlayer(20);

        if (!playerCheck())
            return;

        baritoneIntoHole();
        autoTrapTarget();
        swordFagTarget();
    }

    private void swordFagTarget() {
        if (mc.player.getDistance(targetPlayer) < Aura.range.getValue() && targetPlayer.getHeldItemMainhand().getItem() instanceof ItemSword) {
            ModuleManager.getModuleByName("Aura").enable();
        }

        if (mc.player.getDistance(targetPlayer) < Aura.range.getValue() || targetPlayer.getHeldItemMainhand().getItem() instanceof ItemSword) {
            ModuleManager.getModuleByName("Aura").disable();
        }
    }

    private void autoTrapTarget() {
        if (mc.player.getDistance(targetPlayer) < 1) {
            ModuleManager.getModuleByName("AutoTrap").enable();
            lookingForHoles = true;
        }

        if (targetPlayer.getHeldItemMainhand().getItem() instanceof ItemSword && PlayerUtil.getHealth() <= 16) {
            ModuleManager.getModuleByName("AutoTrap").enable();
            lookingForHoles = true;
        }
    }

    private void baritoneIntoHole() {
        if (HoleUtil.isInHole(mc.player)) {
            lookingForHoles = false;
            return;
        }

        BlockPos goalPos = getHoles(20).stream().min(Comparator.comparing(c -> mc.player.getDistanceSq(c))).orElse(null);

        if (goalPos != null && lookingForHoles)
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalBlock(goalPos));

        if (!lookingForHoles)
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalBlock(null));
    }

    public List<BlockPos> getHoles(double range) {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(BlockUtil.getSphere(new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ)), (int) range, (int) range, false, true, 0).stream().filter(HoleUtil::isHole).collect(Collectors.toList()));
        return positions;
    }

    public boolean playerCheck() {
        return targetPlayer != null && (!targetPlayer.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem().equals(Items.AIR));
    }
}
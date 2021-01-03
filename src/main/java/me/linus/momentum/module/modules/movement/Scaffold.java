package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.client.Timer;
import me.linus.momentum.util.player.rotation.RotationUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.List;

/**
 * @author ZimTheDestroyer
 * @since 12/8/20 @ 12:35 PM CST
 */

public class Scaffold extends Module {
    public Scaffold() {
        super("Scaffold", Category.MOVEMENT, "Rapidly places blocks underneath you");
    }

    private static final Mode mode = new Mode("Mode", "Tower", "Static");
    private static final Checkbox swing = new Checkbox("Swing Arm", false);
    private static final Checkbox bSwitch = new Checkbox("Switch", false);
    private static final Checkbox center = new Checkbox("Center", false);
    private static final Checkbox keepY = new Checkbox("KeepYLevel", false);
    private static final Checkbox sprint = new Checkbox("UseSprint", true);
    private static final Checkbox replenishBlocks = new Checkbox("ReplenishBlocks", true);
    private static final Checkbox down = new Checkbox("Down", false);
    private static final Slider expand = new Slider("Expand", 1.0, 1.0, 6.0, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(swing);
        addSetting(bSwitch);
        addSetting(center);
        addSetting(keepY);
        addSetting(sprint);
        addSetting(replenishBlocks);
        addSetting(down);
        addSetting(expand);
    }

    private final List<Block> invalid = Arrays.asList(
            Blocks.ENCHANTING_TABLE, Blocks.FURNACE, Blocks.CARPET, Blocks.CRAFTING_TABLE, Blocks.TRAPPED_CHEST, Blocks.CHEST, Blocks.DISPENSER,
            Blocks.AIR, Blocks.WATER, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.FLOWING_LAVA,
            Blocks.SNOW_LAYER, Blocks.TORCH, Blocks.ANVIL, Blocks.JUKEBOX, Blocks.STONE_BUTTON, Blocks.WOODEN_BUTTON, Blocks.LEVER,
            Blocks.NOTEBLOCK, Blocks.STONE_PRESSURE_PLATE, Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE,
            Blocks.WOODEN_PRESSURE_PLATE, Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM,
            Blocks.YELLOW_FLOWER, Blocks.RED_FLOWER, Blocks.ANVIL, Blocks.CACTUS, Blocks.LADDER, Blocks.ENDER_CHEST
    );

    private final Timer timerMotion = new Timer();
    private final Timer itemTimer = new Timer();
    private BlockData blockData;
    private int lastY;
    private BlockPos pos;
    private boolean teleported;

    @Override
    public void onUpdate() {
        if (ModuleManager.getModuleByClass(Sprint.class).isEnabled()) {
            if ((down.getValue() && mc.gameSettings.keyBindSneak.isKeyDown()) || !sprint.getValue()){
                mc.player.setSprinting(false);
                ModuleManager.getModuleByClass(Sprint.class).toggle();
            }
        }

        if (replenishBlocks.getValue() && !(mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBlock) && getBlockCountHotbar() <= 0 && itemTimer.passed(100L)) {
            for (int i = 9; i < 45; ++i) {
                if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                    final ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
                    if (is.getItem() instanceof ItemBlock && !invalid.contains(Block.getBlockFromItem(is.getItem()))) {
                        if (i < 36) {
                            swap(getItemSlot(mc.player.inventoryContainer, is.getItem()), 44);
                        }
                    }
                }
            }
        }

        if (keepY.getValue()) {
            if ((!isMoving(mc.player) && mc.gameSettings.keyBindJump.isKeyDown()) || mc.player.collidedVertically || mc.player.onGround)
                lastY = MathHelper.floor(mc.player.posY);
        }

        else
            lastY = MathHelper.floor(mc.player.posY);

        blockData = null;
        double x = mc.player.posX;
        double z = mc.player.posZ;
        double y = keepY.getValue() ? lastY : mc.player.posY;
        double forward = mc.player.movementInput.moveForward;
        double strafe = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;

        if (!mc.player.collidedHorizontally){
            double[] coords = getExpandCoords(x,z,forward,strafe,yaw);
            x = coords[0];
            z = coords[1];
        }

        if (canPlace(mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - (mc.gameSettings.keyBindSneak.isKeyDown() && down.getValue() ? 2 : 1), mc.player.posZ)).getBlock())) {
            x = mc.player.posX;
            z = mc.player.posZ;
        }

        BlockPos blockBelow = new BlockPos(x, y-1, z);
        if (mc.gameSettings.keyBindSneak.isKeyDown() && down.getValue()) {
            blockBelow = new BlockPos(x, y - 2, z);
        }

        pos = blockBelow;
        if (mc.world.getBlockState(blockBelow).getBlock() == Blocks.AIR) {
            blockData = getBlockData2(blockBelow);
        }

        if (blockData != null) {
            if (getBlockCountHotbar() <= 0 || !bSwitch.getValue() && !(mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock)) {
                return;
            }

            final int heldItem = mc.player.inventory.currentItem;
            if (bSwitch.getValue()) {
                for (int j = 0; j < 9; ++j) {
                    mc.player.inventory.getStackInSlot(j);
                    if (mc.player.inventory.getStackInSlot(j).getCount() != 0 && mc.player.inventory.getStackInSlot(j).getItem() instanceof ItemBlock && !invalid.contains(((ItemBlock) mc.player.inventory.getStackInSlot(j).getItem()).getBlock())) {
                        mc.player.inventory.currentItem = j;
                        break;
                    }
                }
            }

            if (mode.getValue() == 0) {
                if (mc.gameSettings.keyBindJump.isKeyDown() && mc.player.moveForward == 0.0f && mc.player.moveStrafing == 0.0f && !mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                    if (!teleported && center.getValue()) {
                        teleported = true;
                        BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
                        mc.player.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                    }

                    if (center.getValue() && !teleported)
                        return;

                    mc.player.motionY = 0.42f;
                    mc.player.motionZ = 0;
                    mc.player.motionX = 0;
                    if (timerMotion.sleep(1500L)) {
                        mc.player.motionY = -0.28;
                    }
                }

                else {
                    timerMotion.reset();
                    if (teleported && center.getValue())
                        teleported = false;
                }
            }

            if (mc.playerController.processRightClickBlock(mc.player, mc.world, blockData.position, blockData.face, new Vec3d(blockData.position.getX() + Math.random(), blockData.position.getY() + Math.random(), blockData.position.getZ() + Math.random()), EnumHand.MAIN_HAND) != EnumActionResult.FAIL) {
                if (swing.getValue())
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                else
                    mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
            }

            mc.player.inventory.currentItem = heldItem;
        }
    }

    public static void swap(int slot, int hotbarNum) {
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, hotbarNum, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.updateController();
    }

    public static int getItemSlot(Container container, Item item) {
        int slot = 0;
        for (int i = 9; i < 45; ++i) {
            if (container.getSlot(i).getHasStack()) {
                ItemStack is = container.getSlot(i).getStack();
                if (is.getItem() == item)
                    slot = i;
            }
        }
        return slot;
    }

    public static boolean isMoving(EntityLivingBase entity) {
        return entity.moveForward != 0 || entity.moveStrafing != 0;
    }

    public double[] getExpandCoords(double x, double z, double forward, double strafe, float YAW){
        BlockPos underPos = new BlockPos(x, mc.player.posY - (mc.gameSettings.keyBindSneak.isKeyDown() && down.getValue() ? 2 : 1), z);
        Block underBlock = mc.world.getBlockState(underPos).getBlock();
        double xCalc = -999, zCalc = -999;
        double dist = 0;
        double expandDist = expand.getValue() * 2;
        while(!canPlace(underBlock)){
            xCalc = x;
            zCalc = z;
            dist ++;
            if(dist > expandDist){
                dist = expandDist;
            }
            xCalc += (forward * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f))) * dist;
            zCalc += (forward * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f))) * dist;
            if(dist == expandDist){
                break;
            }
            underPos = new BlockPos(xCalc, mc.player.posY - (mc.gameSettings.keyBindSneak.isKeyDown() && down.getValue() ? 2 : 1), zCalc);
            underBlock = mc.world.getBlockState(underPos).getBlock();
        }
        return new double[]{xCalc,zCalc};
    }

    public boolean canPlace(Block block) {
        return (block instanceof BlockAir || block instanceof BlockLiquid) && mc.world != null && mc.player != null && pos != null &&mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos)).isEmpty();
    }

    private int getBlockCountHotbar() {
        int blockCount = 0;
        for (int i = 36; i < 45; ++i) {
            if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
                final Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock) {
                    if (!invalid.contains(((ItemBlock)item).getBlock())) {
                        blockCount += is.getCount();
                    }
                }
            }
        }
        return blockCount;
    }

    // the second most spaghetti code shit i've seen - linus
    private BlockData getBlockData2(final BlockPos pos) {
        if (!invalid.contains(mc.world.getBlockState(pos.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.world.getBlockState(pos.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.world.getBlockState(pos.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.world.getBlockState(pos.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.world.getBlockState(pos.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos.add(0, 1, 0), EnumFacing.DOWN);
        }
        final BlockPos pos2 = pos.add(-1, 0, 0);
        if (!invalid.contains(mc.world.getBlockState(pos2.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.world.getBlockState(pos2.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos2.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.world.getBlockState(pos2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.world.getBlockState(pos2.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.world.getBlockState(pos2.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.world.getBlockState(pos2.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos3 = pos.add(1, 0, 0);
        if (!invalid.contains(mc.world.getBlockState(pos3.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.world.getBlockState(pos3.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos3.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.world.getBlockState(pos3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.world.getBlockState(pos3.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.world.getBlockState(pos3.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.world.getBlockState(pos3.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos4 = pos.add(0, 0, 1);
        if (!invalid.contains(mc.world.getBlockState(pos4.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.world.getBlockState(pos4.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos4.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.world.getBlockState(pos4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.world.getBlockState(pos4.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.world.getBlockState(pos4.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.world.getBlockState(pos4.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos5 = pos.add(0, 0, -1);
        if (!invalid.contains(mc.world.getBlockState(pos5.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.world.getBlockState(pos5.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos5.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.world.getBlockState(pos5.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.world.getBlockState(pos5.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.world.getBlockState(pos5.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.world.getBlockState(pos5.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos6 = pos.add(-2, 0, 0);
        if (!invalid.contains(mc.world.getBlockState(pos2.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.world.getBlockState(pos2.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos2.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.world.getBlockState(pos2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.world.getBlockState(pos2.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.world.getBlockState(pos2.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.world.getBlockState(pos2.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos7 = pos.add(2, 0, 0);
        if (!invalid.contains(mc.world.getBlockState(pos3.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.world.getBlockState(pos3.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos3.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.world.getBlockState(pos3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.world.getBlockState(pos3.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.world.getBlockState(pos3.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.world.getBlockState(pos3.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos8 = pos.add(0, 0, 2);
        if (!invalid.contains(mc.world.getBlockState(pos4.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.world.getBlockState(pos4.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos4.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.world.getBlockState(pos4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.world.getBlockState(pos4.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.world.getBlockState(pos4.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.world.getBlockState(pos4.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos9 = pos.add(0, 0, -2);
        if (!invalid.contains(mc.world.getBlockState(pos5.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.world.getBlockState(pos5.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos5.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.world.getBlockState(pos5.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.world.getBlockState(pos5.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }

        if (!invalid.contains(mc.world.getBlockState(pos5.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }

        if (!invalid.contains(mc.world.getBlockState(pos5.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }

        final BlockPos pos10 = pos.add(0, -1, 0);
        if (!invalid.contains(mc.world.getBlockState(pos10.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos10.add(0, -1, 0), EnumFacing.UP);
        }

        if (!invalid.contains(mc.world.getBlockState(pos10.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos10.add(0, 1, 0), EnumFacing.DOWN);
        }

        if (!invalid.contains(mc.world.getBlockState(pos10.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos10.add(-1, 0, 0), EnumFacing.EAST);
        }

        if (!invalid.contains(mc.world.getBlockState(pos10.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos10.add(1, 0, 0), EnumFacing.WEST);
        }

        if (!invalid.contains(mc.world.getBlockState(pos10.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos10.add(0, 0, 1), EnumFacing.NORTH);
        }

        if (!invalid.contains(mc.world.getBlockState(pos10.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos10.add(0, 0, -1), EnumFacing.SOUTH);
        }

        final BlockPos pos11 = pos10.add(1, 0, 0);
        if (!invalid.contains(mc.world.getBlockState(pos11.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos11.add(0, -1, 0), EnumFacing.UP);
        }

        if (!invalid.contains(mc.world.getBlockState(pos11.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos11.add(0, 1, 0), EnumFacing.DOWN);
        }

        if (!invalid.contains(mc.world.getBlockState(pos11.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos11.add(-1, 0, 0), EnumFacing.EAST);
        }

        if (!invalid.contains(mc.world.getBlockState(pos11.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos11.add(1, 0, 0), EnumFacing.WEST);
        }

        if (!invalid.contains(mc.world.getBlockState(pos11.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos11.add(0, 0, 1), EnumFacing.NORTH);
        }

        if (!invalid.contains(mc.world.getBlockState(pos11.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos11.add(0, 0, -1), EnumFacing.SOUTH);
        }

        final BlockPos pos12 = pos10.add(-1, 0, 0);
        if (!invalid.contains(mc.world.getBlockState(pos12.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos12.add(0, -1, 0), EnumFacing.UP);
        }

        if (!invalid.contains(mc.world.getBlockState(pos12.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos12.add(0, 1, 0), EnumFacing.DOWN);
        }

        if (!invalid.contains(mc.world.getBlockState(pos12.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos12.add(-1, 0, 0), EnumFacing.EAST);
        }

        if (!invalid.contains(mc.world.getBlockState(pos12.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos12.add(1, 0, 0), EnumFacing.WEST);
        }

        if (!invalid.contains(mc.world.getBlockState(pos12.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos12.add(0, 0, 1), EnumFacing.NORTH);
        }

        if (!invalid.contains(mc.world.getBlockState(pos12.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos12.add(0, 0, -1), EnumFacing.SOUTH);
        }

        final BlockPos pos13 = pos10.add(0, 0, 1);
        if (!invalid.contains(mc.world.getBlockState(pos13.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos13.add(0, -1, 0), EnumFacing.UP);
        }

        if (!invalid.contains(mc.world.getBlockState(pos13.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos13.add(-1, 0, 0), EnumFacing.EAST);
        }

        if (!invalid.contains(mc.world.getBlockState(pos13.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos13.add(0, 1, 0), EnumFacing.DOWN);
        }

        if (!invalid.contains(mc.world.getBlockState(pos13.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos13.add(1, 0, 0), EnumFacing.WEST);
        }

        if (!invalid.contains(mc.world.getBlockState(pos13.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos13.add(0, 0, 1), EnumFacing.NORTH);
        }

        if (!invalid.contains(mc.world.getBlockState(pos13.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos13.add(0, 0, -1), EnumFacing.SOUTH);
        }

        final BlockPos pos14 = pos10.add(0, 0, -1);
        if (!invalid.contains(mc.world.getBlockState(pos14.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos14.add(0, -1, 0), EnumFacing.UP);
        }

        if (!invalid.contains(mc.world.getBlockState(pos14.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos14.add(0, 1, 0), EnumFacing.DOWN);
        }

        if (!invalid.contains(mc.world.getBlockState(pos14.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos14.add(-1, 0, 0), EnumFacing.EAST);
        }

        if (!invalid.contains(mc.world.getBlockState(pos14.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos14.add(1, 0, 0), EnumFacing.WEST);
        }

        if (!invalid.contains(mc.world.getBlockState(pos14.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos14.add(0, 0, 1), EnumFacing.NORTH);
        }

        if (!invalid.contains(mc.world.getBlockState(pos14.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos14.add(0, 0, -1), EnumFacing.SOUTH);
        }

        return null;
    }

    private static class BlockData {
        public BlockPos position;
        public EnumFacing face;

        public BlockData(final BlockPos position, final EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }
}

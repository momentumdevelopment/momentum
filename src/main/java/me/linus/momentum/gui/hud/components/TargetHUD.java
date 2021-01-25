package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.render.Render2DUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.Comparator;
import java.util.Objects;

/**
 * @author linustouchtips
 * @since 10/23/2020
 *
 * this is the most spaghetti code shit i've ever seen - linus from the future
 */

// TODO: this desperately needs a rewrite
public class TargetHUD extends HUDComponent {
        public TargetHUD() {
            super("TargetHUD", 400, 160);
            width = 183;
            height = 90;
        }

        String playerinfo;
        TextFormatting playercolor;
        Color healthcolor;
        int healthright;
        int newPopCounter;

        @Override
        public void renderComponent() {
            if (mc.player != null && mc.world != null) {
                GuiScreen.drawRect(this.x, this.y, this.x + 180, this.y + 87, new Color(0, 0, 0, 100).getRGB());

                EntityPlayer e = (EntityPlayer) mc.world.loadedEntityList.stream()
                        .filter(entity -> IsValidEntity(entity))
                        .map(entity -> (EntityLivingBase) entity)
                        .min(Comparator.comparing(c -> mc.player.getDistance(c)))
                        .orElse(null);

                if (e == null)
                    return;

                GlStateManager.disableRescaleNormal();
                GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
                GlStateManager.disableTexture2D();
                GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

                Render2DUtil.drawEntityOnScreen(this.x + 27, this.y + 71, 30, this.x + 32,  this.x + 70, e);

                GlStateManager.enableRescaleNormal();
                GlStateManager.enableTexture2D();
                GlStateManager.enableBlend();

                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);


                if (e.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA) {
                    playerinfo = "Wasp";
                    playercolor = TextFormatting.LIGHT_PURPLE;
                }

                if (e.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.DIAMOND_CHESTPLATE) {
                    playerinfo = "Threat";
                    playercolor = TextFormatting.RED;
                }

                if (e.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.AIR) {
                    playerinfo = "NewFag";
                    playercolor = TextFormatting.GREEN;
                } else {
                    playerinfo = "None";
                    playercolor = TextFormatting.WHITE;
                }


                if (Math.rint(e.getHealth() + e.getAbsorptionAmount()) <= 5) {
                    healthcolor = new Color(235, 64, 52, 255);
                } if (Math.rint(e.getHealth() + e.getAbsorptionAmount()) <= 10) {
                    healthcolor = new Color(235, 137, 52, 255);
                } if (Math.rint(e.getHealth() + e.getAbsorptionAmount()) <= 15) {
                    healthcolor = new Color(227, 214, 102, 255);
                } if (Math.rint(e.getHealth() + e.getAbsorptionAmount()) <= 20) {
                    healthcolor = new Color(152, 227, 102, 255);
                } else {
                    healthcolor = new Color(79, 196, 82, 255);
                }

                // my braindead fix for the health rect for now
                // holy fuck how was i so retarded - linus from the future
                if (e.getHealth() + e.getAbsorptionAmount() == 0) {healthright = 54;}
                if (e.getHealth() + e.getAbsorptionAmount() == 1) {healthright = 58;}
                if (e.getHealth() + e.getAbsorptionAmount() == 2) {healthright = 61;}
                if (e.getHealth() + e.getAbsorptionAmount() == 3) {healthright = 64;}
                if (e.getHealth() + e.getAbsorptionAmount() == 4) {healthright = 68;}
                if (e.getHealth() + e.getAbsorptionAmount() == 5) {healthright = 71;}
                if (e.getHealth() + e.getAbsorptionAmount() == 6) {healthright = 74;}
                if (e.getHealth() + e.getAbsorptionAmount() == 7) {healthright = 78;}
                if (e.getHealth() + e.getAbsorptionAmount() == 8) {healthright = 81;}
                if (e.getHealth() + e.getAbsorptionAmount() == 9) {healthright = 84;}
                if (e.getHealth() + e.getAbsorptionAmount() == 10) {healthright = 88;}
                if (e.getHealth() + e.getAbsorptionAmount() == 11) {healthright = 91;}
                if (e.getHealth() + e.getAbsorptionAmount() == 12) {healthright = 94;}
                if (e.getHealth() + e.getAbsorptionAmount() == 13) {healthright = 98;}
                if (e.getHealth() + e.getAbsorptionAmount() == 14) {healthright = 101;}
                if (e.getHealth() + e.getAbsorptionAmount() == 15) {healthright = 104;}
                if (e.getHealth() + e.getAbsorptionAmount() == 16) {healthright = 108;}
                if (e.getHealth() + e.getAbsorptionAmount() == 17) {healthright = 111;}
                if (e.getHealth() + e.getAbsorptionAmount() == 18) {healthright = 114;}
                if (e.getHealth() + e.getAbsorptionAmount() == 19) {healthright = 118;}
                if (e.getHealth() + e.getAbsorptionAmount() == 20) {healthright = 121;}
                if (e.getHealth() + e.getAbsorptionAmount() == 21) {healthright = 124;}
                if (e.getHealth() + e.getAbsorptionAmount() == 22) {healthright = 128;}
                if (e.getHealth() + e.getAbsorptionAmount() == 23) {healthright = 131;}
                if (e.getHealth() + e.getAbsorptionAmount() == 24) {healthright = 134;}
                if (e.getHealth() + e.getAbsorptionAmount() == 25) {healthright = 138;}
                if (e.getHealth() + e.getAbsorptionAmount() == 26) {healthright = 141;}
                if (e.getHealth() + e.getAbsorptionAmount() == 27) {healthright = 144;}
                if (e.getHealth() + e.getAbsorptionAmount() == 28) {healthright = 148;}
                if (e.getHealth() + e.getAbsorptionAmount() == 29) {healthright = 151;}
                if (e.getHealth() + e.getAbsorptionAmount() == 30) {healthright = 154;}
                if (e.getHealth() + e.getAbsorptionAmount() == 31) {healthright = 158;}
                if (e.getHealth() + e.getAbsorptionAmount() == 32) {healthright = 161;}
                if (e.getHealth() + e.getAbsorptionAmount() == 33) {healthright = 164;}
                if (e.getHealth() + e.getAbsorptionAmount() == 34) {healthright = 168;}
                if (e.getHealth() + e.getAbsorptionAmount() == 35) {healthright = 171;}
                if (e.getHealth() + e.getAbsorptionAmount() == 36) {healthright = 174;}

                FontUtil.drawString(e.getName(), this.x + 54, this.y + 8, -1);
                GuiScreen.drawRect(this.x + 54, this.y + 18, this.x + healthright, this.y + 30, healthcolor.getRGB());
                FontUtil.drawString("Health: " + (Math.rint(e.getHealth() + e.getAbsorptionAmount())), this.x + 57, this.y + 20, -1);
                FontUtil.drawString(playercolor + playerinfo + TextFormatting.WHITE + " | " + "Ping: " + getPing(e) + " ms", this.x + 54, this.y + 37, -1);
                if (e.getHeldItemMainhand().item == Items.END_CRYSTAL) {
                    FontUtil.drawString( "Target: Crystalling!", this.x + 54, this.y + 47, -1);
                } if (e.getHeldItemMainhand().item == Items.GOLDEN_APPLE) {
                    FontUtil.drawString( "Target: Eating Gapple!", this.x + 54, this.y + 47, -1);
                } if (e.getHeldItemMainhand().item == Items.DIAMOND_SWORD) {
                    FontUtil.drawString("Target: Swording!", this.x + 54, this.y + 47, -1);
                } if (e.getHeldItemMainhand().item == Items.POTIONITEM || e.getHeldItemOffhand().item == Items.POTIONITEM) {
                    FontUtil.drawString("Target: Drinking Potion!", this.x + 54, this.y + 47, -1);
                } if (e.getHeldItemMainhand().item == Items.ENDER_PEARL) {
                    FontUtil.drawString("Target: Using Pearl!", this.x + 54, this.y + 47, -1);
                } if (e.getHeldItemMainhand().item == Items.DIAMOND_PICKAXE) {
                    FontUtil.drawString("Target: Attempting to City!", this.x + 54, this.y + 47, -1);
                } if (e.getHeldItemMainhand().canPlaceOn(Blocks.OBSIDIAN)) {
                    FontUtil.drawString("Target: Surrounding!", this.x + 54, this.y + 47, -1);
                }

                FontUtil.drawString("Totems Pops: " + newPopCounter, this.x + 54, this.y + 77, -1);

                GlStateManager.enableTexture2D();
                int iteration = 0;
                for (ItemStack is : e.inventory.armorInventory) {
                    ++iteration;
                    if (is.isEmpty()) continue;
                    int x = this.x + ((9 - iteration) * 14) - 17;
                    mc.getRenderItem().zLevel = 200.0f;
                    mc.getRenderItem().renderItemAndEffectIntoGUI(is, x, this.y + 58);
                    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, is, x, this.y + 58, "");
                    mc.getRenderItem().zLevel = 0.0f;
                }

                mc.getRenderItem().renderItemAndEffectIntoGUI(e.itemStackMainHand, this.x + 114, this.y + 58);
                mc.getRenderItem().renderItemAndEffectIntoGUI(e.getHeldItemOffhand(), this.x + 130, this.y + 58);
            }
            
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }

        private boolean IsValidEntity (Entity e){
            if (!(e instanceof EntityPlayer)) {
                return false;
            }

            if (e instanceof EntityPlayer) {
                return e != mc.player;
            }

            return true;
        }

        public int getPing (EntityPlayer player){
            int ping = 0;
            try {
                ping = (int) MathUtil.clamp((float) Objects.requireNonNull(mc.getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime(), 1, 300.0f);
            } catch (NullPointerException ignored) {

            }
            return ping;
        }
}

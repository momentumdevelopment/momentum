package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.module.modules.hud.TargetHUDModule;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
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

public class TargetHUD extends HUDComponent<TargetHUDModule> {
        public TargetHUD() {
            super("TargetHUD", 400, 160, TargetHUDModule.INSTANCE);
            width = 183;
            height = 90;
        }

        public final RenderItem itemRender = mc.getRenderItem();

        String playerinfo;
        TextFormatting playercolor;
        Color healthcolor;
        int healthright;
        int newPopCounter;

        @Override
        public void render() {
            if (mc.player != null && mc.world != null) {
                GuiScreen.drawRect(Momentum.componentManager.getComponentByName("TargetHUD").getX(), Momentum.componentManager.getComponentByName("TargetHUD").getY(), Momentum.componentManager.getComponentByName("TargetHUD").getX() + 180, Momentum.componentManager.getComponentByName("TargetHUD").getY() + 87, new Color(0, 0, 0, 100).getRGB());

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

                drawEntityOnScreen(Momentum.componentManager.getComponentByName("TargetHUD").getX() + 27, Momentum.componentManager.getComponentByName("TargetHUD").getY() + 71, 30, Momentum.componentManager.getComponentByName("TargetHUD").getX() + 32,  Momentum.componentManager.getComponentByName("TargetHUD").getX() + 70, e);

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

                FontUtil.drawStringWithShadow(e.getName(), Momentum.componentManager.getComponentByName("TargetHUD").getX() + 54, Momentum.componentManager.getComponentByName("TargetHUD").getY() + 8, new Color(255, 255, 255).getRGB());
                GuiScreen.drawRect(Momentum.componentManager.getComponentByName("TargetHUD").getX() + 54, Momentum.componentManager.getComponentByName("TargetHUD").getY() + 18, Momentum.componentManager.getComponentByName("TargetHUD").getX() + healthright, Momentum.componentManager.getComponentByName("TargetHUD").getY() + 30, healthcolor.getRGB());
                FontUtil.drawStringWithShadow("Health: " + (Math.rint(e.getHealth() + e.getAbsorptionAmount())), Momentum.componentManager.getComponentByName("TargetHUD").getX() + 57, Momentum.componentManager.getComponentByName("TargetHUD").getY() + 20, new Color(255, 255, 255).getRGB());
                FontUtil.drawStringWithShadow(playercolor + playerinfo + TextFormatting.WHITE + " | " + "Ping: " + getPing(e) + " ms", Momentum.componentManager.getComponentByName("TargetHUD").getX() + 54, Momentum.componentManager.getComponentByName("TargetHUD").getY() + 37, new Color(255, 255, 255).getRGB());
                if (e.getHeldItemMainhand().item == Items.END_CRYSTAL) {
                    FontUtil.drawStringWithShadow( "Target: Crystalling!", Momentum.componentManager.getComponentByName("TargetHUD").getX() + 54, Momentum.componentManager.getComponentByName("TargetHUD").getY() + 47, new Color(255, 255, 255).getRGB());
                } if (e.getHeldItemMainhand().item == Items.GOLDEN_APPLE) {
                    FontUtil.drawStringWithShadow( "Target: Eating Gapple!", Momentum.componentManager.getComponentByName("TargetHUD").getX() + 54, Momentum.componentManager.getComponentByName("TargetHUD").getY() + 47, new Color(255, 255, 255).getRGB());
                } if (e.getHeldItemMainhand().item == Items.DIAMOND_SWORD) {
                    FontUtil.drawStringWithShadow("Target: Swording!", Momentum.componentManager.getComponentByName("TargetHUD").getX() + 54, Momentum.componentManager.getComponentByName("TargetHUD").getY() + 47, new Color(255, 255, 255).getRGB());
                } if (e.getHeldItemMainhand().item == Items.POTIONITEM || e.getHeldItemOffhand().item == Items.POTIONITEM) {
                    FontUtil.drawStringWithShadow("Target: Drinking Potion!", Momentum.componentManager.getComponentByName("TargetHUD").getX() + 54, Momentum.componentManager.getComponentByName("TargetHUD").getY() + 47, new Color(255, 255, 255).getRGB());
                } if (e.getHeldItemMainhand().item == Items.ENDER_PEARL) {
                    FontUtil.drawStringWithShadow("Target: Using Pearl!", Momentum.componentManager.getComponentByName("TargetHUD").getX() + 54, Momentum.componentManager.getComponentByName("TargetHUD").getY() + 47, new Color(255, 255, 255).getRGB());
                } if (e.getHeldItemMainhand().item == Items.DIAMOND_PICKAXE) {
                    FontUtil.drawStringWithShadow("Target: Attempting to City!", Momentum.componentManager.getComponentByName("TargetHUD").getX() + 54, Momentum.componentManager.getComponentByName("TargetHUD").getY() + 47, new Color(255, 255, 255).getRGB());
                } if (e.getHeldItemMainhand().canPlaceOn(Blocks.OBSIDIAN)) {
                    FontUtil.drawStringWithShadow("Target: Surrounding!", Momentum.componentManager.getComponentByName("TargetHUD").getX() + 54, Momentum.componentManager.getComponentByName("TargetHUD").getY() + 47, new Color(255, 255, 255).getRGB());
                }

                FontUtil.drawStringWithShadow("Totems Pops: " + newPopCounter, Momentum.componentManager.getComponentByName("TargetHUD").getX() + 54, Momentum.componentManager.getComponentByName("TargetHUD").getY() + 77, new Color(255, 255, 255).getRGB());

                GlStateManager.enableTexture2D();
                int iteration = 0;
                for (ItemStack is : e.inventory.armorInventory) {
                    ++iteration;
                    if (is.isEmpty()) continue;
                    int x = Momentum.componentManager.getComponentByName("TargetHUD").getX() + ((9 - iteration) * 14) - 17;
                    itemRender.zLevel = 200.0f;
                    itemRender.renderItemAndEffectIntoGUI(is, x, Momentum.componentManager.getComponentByName("TargetHUD").getY() + 58);
                    itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x, Momentum.componentManager.getComponentByName("TargetHUD").getY() + 58, "");
                    itemRender.zLevel = 0.0f;
                }

                ItemStack mainheld = e.itemStackMainHand;
                ItemStack offheld = e.getHeldItemOffhand();
                itemRender.renderItemAndEffectIntoGUI(mainheld, Momentum.componentManager.getComponentByName("TargetHUD").getX() + 114, Momentum.componentManager.getComponentByName("TargetHUD").getY() + 58);
                itemRender.renderItemAndEffectIntoGUI(offheld, Momentum.componentManager.getComponentByName("TargetHUD").getX() + 130, Momentum.componentManager.getComponentByName("TargetHUD").getY() + 58);
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }

        private boolean IsValidEntity (Entity e){
            if (!(e instanceof EntityPlayer)) {
                return false;
            }

            if (e instanceof EntityPlayer) {
                if (e == mc.player) {
                    return false;
                }
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

        public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent) {
            GlStateManager.enableColorMaterial();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)posX, (float)posY, 50.0F);
            GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
            GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
            RenderHelper.enableStandardItemLighting();
            GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.translate(0.0F, 0.0F, 0.0F);
            RenderManager rendermanager = mc.getRenderManager();
            rendermanager.setPlayerViewY(180.0F);
            rendermanager.setRenderShadow(false);
            rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
            rendermanager.setRenderShadow(true);
            GlStateManager.popMatrix();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.disableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        }

        @Override
        public void mouseHovered(int mouseX, int mouseY) {
            if (isMouseOnComponent(mouseX, mouseY)) colors = new Color(82, 81, 77, 125).getRGB();
            else colors = new Color(117, 116, 110, 125).getRGB();
        }

        public boolean isMouseOnComponent(int x, int y) {
            if (x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height) {
                return true;
            }
            return false;
        }
}

package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.util.combat.EnemyUtil;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.EntityUtil;
import me.linus.momentum.util.world.RotationUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class NameTags extends Module {
    public NameTags() {
        super("NameTags", Category.RENDER, "Draws useful information at player'subString heads");
    }

    private static Checkbox health = new Checkbox("Health", true);
    private static Checkbox ping = new Checkbox("Ping", true);
    private static Checkbox gamemode = new Checkbox("GameMode", false);

    private static Checkbox onlyInViewFrustrum = new Checkbox("View Frustrum", true);

    @Override
    public void setup() {
        addSetting(health);
        addSetting(ping);
        addSetting(gamemode);
        addSetting(onlyInViewFrustrum);
    }

    List<EntityPlayer> nametagEntities = new ArrayList<>();

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (nullCheck())
            return;

        if (mc.renderEngine == null || mc.getRenderManager() == null || mc.getRenderManager().options == null)
            return;

        mc.world.playerEntities.stream().filter(entityPlayer -> mc.player != entityPlayer).forEach(entityPlayer -> {
            RenderUtil.camera.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);

            if (!RotationUtil.isInViewFrustrum(entityPlayer) && onlyInViewFrustrum.getValue())
                return;

            nametagEntities.add(entityPlayer);
        });

        nametagEntities.sort((p1, p2) -> Double.compare(p2.getDistance(mc.getRenderViewEntity()), p1.getDistance(mc.getRenderViewEntity())));
        nametagEntities.stream().forEach(nametagEntity -> {
            Vec3d pos = EntityUtil.interpolateEntityByTicks(mc.getRenderViewEntity(), eventRender.getPartialTicks());

            double posX = mc.getRenderViewEntity().posX;
            double posY = mc.getRenderViewEntity().posY;
            double posZ = mc.getRenderViewEntity().posZ;

            mc.getRenderViewEntity().posX = pos.x;
            mc.getRenderViewEntity().posY = pos.y;
            mc.getRenderViewEntity().posZ = pos.z;

            double scale = 0.04;

            if (mc.getRenderViewEntity().getDistance(EntityUtil.interpolateEntityByTicks(nametagEntity, eventRender.getPartialTicks()).x, EntityUtil.interpolateEntityByTicks(nametagEntity, eventRender.getPartialTicks()).y + 0.65, EntityUtil.interpolateEntityByTicks(nametagEntity, eventRender.getPartialTicks()).z) > 0.0)
                scale = 0.02 + (3 / 1000) * mc.getRenderViewEntity().getDistance(EntityUtil.interpolateEntityByTicks(nametagEntity, eventRender.getPartialTicks()).x, EntityUtil.interpolateEntityByTicks(nametagEntity, eventRender.getPartialTicks()).y + 0.65, EntityUtil.interpolateEntityByTicks(nametagEntity, eventRender.getPartialTicks()).z);

            GlStateManager.pushMatrix();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
            GlStateManager.disableLighting();
            GlStateManager.translate((float) EntityUtil.interpolateEntityByTicks(nametagEntity, eventRender.getPartialTicks()).x, (float) EntityUtil.interpolateEntityByTicks(nametagEntity, eventRender.getPartialTicks()).y + 0.65 + (nametagEntity.isSneaking() ? 0.0 : 0.08f) + 1.4f, (float)EntityUtil.interpolateEntityByTicks(nametagEntity, eventRender.getPartialTicks()).z);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0, 1, 0);
            GlStateManager.rotate(mc.getRenderManager().playerViewX, (mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, (float) 0);
            GlStateManager.scale(-scale, -scale, scale);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();

            GuiScreen.drawRect((int) -(FontUtil.getStringWidth(generateNameTag(nametagEntity)) / 2)  / 2 - 1, (int) -(FontUtil.getFontHeight() + 1), (int) (FontUtil.getStringWidth(generateNameTag(nametagEntity)) / 2)  + 2, 2, new Color(0, 0, 0, 90).getRGB());
            GlStateManager.disableBlend();
            FontUtil.drawStringWithShadow(generateNameTag(nametagEntity), -(FontUtil.getStringWidth(generateNameTag(nametagEntity)) / 2) + 1, -FontUtil.getFontHeight() + 3, -1);

            GlStateManager.pushMatrix();

            Iterator<ItemStack> items = nametagEntity.getArmorInventoryList().iterator();
            ArrayList<ItemStack> stacks = new ArrayList<>();

            stacks.add(nametagEntity.getHeldItemOffhand());

            while (items.hasNext()) {
                ItemStack stack = items.next();

                if (!stack.isEmpty())
                    stacks.add(stack);
            }

            stacks.add(nametagEntity.getHeldItemMainhand());

            Collections.reverse(stacks);

            int x = (int) -(FontUtil.getStringWidth(generateNameTag(nametagEntity)) / 2) ;

            for (ItemStack stack : stacks) {
                RenderItemStack(stack, x, -32, 0);
                renderItemEnchantments(stack, x, -62);
                x += 16;
            }

            GlStateManager.popMatrix();

            GlStateManager.enableDepth();
            GlStateManager.disableBlend();
            GlStateManager.disablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
            GlStateManager.popMatrix();

            mc.getRenderViewEntity().posX = posX;
            mc.getRenderViewEntity().posY = posY;
            mc.getRenderViewEntity().posZ = posZ;
        });
    }

    private String getEnchantName(Enchantment enchantment, int x) {
        if (enchantment.getTranslatedName(x).contains("Vanish"))
            return TextFormatting.RED + "Van";
        
        if (enchantment.getTranslatedName(x).contains("Bind"))
            return TextFormatting.RED + "Bind";

        String substring = enchantment.getTranslatedName(x);
   
        if (substring.length() > ((x > 1) ? 2 : 3))
            substring = substring.substring(0, (x > 1) ? 2 : 3);
        
        StringBuilder stringBuilder = new StringBuilder();
        String subString = substring;
        String completedString = stringBuilder.insert(0, subString.substring(0, 1).toUpperCase()).append(substring.substring(1)).toString();
        if (x > 1)
            completedString = new StringBuilder().insert(0, completedString).append(x).toString();
        
        return completedString;
    }

    private void renderItemEnchantments(ItemStack itemStack, int x, int y) {
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        Iterator<Enchantment> iteratorReturned;
        Iterator<Enchantment> iterator = iteratorReturned = EnchantmentHelper.getEnchantments(itemStack).keySet().iterator();
        while (iterator.hasNext()) {
            Enchantment enchantment;
            if ((enchantment = iteratorReturned.next()) == null)
                iterator = iteratorReturned;
            else {
                FontUtil.drawStringWithShadow(getEnchantName(enchantment, EnchantmentHelper.getEnchantmentLevel(enchantment, itemStack)), (float) (x * 2), (float) y, -1);

                y += 8;
                iterator = iteratorReturned;
            }
        }
        
        if (itemStack.getItem().equals(Items.GOLDEN_APPLE) && itemStack.hasEffect())
            FontUtil.drawStringWithShadow(TextFormatting.DARK_RED + "God", (float) (x * 2), (float) y, -1);
        
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
    }

    private void RenderItemStack(ItemStack itemStack, int x, int y, int z) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y + ((z > 4) ? ((z - 4) * 8 / 2) : 0));
        mc.getRenderItem().renderItemOverlays(mc.fontRenderer, itemStack, x,y + ((z > 4) ? ((z - 4) * 8 / 2) : 0));
        mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2, 2, 2);
        GlStateManager.popMatrix();
    }

    public String generateNameTag(EntityPlayer entityPlayer) {
        return generateName(entityPlayer) + generateGamemode(entityPlayer) + generatePing(entityPlayer) + TextFormatting.GREEN + generateHealth(entityPlayer);
    }

    public String generateHealth(EntityPlayer entityPlayer) {
        return " " + EnemyUtil.getHealth(entityPlayer);
    }

    public String generateGamemode(EntityPlayer entityPlayer) {
        if (entityPlayer.isCreative())
            return " [C]";
        else if (entityPlayer.isSpectator())
            return " [I]";
        else
            return " [S]";
    }

    public String generatePing(EntityPlayer entityPlayer) {
        try {
            if (!mc.isSingleplayer())
                return " " + mc.getConnection().getPlayerInfo(entityPlayer.getUniqueID()).getResponseTime();
            else
                return " -1";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return " -1";
    }

    public String generateName(EntityPlayer entityPlayer) {
        return entityPlayer.getName();
    }
}

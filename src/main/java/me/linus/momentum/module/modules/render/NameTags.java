package me.linus.momentum.module.modules.render;

import me.linus.momentum.Momentum;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.client.color.ColorUtil;
import me.linus.momentum.util.client.friend.FriendManager;
import me.linus.momentum.util.client.system.MathUtil;
import me.linus.momentum.util.combat.EnemyUtil;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
        super("NameTags", Category.RENDER, "Draws useful information at player's heads");
    }

    private static Checkbox health = new Checkbox("Health", true);
    private static Checkbox ping = new Checkbox("Ping", true);
    private static Checkbox gamemode = new Checkbox("GameMode", false);
    private static Checkbox armor = new Checkbox("Armor", true);
    private static Checkbox item = new Checkbox("Items", true);
    private static Checkbox enchants = new Checkbox("Enchants", true);
    private static Checkbox onlyInViewFrustrum = new Checkbox("View Frustrum", true);

    public static Slider scale = new Slider("Scale", 0.0D, 2.0D, 10.0D, 1);
    private static SubCheckbox distanceScale = new SubCheckbox(scale, "Scale By Distance", false);

    @Override
    public void setup() {
        addSetting(health);
        addSetting(ping);
        addSetting(gamemode);
        addSetting(armor);
        addSetting(item);
        addSetting(onlyInViewFrustrum);
        addSetting(scale);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (nullCheck() || mc.renderEngine == null || mc.getRenderManager() == null || mc.getRenderManager().options == null)
            return;

        List<EntityPlayer> nametagEntities = new ArrayList<>();

        mc.world.playerEntities.stream().filter(entity -> entity instanceof EntityPlayer && EntityUtil.isLiving(entity) && entity != mc.getRenderViewEntity()).forEach(e -> {
            RenderUtil.camera.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);

            if (!RenderUtil.camera.isBoundingBoxInFrustum(e.getEntityBoundingBox()) && onlyInViewFrustrum.getValue())
                return;

                nametagEntities.add(e);
        });

        nametagEntities.sort((p1, p2) -> Double.compare(p2.getDistance(mc.getRenderViewEntity()), p1.getDistance(mc.getRenderViewEntity())));
        nametagEntities.stream().forEach(entityPlayer -> {
            final Entity entity2 = mc.getRenderViewEntity();

            Vec3d pos = EntityUtil.interpolateEntityByTicks(entityPlayer, event.getPartialTicks());

            double n = pos.x;
            double distance = pos.y + 0.65;
            double n2 = pos.z;

            final double n3 = distance + (entityPlayer.isSneaking() ? 0.0 : 0.08f);

            pos = EntityUtil.interpolateEntityByTicks(entity2, event.getPartialTicks());

            final double posX = entity2.posX;
            final double posY = entity2.posY;
            final double posZ = entity2.posZ;

            entity2.posX = pos.x;
            entity2.posY = pos.y;
            entity2.posZ = pos.z;

            GlStateManager.pushMatrix();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
            GlStateManager.disableLighting();
            GlStateManager.translate((float) n, (float) n3 + 1.4f, (float) n2);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(mc.getRenderManager().playerViewX, (mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, (float) 0);
            GlStateManager.scale(-(scale.getValue() / 100), -(scale.getValue() / 100), (scale.getValue() / 100));
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();

            String nameTag = generateNameTag(entityPlayer);

            float width = FontUtil.getStringWidth(nameTag) / 2;
            float height = mc.fontRenderer.FONT_HEIGHT;

            GlStateManager.enableBlend();
            GuiScreen.drawRect((int) -width - 1, (int) -(height - 1), (int) width + 2, 3, 0x5F0A0A0A);
            GlStateManager.disableBlend();
            Momentum.fontManager.getCustomFont().drawString(nameTag, -width + 1, -height + 3, -1);

            GlStateManager.pushMatrix();

            if (armor.getValue()) {
                Iterator<ItemStack> items = entityPlayer.getArmorInventoryList().iterator();
                ArrayList<ItemStack> stacks = new ArrayList<>();

                stacks.add(entityPlayer.getHeldItemOffhand());

                while (items.hasNext()) {
                    ItemStack stack = items.next();
                    if (!stack.isEmpty())
                        stacks.add(stack);
                }

                stacks.add(entityPlayer.getHeldItemMainhand());

                Collections.reverse(stacks);

                int x = (int) -width;

                for (ItemStack stack : stacks) {
                    renderItemStack(stack, x, -32, 0);
                    renderItemEnchantments(stack, x, -62);
                    x += 16;
                }

                GlStateManager.popMatrix();
            }

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

    private String getEnchantName(Enchantment enchantment, int translated) {
        if (enchants.getValue()) {
            if (enchantment.getTranslatedName(translated).contains("Vanish"))
                return TextFormatting.RED + "Van";
            if (enchantment.getTranslatedName(translated).contains("Bind"))
                return TextFormatting.RED + "Bind";

            String substring = enchantment.getTranslatedName(translated);
            int n2 = (translated > 1) ? 2 : 3;
            if (substring.length() > n2)
                substring = substring.substring(0, n2);

            StringBuilder sb = new StringBuilder();
            String s = substring;
            String s2 = sb.insert(0, s.substring(0, 1).toUpperCase()).append(substring.substring(1)).toString();
            if (translated > 1)
                s2 = new StringBuilder().insert(0, s2).append(translated).toString();

            return s2;
        }

        return "";
    }

    private void renderItemEnchantments(ItemStack itemStack, int n, int n2) {
        if (enchants.getValue()) {
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
            int n3 = -1;
            Iterator<Enchantment> iterator2;
            Iterator<Enchantment> iterator = iterator2 = EnchantmentHelper.getEnchantments(itemStack).keySet().iterator();
            while (iterator.hasNext()) {
                Enchantment enchantment;
                if ((enchantment = iterator2.next()) == null)
                    iterator = iterator2;

                else {
                    Momentum.fontManager.getCustomFont().drawString(getEnchantName(enchantment, EnchantmentHelper.getEnchantmentLevel(enchantment, itemStack)), (float) (n * 2), (float) n2, n3);

                    n2 += 8;
                    iterator = iterator2;
                }
            }

            if (itemStack.getItem().equals(Items.GOLDEN_APPLE) && itemStack.hasEffect())
                Momentum.fontManager.getCustomFont().drawString(TextFormatting.DARK_RED + "God", (float) (n * 2), (float) n2, -1);

            GlStateManager.scale(2.0f, 2.0f, 2.0f);
        }
    }

    private void renderItemStack(ItemStack itemStack, int x, int y, int scaled) {
        if (item.getValue()) {
            GlStateManager.pushMatrix();
            GlStateManager.depthMask(true);
            GlStateManager.clear(256);
            RenderHelper.enableStandardItemLighting();
            mc.getRenderItem().zLevel = -150.0f;
            GlStateManager.disableAlpha();
            GlStateManager.enableDepth();
            GlStateManager.disableCull();
            int scaledFinal = (scaled > 4) ? ((scaled - 4) * 8 / 2) : 0;
            mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y + scaledFinal);
            mc.getRenderItem().renderItemOverlays(mc.fontRenderer, itemStack, x, y + scaledFinal);
            mc.getRenderItem().zLevel = 0.0f;
            RenderHelper.disableStandardItemLighting();
            GlStateManager.enableCull();
            GlStateManager.enableAlpha();
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
            GlStateManager.disableDepth();
            GlStateManager.enableDepth();
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.popMatrix();
        }
    }

    private String generateNameTag(EntityPlayer entityPlayer) {
        try {
            return generateName(entityPlayer) + generateGamemode(entityPlayer) + ColorUtil.getPingText(mc.getConnection().getPlayerInfo(entityPlayer.getUniqueID()).getResponseTime()) + generatePing(entityPlayer) + ColorUtil.getHealthText(EnemyUtil.getHealth(entityPlayer)) + generateHealth(entityPlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public String generateName(EntityPlayer entityPlayer) {
        if (FriendManager.isFriend(entityPlayer.getName()))
            return TextFormatting.AQUA + entityPlayer.getName() + TextFormatting.RESET;
        else if (entityPlayer.isSneaking())
            return TextFormatting.GOLD + entityPlayer.getName() + TextFormatting.RESET;
        else
            return entityPlayer.getName();
    }

    public String generateHealth(EntityPlayer entityPlayer) {
        return health.getValue() ? " " + MathUtil.roundAvoid(EnemyUtil.getHealth(entityPlayer), 1) : "";
    }

    public String generateGamemode(EntityPlayer entityPlayer) {
        if (gamemode.getValue()) {
            if (entityPlayer.isCreative())
                return " [C]";
            else if (entityPlayer.isSpectator())
                return " [I]";
            else
                return " [S]";
        }

        else
            return " ";
    }

    public String generatePing(EntityPlayer entityPlayer) {
        try {
            if (!mc.isSingleplayer())
                return ping.getValue() ? " " + mc.getConnection().getPlayerInfo(entityPlayer.getUniqueID()).getResponseTime() + "ms" : "";
            else
                return ping.getValue() ? " -1ms" : "";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return " ";
    }
}

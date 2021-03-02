package me.linus.momentum.gui.window.windows;

import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.gui.window.Window;
import me.linus.momentum.managers.social.friend.FriendManager;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.render.GUIUtil;
import me.linus.momentum.util.render.Render2DUtil;
import me.linus.momentum.util.render.builder.Render2DBuilder;
import me.linus.momentum.util.render.builder.Render2DBuilder.Render2DMode;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class SocialWindow extends Window {
    public SocialWindow() {
        super("Social Manager", 300, 100, 200, 200, new ResourceLocation("momentum:social.png"));
    }

    public int scrollbar = 0;

    @Override
    public void drawWindowBase() {
        Render2DUtil.drawRect(x - 2, y + 14, x + this.width + 2, y + this.height + 30, 1, new Color(36, 36, 36, 60).getRGB(), ThemeColor.COLOR, false, Render2DBuilder.Render2DMode.Both);
    }

    @Override
    public void drawWindow() {
        Render2DUtil.drawRect(x - 2, y + 14, x + this.width + 2, y + 29 + this.height, 1,  new Color(36, 36, 36, 60).getRGB(), ThemeColor.COLOR, false, Render2DBuilder.Render2DMode.Both);

        Render2DUtil.drawRect(x + this.width - 9, y + 14 + 2, x + this.width - 1, y + 27 + this.height, 1,  new Color(36, 36, 36, 70).getRGB(), new Color(0, 0, 0, 70).getRGB(), false, Render2DBuilder.Render2DMode.Both);
        Render2DUtil.drawRect(x + this.width - 9, (int) MathUtil.clamp(this.y + 16 + MathUtil.clamp(this.scrollbar, 0, 216), y + 14, y + 12 + this.height), x + this.width - 1, (int) MathUtil.clamp(this.y + 31 + MathUtil.clamp(this.scrollbar, 0, 216), y + 14, y + 12 + this.height), 1, ThemeColor.COLOR, -1, false, Render2DMode.Normal);

        int playerOffset = 0;

        for (EntityPlayer player : mc.world.playerEntities) {
            int friendColor = 0xCC232323;
            if (GUIUtil.mouseOver(x + this.width - 41, (int) (y + 14 + 1 + (16 * playerOffset) + MathUtil.clamp(this.scrollbar, 0, 10000)), x + this.width - 5, (int) (y + 14 + 18 + (16 * playerOffset) + MathUtil.clamp(this.scrollbar, 0, 10000)))) {
                friendColor = 0xCC383838;

                if (GUIUtil.ldown) {
                    if (FriendManager.isFriend(player.getName()))
                        FriendManager.removeFriend(player.getName());

                    else
                        FriendManager.addFriend(player.getName());
                }
            }

            Render2DBuilder.prepareScissor(x, y + 14, x + this.width - 9, y + 29 + this.height);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            Render2DBuilder.translate(0, (int) MathUtil.clamp(this.scrollbar, -10000, 0));

            Render2DUtil.drawRect(x + 1, y + 14 + 1 + (16 * playerOffset), x + this.width - 5, y + 14 + 18 + (16 * playerOffset), 1, new Color(18, 18, 18, 90).getRGB(), new Color(0, 0, 0, 90).getRGB(), false, Render2DBuilder.Render2DMode.Both);
            FontUtil.drawString(player.getName(), x + 23, y + 14 + 6 + (16 * playerOffset), FriendManager.isFriend(player.getName()) ? new Color(85, 231, 215).getRGB() : -1);

            try {
                ResourceLocation resourceLocation = AbstractClientPlayer.getLocationSkin(player.getName());
                getDownloadImageSkin(resourceLocation, player.getName());
                mc.getTextureManager().bindTexture(resourceLocation);
                GlStateManager.enableTexture2D();
                GL11.glColor4f(1F, 1F, 1F, 1F);
                GuiScreen.drawScaledCustomSizeModalRect(x + 3, y + 14 + 2 + (16 * playerOffset), 8, 8, 8, 8, 16, 16, 64, 64);
            } catch (Exception ignored) {

            }

            Render2DUtil.drawRect(x + this.width - 41, y + 14 + 1 + (16 * playerOffset), x + this.width - 5, y + 14 + 18 + (16 * playerOffset), 0, FriendManager.isFriend(player.getName()) ? ThemeColor.COLOR : friendColor, -1, false, Render2DBuilder.Render2DMode.Normal);
            FontUtil.drawString("Friend", x + this.width - 38, y + 14 + 4 + (16 * playerOffset) + 1, -1);

            Render2DBuilder.translate(0, (int) -MathUtil.clamp(this.scrollbar, -10000, 0));
            GL11.glDisable(GL11.GL_SCISSOR_TEST);

            playerOffset++;
        }
    }

    @Override
    public void mouseWheelListen() {
        int scrollWheel = Mouse.getDWheel();

        if (GUIUtil.mouseOver(this.x, this.y, this.x + this.width, this.y + this.height)) {
            if (scrollWheel < 0)
                this.scrollbar += 11;
            else if (scrollWheel > 0)
                this.scrollbar -= 11;
        }
    }

    public ThreadDownloadImageData getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
        mc.getTextureManager().getTexture(resourceLocationIn);
        ThreadDownloadImageData textureObject = new ThreadDownloadImageData(null, String.format("https://minotar.net/avatar/%s/64.png", StringUtils.stripControlCodes(username)), DefaultPlayerSkin.getDefaultSkin(AbstractClientPlayer.getOfflineUUID(username)), new ImageBufferDownload());
        mc.getTextureManager().loadTexture(resourceLocationIn, textureObject);
        return textureObject;
    }
}

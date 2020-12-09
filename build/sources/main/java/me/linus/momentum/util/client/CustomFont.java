package me.linus.momentum.util.client;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.StringUtils;

public class CustomFont implements MixinInterface {
	
	private int scaleFactor = new ScaledResolution(mc).getScaleFactor();
	private static final Pattern colorPattern = Pattern.compile("§[0123456789abcdefklmnor]");
	public final int height = 9;
	private final Map<String, Float> stringWidthMap = new HashMap<>();
	private UnicodeFont font;
	private final String name;
	private final float size;
	private float aAFactor;

	public CustomFont(String name, float size) {
		this.name = name;
		this.size = size;
		ScaledResolution sr = new ScaledResolution(mc);
		try {
			scaleFactor = sr.getScaleFactor();
			font = new UnicodeFont(getFontByName(name).deriveFont(size * scaleFactor / 2));
		    font.addAsciiGlyphs();
		    font.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
		    font.loadGlyphs();
		} catch(Exception e) {
			e.printStackTrace();
		}
		this.aAFactor = sr.getScaleFactor();
	}
	
	private Font getFontByName(String name) throws IOException, FontFormatException {
		return getFontFromInput("/assets/momentum/fonts/" + name + ".ttf");
	}
	
	private Font getFontFromInput(String path) throws IOException, FontFormatException {
	    return Font.createFont(Font.TRUETYPE_FONT, CustomFont.class.getResourceAsStream(path));
	  }
	
	public int drawString(String text, float x, float y, int color) {
		if (text == null) {
		      return 0;
		    }

		    ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

		    try {
		      if (resolution.getScaleFactor() != scaleFactor) {
		        scaleFactor = resolution.getScaleFactor();
		        font = new UnicodeFont(getFontByName(name).deriveFont(size * scaleFactor / 2));
		        font.addAsciiGlyphs();
		        font.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
		        font.loadGlyphs();
		      }
		    } catch (Exception e) {
		      e.printStackTrace();
		    }

		    this.aAFactor = resolution.getScaleFactor();

		    GlStateManager.pushMatrix();
		    GlStateManager.scale(1 / aAFactor, 1 / aAFactor, 1 / aAFactor);
		    x *= aAFactor;
		    y *= aAFactor;
		    float originalX = x;
		    float red = (float) (color >> 16 & 255) / 255.0F;
		    float green = (float) (color >> 8 & 255) / 255.0F;
		    float blue = (float) (color & 255) / 255.0F;
		    float alpha = (float) (color >> 24 & 255) / 255.0F;
		    GlStateManager.color(red, green, blue, alpha);

		    char[] characters = text.toCharArray();

		    GlStateManager.disableLighting();
		    GlStateManager.enableBlend();
		    GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
		    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		    String[] parts = colorPattern.split(text);
		    int index = 0;
		    for (String s : parts) {
		      for (String s2 : s.split("\n")) {
		        for (String s3 : s2.split("\r")) {
		          font.drawString(x, y, s3, new org.newdawn.slick.Color(color));
		          x += font.getWidth(s3);

		          index += s3.length();
		          if (index < characters.length && characters[index] == '\r') {
		            x = originalX;
		            index++;
		          }
		        }
		        if (index < characters.length && characters[index] == '\n') {
		          x = originalX;
		          y += getHeight(s2) * 2;
		          index++;
		        }
		      }
		    }

		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.popMatrix();
		return (int) x;
	}
	
	public int drawStringWithShadow(String text, float x, float y, int color) {
	    drawString(StringUtils.stripControlCodes(text), x + 0.5F, y + 0.5F, 0x000000);
	    return drawString(text, x, y, color);
	  }
	
	public int drawString(String text, float x, float y, int color, boolean shadow) {
		if(shadow) {
			drawStringWithShadow(text, x, y, color);
		} else {
			drawString(text, x, y, color);
		}
		return drawString(text, x, y, color);
	}
	
	public float getHeight(String s) {
	    return font.getHeight(s) / 2.0F;
	  }
	
	public float getStringWidth(String text)
    {   
        return font.getWidth(text) / 2;
    }
	
}

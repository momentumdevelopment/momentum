package me.linus.momentum.gui.window.windows;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.gui.window.Window;
import me.linus.momentum.managers.CommandManager;
import me.linus.momentum.module.modules.client.Colors;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.render.GUIUtil;
import me.linus.momentum.util.render.Render2DUtil;
import me.linus.momentum.util.render.builder.Render2DBuilder;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConsoleWindow extends Window {
    public ConsoleWindow() {
        super("Console", 100, 100, 305, 200, new ResourceLocation("momentum:console.png"));
    }

    public static String typedCharacters = "";
    public static List<String> outputs = new ArrayList<>();

    public int scrollbar = 0;
    
    @Override
    public void drawWindowBase() {
        Render2DUtil.drawRect(x - 2, y + 14, x + this.width + 2, y + 30 + this.height, 1, new Color(36, 36, 36, 60).getRGB(), ThemeColor.COLOR, false, Render2DBuilder.Render2DMode.Both);
        Render2DUtil.drawRect(x, y + 14 + this.height, x + this.width - 28, y + 28 + this.height, 1, new Color(18, 18, 18, 90).getRGB(), new Color(0, 0, 0, 90).getRGB(), false, Render2DBuilder.Render2DMode.Both);
    }
    
    @Override
    public void drawWindow() {
        int enterColor = ThemeColor.COLOR;
        if (GUIUtil.mouseOver(x + this.width - 27, y + 14 + this.height, x + this.width - 1, y + 28 + this.height)) {
            enterColor = new Color(Colors.clientPicker.getColor().getRed(), Colors.clientPicker.getColor().getGreen(), Colors.clientPicker.getColor().getBlue(), Colors.clientPicker.getColor().getAlpha() + 15).getRGB();

            if (GUIUtil.ldown)
                resetText();
        }

        Render2DUtil.drawRect(x - 2, y + 14, x + this.width + 2, y + 30 + this.height, 1, new Color(36, 36, 36, 60).getRGB(), ThemeColor.COLOR, false, Render2DBuilder.Render2DMode.Both);
        Render2DUtil.drawRect(x, y + 14 + this.height, x + this.width - 28, y + 28 + this.height, 1, new Color(18, 18, 18, 90).getRGB(), new Color(0, 0, 0, 90).getRGB(), false, Render2DBuilder.Render2DMode.Both);

        Render2DUtil.drawRect(x + this.width - 9, y + 14 + 2, x + this.width - 1, y + 14 + 198, 1, new Color(36, 36, 36, 70).getRGB(), new Color(0, 0, 0, 70).getRGB(), false, Render2DBuilder.Render2DMode.Both);

        Render2DUtil.drawRect(x + this.width - 27, y + 14 + this.height, x + this.width - 1, y + 28 + this.height, 1, enterColor, -1, false, Render2DBuilder.Render2DMode.Normal);
        FontUtil.drawString("Enter", x + this.width - 25, y + 17 + this.height, -1);

        Render2DUtil.drawRect(x + this.width - 9, (int) MathUtil.clamp(y + this.height - 3 + MathUtil.clamp(this.scrollbar, -180, 0), y + 14, y + 28 + this.height), x + this.width - 1, (int) MathUtil.clamp(y + 12 + this.height + MathUtil.clamp(this.scrollbar, -180, 0), y + 14, y + 28 + this.height), 1, ThemeColor.COLOR, -1, false, Render2DBuilder.Render2DMode.Normal);

        int outputLength = 0;

        String[] command = getConsoleLine().split(" ");
        String predictionString = "";

        Collections.reverse(outputs);

        for (String output : outputs) {
            outputLength++;

            Render2DBuilder.prepareScissor(x, y + 14, x + this.width - 9, y + 14 + this.height);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            Render2DBuilder.translate(0, (int) -MathUtil.clamp(this.scrollbar, -(16 * outputLength), 0));
            FontUtil.drawString(output, x + 3, y + 14 + this.height - (12 * outputLength), ThemeColor.BRIGHT);
            Render2DBuilder.translate(0, (int) MathUtil.clamp(this.scrollbar, -(16 * outputLength), 0));
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }

        Collections.reverse(outputs);

        try {
            if ((!CommandManager.predictCommands(command[0]).isEmpty() || CommandManager.predictCommands(command[0]).size() != 0) && typedCharacters.length() > 0)
                predictionString = CommandManager.predictCommands(command[0]).get(0).getUsage() + " " + (command.length < 2 ? CommandManager.predictCommands(command[0]).get(0).getUsageException() : "");
        } catch (ArrayIndexOutOfBoundsException e) {

        }

        FontUtil.drawString(predictionString, x + 3, y + 17 + this.height, ThemeColor.COLOR);
        FontUtil.drawString(getConsoleLine() + (this.isTyping ? FontUtil.insertionPoint() : ""), x + 3, y + 17 + this.height, -1);
    }

    private static void executeCommand(String[] command) {
        try {
            outputs.add(TextFormatting.WHITE + "> " + CommandManager.getCommandByUsage(command[0]).getUsage());

            try {
                CommandManager.getCommandByUsage(command[0]).onCommand(command);
            } catch (Exception e) {
                outputs.add(TextFormatting.RED + "Usage Incorrect!");
            }
        } catch (Exception e) {
            outputs.add(TextFormatting.RED + "Invalid Command!");
        }
    }

    @Override
    public void keyListen(char typedChar, int keyCode) {
        if (isTyping) {
            if (keyCode == Keyboard.KEY_RETURN)
                resetText();

            else {
                String tempText = "";

                if (ChatAllowedCharacters.isAllowedCharacter(typedChar))
                    tempText = typedChar + "";

                else {
                    if (keyCode == Keyboard.KEY_BACK) {
                        if (typedCharacters.length() >= 1)
                            typedCharacters = typedCharacters.substring(0, typedCharacters.length() -1);
                    }
                }

                typedCharacters += tempText;
            }
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

    public static void resetText() {
        executeCommand(typedCharacters.split(" "));
        typedCharacters = "";
    }

    public String getConsoleLine() {
        if (!this.isTyping)
            return ChatFormatting.WHITE + "Please enter a command ...";

        else {
            if (FontUtil.getStringWidth(ChatFormatting.WHITE + typedCharacters) < this.width)
                return typedCharacters;
            else
                resetText();
        }

        return "";
    }
}

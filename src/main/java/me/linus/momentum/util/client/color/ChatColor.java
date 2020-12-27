package me.linus.momentum.util.client.color;

import java.util.regex.Pattern;

/**
 * @author https://github.com/HyperiumClient/Hyperium/blob/mcgradle/src/main/java/cc/hyperium/utils/HyperiumFontRenderer.java
 * @since 12/25/2020
 */

public enum ChatColor {

    BLACK('0'), DARK_BLUE('1'), DARK_GREEN('2'), DARK_AQUA('3'), DARK_RED('4'), DARK_PURPLE('5'), GOLD('6'), GRAY('7'), DARK_GRAY('8'), BLUE('9'), GREEN('a'), AQUA('b'), RED('c'), LIGHT_PURPLE('d'),
    YELLOW('e'), WHITE('f'), MAGIC('k', true), BOLD('l', true), STRIKETHROUGH('m', true), UNDERLINE('n', true), ITALIC('o', true), RESET('r');

    public static final char COLOR_CHAR = '\u00A7';
    private final char code;
    private final boolean isFormat;
    private final String toString;

    ChatColor(char code)
    {
        this(code, false);
    }

    ChatColor(char code, boolean isFormat) {
        this.code = code;
        this.isFormat = isFormat;
        toString = new String(new char[] {
                COLOR_CHAR, code
        });
    }

    public static String stripColor(final String input) {
        return input == null ? null : Pattern.compile("(?i)" + COLOR_CHAR + "[0-9A-FK-OR]").matcher(input).replaceAll("");
    }

    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        int bound = b.length - 1;
        for (int i = 0; i < bound; i++) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = ChatColor.COLOR_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }

        return new String(b);
    }

    public char getChar()
    {
        return code;
    }

    @Override
    public String toString()
    {
        return toString;
    }

    public boolean isFormat()
    {
        return isFormat;
    }

    public boolean isColor()
    {
        return !isFormat && this != RESET;
    }
}

package me.linus.momentum.module.modules.misc;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.util.config.ConfigManager;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author max
 * @since 12/17/2020
 */

public class ChatLogger extends Module {
    public ChatLogger() {
        super("ChatLogger", Category.MISC, "Logs chat to a file");
    }

    public static  Checkbox numbers = new Checkbox("Only Numbers", false);

    @Override
    public void setup() {
        addSetting(numbers);
    }

    File txt;
    File folder;
    BufferedWriter out;

    @Override
    public void onEnable() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
            Date date = new Date();
            folder = new File(ConfigManager.config + File.separator + "logs");
            if (!folder.exists())
                folder.mkdirs();

            String fileName = (formatter.format(date)) + "-chatlogs.txt";

            txt = new File(folder + File.separator + fileName);

            txt.createNewFile();
            out = new BufferedWriter(new FileWriter(txt));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        if (out != null) {
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @SubscribeEvent
    public void onChatRecieved(ClientChatReceivedEvent event) {
        try {
            String message = event.getMessage().getUnformattedText();

            if (numbers.getValue()) {
                if (message.matches(".*\\d.*")) {
                    out.write(message);
                    out.write(endLine());
                    out.flush();
                }
            }

            else {
                out.write(message);
                out.write(endLine());
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String endLine() {
        return "\r\n";
    }
}
package me.linus.momentum.module.modules.misc;

import me.linus.momentum.Momentum;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.util.config.ConfigManager;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatLogger extends Module {
    public File txt;
    File folder;
    BufferedWriter out;
    public static final Checkbox numbers = new Checkbox("OnlyNumbers", false);
    public ChatLogger() {
        super("ChatLogger", Category.MISC, "Logs chat to a file");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        super.onEnable();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
            Date date = new Date();
            folder = new File(ConfigManager.config + File.separator + "logs");
            if (!folder.exists()){
                folder.mkdirs();
            }
            String fileName = (formatter.format(date)) + "-chatlogs.txt";


            txt = new File(folder + File.separator + fileName);

            txt.createNewFile();
            out = new BufferedWriter(new FileWriter(txt));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (out != null){
            try {
                out.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @SubscribeEvent
    public void onChatRecieved(ClientChatReceivedEvent event){
        try {
            String message = event.getMessage().getUnformattedText();
            if (numbers.getValue()){
                if (message.matches(".*\\d.*")){
                    out.write(message);
                    out.write(endLine());
                    out.flush();
                }
            } else {
                out.write(message);
                out.write(endLine());
                out.flush();
            }
        } catch (Exception e){ e.printStackTrace();
        }
    }

    public static String endLine(){
        return "\r\n";
    }

}

package me.linus.momentum.util.config;

import me.linus.momentum.module.Module;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.setting.Setting;
import me.linus.momentum.setting.SubSetting;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.keybind.Keybind;
import me.linus.momentum.setting.keybind.SubKeybind;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.mode.SubMode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import org.lwjgl.input.Keyboard;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author linustouchtips & LittleDraily
 * @since 01/18/2020
 */

public class ConfigManager2 {

    public static File configFolder = new File("momentum");
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void saveConfig() throws IOException {
        saveModule();
    }

    public static void loadConfig() {
        createFolder();
        loadModule();
    }

    public static void createFolder() {
        if (!configFolder.exists())
            configFolder.mkdirs();
    }

    public void createFile(String location, String name) throws IOException {
        if (!Files.exists(Paths.get("momentum" + location + name + ".json")))
            Files.createFile(Paths.get("momentum" + location + name + ".json"));

        else {
            File file = new File("momentum" + location + name + ".json");
            file.delete();
            Files.createFile(Paths.get("momentum" + location +name + ".json"));
        }
    }

    public static void saveModule() throws IOException {
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("momentum" + "module.json"), StandardCharsets.UTF_8);

        JsonObject settingsArray = new JsonObject();
        JsonObject subSettingsArray = new JsonObject();
        JsonObject moduleArray = new JsonObject();
        for (Module module: ModuleManager.getModules()) {
            JsonObject moduleSettingsArray = new JsonObject();
            moduleSettingsArray.addProperty("bind", Keyboard.getKeyName(module.getKeybind().getKeyCode()));
            moduleSettingsArray.addProperty("enabled", module.isEnabled());

            for (Setting setting: module.getSettings()) {
                JsonObject moduleSubSettingsArray = new JsonObject();

                if (setting instanceof Checkbox) {
                    moduleSettingsArray.addProperty(((Checkbox) setting).getName(), ((Checkbox) setting).getValue());

                    for (SubSetting subSetting : ((Checkbox) setting).getSubSettings()) {
                        if (subSetting instanceof SubCheckbox)
                            moduleSubSettingsArray.addProperty(((SubCheckbox) subSetting).getName(), ((SubCheckbox) subSetting).getValue());

                        if (subSetting instanceof SubMode)
                            moduleSubSettingsArray.addProperty(((SubMode) subSetting).getName(), ((SubMode) subSetting).getValue());

                        if (subSetting instanceof SubSlider)
                            moduleSubSettingsArray.addProperty(((SubSlider) subSetting).getName(), ((SubSlider) subSetting).getValue());

                        if (subSetting instanceof SubKeybind)
                            moduleSubSettingsArray.addProperty(((SubKeybind) subSetting).getName(), ((SubKeybind) subSetting).getKey());
                    }
                }

                if (setting instanceof Mode) {
                    moduleSettingsArray.addProperty(((Mode) setting).getName(), ((Mode) setting).getValue());

                    for (SubSetting subSetting : ((Mode) setting).getSubSettings()) {
                        if (subSetting instanceof SubCheckbox)
                            moduleSubSettingsArray.addProperty(((SubCheckbox) subSetting).getName(), ((SubCheckbox) subSetting).getValue());

                        if (subSetting instanceof SubMode)
                            moduleSubSettingsArray.addProperty(((SubMode) subSetting).getName(), ((SubMode) subSetting).getValue());

                        if (subSetting instanceof SubSlider)
                            moduleSubSettingsArray.addProperty(((SubSlider) subSetting).getName(), ((SubSlider) subSetting).getValue());

                        if (subSetting instanceof SubKeybind)
                            moduleSubSettingsArray.addProperty(((SubKeybind) subSetting).getName(), ((SubKeybind) subSetting).getKey());
                    }
                }

                if (setting instanceof Slider) {
                    moduleSettingsArray.addProperty(((Slider) setting).getName(), ((Slider) setting).getValue());

                    for (SubSetting subSetting : ((Slider) setting).getSubSettings()) {
                        if (subSetting instanceof SubCheckbox)
                            moduleSubSettingsArray.addProperty(((SubCheckbox) subSetting).getName(), ((SubCheckbox) subSetting).getValue());

                        if (subSetting instanceof SubMode)
                            moduleSubSettingsArray.addProperty(((SubMode) subSetting).getName(), ((SubMode) subSetting).getValue());

                        if (subSetting instanceof SubSlider)
                            moduleSubSettingsArray.addProperty(((SubSlider) subSetting).getName(), ((SubSlider) subSetting).getValue());

                        if (subSetting instanceof SubKeybind)
                            moduleSubSettingsArray.addProperty(((SubKeybind) subSetting).getName(), ((SubKeybind) subSetting).getKey());
                    }
                }

                if (setting instanceof Keybind) {
                    moduleSettingsArray.addProperty(((Keybind) setting).getName(), ((Keybind) setting).getKey());

                    for (SubSetting subSetting : ((Checkbox) setting).getSubSettings()) {
                        if (subSetting instanceof SubCheckbox)
                            moduleSubSettingsArray.addProperty(((SubCheckbox) subSetting).getName(), ((SubCheckbox) subSetting).getValue());

                        if (subSetting instanceof SubMode)
                            moduleSubSettingsArray.addProperty(((SubMode) subSetting).getName(), ((SubMode) subSetting).getValue());

                        if (subSetting instanceof SubSlider)
                            moduleSubSettingsArray.addProperty(((SubSlider) subSetting).getName(), ((SubSlider) subSetting).getValue());

                        if (subSetting instanceof SubKeybind)
                            moduleSubSettingsArray.addProperty(((SubKeybind) subSetting).getName(), ((SubKeybind) subSetting).getKey());
                    }
                }

                subSettingsArray.add("subSettings", moduleSubSettingsArray);
            }

            moduleArray.add(module.getName(), moduleSettingsArray);
        }

        settingsArray.add("modules", moduleArray);
        String jsonString = gson.toJson(new JsonParser().parse(settingsArray.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    public static void loadModule() {
        try {
            JsonParser jsonParser = new JsonParser();
            JsonReader reader = new JsonReader(new FileReader("modules.json"));
            JsonElement jsonElement = jsonParser.parse(reader);

            JsonElement moduleMap = jsonElement.getAsJsonObject().get("modules");
            JsonElement settingMap = jsonElement.getAsJsonObject().get("subSettings");
            if (moduleMap != null && settingMap != null) {
                for (Module module : ModuleManager.getModules()) {
                    JsonElement tempModuleMap = moduleMap.getAsJsonObject().get(module.getName());
                    if (tempModuleMap != null) {
                        if (tempModuleMap.getAsJsonObject().get("enabled").getAsBoolean())
                            module.enable();

                        for (Setting setting : module.getSettings()) {
                            if (setting instanceof Checkbox) {
                                if (tempModuleMap.getAsJsonObject().get(((Checkbox) setting).getName()) != null) {
                                    JsonElement tempSettingMap = settingMap.getAsJsonObject().get(((Checkbox) setting).getName());

                                    if (tempSettingMap != null) {
                                        ((Checkbox) setting).setChecked(tempModuleMap.getAsJsonObject().get(((Checkbox) setting).getName()).getAsBoolean());

                                        for (SubSetting subSetting : ((Checkbox) setting).getSubSettings()) {
                                            if (subSetting instanceof SubCheckbox)
                                            ((SubCheckbox) subSetting).setChecked(tempSettingMap.getAsJsonObject().get(((SubCheckbox) subSetting).getName()).getAsBoolean());

                                            if (subSetting instanceof SubMode)
                                                ((SubMode) subSetting).setMode(tempSettingMap.getAsJsonObject().get(((SubMode) subSetting).getName()).getAsInt());

                                            if (subSetting instanceof SubSlider)
                                                ((SubSlider) subSetting).setValue(tempSettingMap.getAsJsonObject().get(((SubSlider) subSetting).getName()).getAsDouble());

                                            if (subSetting instanceof SubKeybind)
                                                ((SubKeybind) subSetting).setKey(tempSettingMap.getAsJsonObject().get(((SubKeybind) subSetting).getName()).getAsInt());
                                        }
                                    }
                                }
                            }

                            if (setting instanceof Mode) {
                                if (tempModuleMap.getAsJsonObject().get(((Mode) setting).getName()) != null) {
                                    JsonElement tempSettingMap = settingMap.getAsJsonObject().get(((Mode) setting).getName());

                                    if (tempSettingMap != null) {
                                        ((Mode) setting).setMode(tempModuleMap.getAsJsonObject().get(((Mode) setting).getName()).getAsInt());

                                        for (SubSetting subSetting : ((Mode) setting).getSubSettings()) {
                                            if (subSetting instanceof SubCheckbox)
                                                ((SubCheckbox) subSetting).setChecked(tempSettingMap.getAsJsonObject().get(((SubCheckbox) subSetting).getName()).getAsBoolean());

                                            if (subSetting instanceof SubMode)
                                                ((SubMode) subSetting).setMode(tempSettingMap.getAsJsonObject().get(((SubMode) subSetting).getName()).getAsInt());

                                            if (subSetting instanceof SubSlider)
                                                ((SubSlider) subSetting).setValue(tempSettingMap.getAsJsonObject().get(((SubSlider) subSetting).getName()).getAsDouble());

                                            if (subSetting instanceof SubKeybind)
                                                ((SubKeybind) subSetting).setKey(tempSettingMap.getAsJsonObject().get(((SubKeybind) subSetting).getName()).getAsInt());
                                        }
                                    }
                                }
                            }

                            if (setting instanceof Slider) {
                                if (tempModuleMap.getAsJsonObject().get(((Slider) setting).getName()) != null) {
                                    JsonElement tempSettingMap = settingMap.getAsJsonObject().get(((Slider) setting).getName());

                                    if (tempSettingMap != null) {
                                        ((Slider) setting).setValue(tempModuleMap.getAsJsonObject().get(((Slider) setting).getName()).getAsDouble());

                                        for (SubSetting subSetting : ((Slider) setting).getSubSettings()) {
                                            if (subSetting instanceof SubCheckbox)
                                                ((SubCheckbox) subSetting).setChecked(tempSettingMap.getAsJsonObject().get(((SubCheckbox) subSetting).getName()).getAsBoolean());

                                            if (subSetting instanceof SubMode)
                                                ((SubMode) subSetting).setMode(tempSettingMap.getAsJsonObject().get(((SubMode) subSetting).getName()).getAsInt());

                                            if (subSetting instanceof SubSlider)
                                                ((SubSlider) subSetting).setValue(tempSettingMap.getAsJsonObject().get(((SubSlider) subSetting).getName()).getAsDouble());

                                            if (subSetting instanceof SubKeybind)
                                                ((SubKeybind) subSetting).setKey(tempSettingMap.getAsJsonObject().get(((SubKeybind) subSetting).getName()).getAsInt());
                                        }
                                    }
                                }
                            }

                            if (setting instanceof Keybind) {
                                if (tempModuleMap.getAsJsonObject().get(((Keybind) setting).getName()) != null) {
                                    JsonElement tempSettingMap = settingMap.getAsJsonObject().get(((Keybind) setting).getName());

                                    if (tempSettingMap != null) {
                                        ((Keybind) setting).setKey(tempModuleMap.getAsJsonObject().get(((Keybind) setting).getName()).getAsInt());

                                        for (SubSetting subSetting : ((Keybind) setting).getSubSettings()) {
                                            if (subSetting instanceof SubCheckbox)
                                                ((SubCheckbox) subSetting).setChecked(tempSettingMap.getAsJsonObject().get(((SubCheckbox) subSetting).getName()).getAsBoolean());

                                            if (subSetting instanceof SubMode)
                                                ((SubMode) subSetting).setMode(tempSettingMap.getAsJsonObject().get(((SubMode) subSetting).getName()).getAsInt());

                                            if (subSetting instanceof SubSlider)
                                                ((SubSlider) subSetting).setValue(tempSettingMap.getAsJsonObject().get(((SubSlider) subSetting).getName()).getAsDouble());

                                            if (subSetting instanceof SubKeybind)
                                                ((SubKeybind) subSetting).setKey(tempSettingMap.getAsJsonObject().get(((SubKeybind) subSetting).getName()).getAsInt());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
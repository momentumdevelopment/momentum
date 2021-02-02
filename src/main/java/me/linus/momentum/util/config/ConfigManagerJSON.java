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
import com.google.gson.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author linustouchtips & Hoosiers
 * @since 01/18/2020
 */

public class ConfigManagerJSON {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void createDirectory() throws IOException {
        if (!Files.exists(Paths.get("momentum/"))) 
            Files.createDirectories(Paths.get("momentum/"));
    }

    public static void registerFiles(String name) throws IOException {
        if (!Files.exists(Paths.get("momentum/" + name + ".json")))
            Files.createFile(Paths.get("momentum/" + name + ".json"));
        
        else {
            File file = new File("momentum/" + name + ".json");
            file.delete();
            Files.createFile(Paths.get("momentum/" + name + ".json"));
        }
    }

    public static void saveConfig() throws IOException {
        saveModules();
    }

    public static void loadConfig() throws IOException {
        createDirectory();
        loadModules();
    }

    public static void saveModules() throws IOException {
        for (Module module : ModuleManager.getModules()) {
            registerFiles(module.getName());
            OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("momentum/" + module.getName() + ".json"), StandardCharsets.UTF_8);

            JsonObject moduleObject = new JsonObject();
            JsonObject settingObject = new JsonObject();
            JsonObject subSettingObject = new JsonObject();

            moduleObject.add("Name", new JsonPrimitive(module.getName()));

            for (Setting setting : module.getSettings()) {
                if (setting instanceof Checkbox) {
                    settingObject.add(((Checkbox) setting).getName(), new JsonPrimitive(((Checkbox) setting).getValue()));

                    if (((Checkbox) setting).hasSubSettings()) {
                        for (SubSetting subSetting : ((Checkbox) setting).getSubSettings()) {
                            if (subSetting instanceof SubCheckbox)
                                subSettingObject.add(((SubCheckbox) subSetting).getName(), new JsonPrimitive(((SubCheckbox) subSetting).getValue()));

                            if (subSetting instanceof SubSlider)
                                subSettingObject.add(((SubSlider) subSetting).getName(), new JsonPrimitive(((SubSlider) subSetting).getValue()));

                            if (subSetting instanceof SubMode)
                                subSettingObject.add(((SubMode) subSetting).getName(), new JsonPrimitive(((SubMode) subSetting).getValue()));

                            if (subSetting instanceof SubKeybind)
                                subSettingObject.add(((SubKeybind) subSetting).getName(), new JsonPrimitive(((SubKeybind) subSetting).getKey()));
                        }
                    }
                }

                if (setting instanceof Slider) {
                    settingObject.add(((Slider) setting).getName(), new JsonPrimitive(((Slider) setting).getValue()));

                    if (((Slider) setting).hasSubSettings()) {
                        for (SubSetting subSetting : ((Slider) setting).getSubSettings()) {
                            if (subSetting instanceof SubCheckbox)
                                subSettingObject.add(((SubCheckbox) subSetting).getName(), new JsonPrimitive(((SubCheckbox) subSetting).getValue()));

                            if (subSetting instanceof SubSlider)
                                subSettingObject.add(((SubSlider) subSetting).getName(), new JsonPrimitive(((SubSlider) subSetting).getValue()));

                            if (subSetting instanceof SubMode)
                                subSettingObject.add(((SubMode) subSetting).getName(), new JsonPrimitive(((SubMode) subSetting).getValue()));

                            if (subSetting instanceof SubKeybind)
                                subSettingObject.add(((SubKeybind) subSetting).getName(), new JsonPrimitive(((SubKeybind) subSetting).getKey()));
                        }
                    }
                }

                if (setting instanceof Mode) {
                    settingObject.add(((Mode) setting).getName(), new JsonPrimitive(((Mode) setting).getValue()));

                    if (((Mode) setting).hasSubSettings()) {
                        for (SubSetting subSetting : ((Mode) setting).getSubSettings()) {
                            if (subSetting instanceof SubCheckbox)
                                subSettingObject.add(((SubCheckbox) subSetting).getName(), new JsonPrimitive(((SubCheckbox) subSetting).getValue()));

                            if (subSetting instanceof SubSlider)
                                subSettingObject.add(((SubSlider) subSetting).getName(), new JsonPrimitive(((SubSlider) subSetting).getValue()));

                            if (subSetting instanceof SubMode)
                                subSettingObject.add(((SubMode) subSetting).getName(), new JsonPrimitive(((SubMode) subSetting).getValue()));

                            if (subSetting instanceof SubKeybind)
                                subSettingObject.add(((SubKeybind) subSetting).getName(), new JsonPrimitive(((SubKeybind) subSetting).getKey()));
                        }
                    }
                }

                if (setting instanceof Keybind) {
                    settingObject.add(((Keybind) setting).getName(), new JsonPrimitive(((Keybind) setting).getKey()));

                    if (((Keybind) setting).hasSubSettings()) {
                        for (SubSetting subSetting : ((Keybind) setting).getSubSettings()) {
                            if (subSetting instanceof SubCheckbox)
                                subSettingObject.add(((SubCheckbox) subSetting).getName(), new JsonPrimitive(((SubCheckbox) subSetting).getValue()));

                            if (subSetting instanceof SubSlider)
                                subSettingObject.add(((SubSlider) subSetting).getName(), new JsonPrimitive(((SubSlider) subSetting).getValue()));

                            if (subSetting instanceof SubMode)
                                subSettingObject.add(((SubMode) subSetting).getName(), new JsonPrimitive(((SubMode) subSetting).getValue()));

                            if (subSetting instanceof SubKeybind)
                                subSettingObject.add(((SubKeybind) subSetting).getName(), new JsonPrimitive(((SubKeybind) subSetting).getKey()));
                        }
                    }
                }
            }

            settingObject.add("SubSettings", subSettingObject);
            moduleObject.add("Settings", settingObject);
            String jsonString = gson.toJson(new JsonParser().parse(moduleObject.toString()));
            fileOutputStreamWriter.write(jsonString);
            fileOutputStreamWriter.close();
        }
    }

    public static void loadModules() throws IOException {
        for (Module module : ModuleManager.getModules()) {
            if (!Files.exists(Paths.get("momentum/" + module.getName() + ".json")))
                return;

            InputStream inputStream = Files.newInputStream(Paths.get("momentum/" + module.getName() + ".json"));
            JsonObject moduleObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

            if (moduleObject.get("Name") == null)
                return;

            JsonObject settingObject = moduleObject.get("Settings").getAsJsonObject();
            JsonObject subSettingObject = settingObject.get("SubSettings").getAsJsonObject();

            for (Setting setting : module.getSettings()) {

                JsonElement dataSettingObject = null;

                if (setting instanceof Checkbox) {
                    dataSettingObject = settingObject.get(((Checkbox) setting).getName());

                    if (((Checkbox) setting).hasSubSettings()) {
                        for (SubSetting subSetting : ((Checkbox) setting).getSubSettings()) {

                            JsonElement dataSubSettingObject = null;

                            if (subSetting instanceof SubCheckbox)
                                dataSubSettingObject = subSettingObject.get(((SubCheckbox) subSetting).getName());

                            if (subSetting instanceof SubSlider)
                                dataSubSettingObject = subSettingObject.get(((SubSlider) subSetting).getName());

                            if (subSetting instanceof SubMode)
                                dataSubSettingObject = subSettingObject.get(((SubMode) subSetting).getName());

                            if (subSetting instanceof SubKeybind)
                                dataSubSettingObject = subSettingObject.get(((SubKeybind) subSetting).getName());

                            if (dataSubSettingObject != null && dataSubSettingObject.isJsonPrimitive()) {
                                if (subSetting instanceof SubCheckbox)
                                    ((SubCheckbox) subSetting).setChecked(dataSubSettingObject.getAsBoolean());

                                if (subSetting instanceof SubSlider)
                                    ((SubSlider) subSetting).setValue(dataSubSettingObject.getAsDouble());

                                if (subSetting instanceof SubMode)
                                    ((SubMode) subSetting).setMode(dataSubSettingObject.getAsInt());

                                if (subSetting instanceof SubKeybind)
                                    ((SubKeybind) subSetting).setKey(dataSubSettingObject.getAsInt());
                            }
                        }
                    }
                }

                if (setting instanceof Slider) {
                    dataSettingObject = settingObject.get(((Slider) setting).getName());

                    if (((Slider) setting).hasSubSettings()) {
                        for (SubSetting subSetting : ((Slider) setting).getSubSettings()) {

                            JsonElement dataSubSettingObject = null;

                            if (subSetting instanceof SubCheckbox)
                                dataSubSettingObject = subSettingObject.get(((SubCheckbox) subSetting).getName());

                            if (subSetting instanceof SubSlider)
                                dataSubSettingObject = subSettingObject.get(((SubSlider) subSetting).getName());

                            if (subSetting instanceof SubMode)
                                dataSubSettingObject = subSettingObject.get(((SubMode) subSetting).getName());

                            if (subSetting instanceof SubKeybind)
                                dataSubSettingObject = subSettingObject.get(((SubKeybind) subSetting).getName());

                            if (dataSubSettingObject != null && dataSubSettingObject.isJsonPrimitive()) {
                                if (subSetting instanceof SubCheckbox)
                                    ((SubCheckbox) subSetting).setChecked(dataSubSettingObject.getAsBoolean());

                                if (subSetting instanceof SubSlider)
                                    ((SubSlider) subSetting).setValue(dataSubSettingObject.getAsDouble());

                                if (subSetting instanceof SubMode)
                                    ((SubMode) subSetting).setMode(dataSubSettingObject.getAsInt());

                                if (subSetting instanceof SubKeybind)
                                    ((SubKeybind) subSetting).setKey(dataSubSettingObject.getAsInt());
                            }
                        }
                    }
                }

                if (setting instanceof Mode) {
                    dataSettingObject = settingObject.get(((Mode) setting).getName());

                    if (((Mode) setting).hasSubSettings()) {
                        for (SubSetting subSetting : ((Mode) setting).getSubSettings()) {

                            JsonElement dataSubSettingObject = null;

                            if (subSetting instanceof SubCheckbox)
                                dataSubSettingObject = subSettingObject.get(((SubCheckbox) subSetting).getName());

                            if (subSetting instanceof SubSlider)
                                dataSubSettingObject = subSettingObject.get(((SubSlider) subSetting).getName());

                            if (subSetting instanceof SubMode)
                                dataSubSettingObject = subSettingObject.get(((SubMode) subSetting).getName());

                            if (subSetting instanceof SubKeybind)
                                dataSubSettingObject = subSettingObject.get(((SubKeybind) subSetting).getName());

                            if (dataSubSettingObject != null && dataSubSettingObject.isJsonPrimitive()) {
                                if (subSetting instanceof SubCheckbox)
                                    ((SubCheckbox) subSetting).setChecked(dataSubSettingObject.getAsBoolean());

                                if (subSetting instanceof SubSlider)
                                    ((SubSlider) subSetting).setValue(dataSubSettingObject.getAsDouble());

                                if (subSetting instanceof SubMode)
                                    ((SubMode) subSetting).setMode(dataSubSettingObject.getAsInt());

                                if (subSetting instanceof SubKeybind)
                                    ((SubKeybind) subSetting).setKey(dataSubSettingObject.getAsInt());
                            }
                        }
                    }
                }

                if (setting instanceof Keybind) {
                    dataSettingObject = settingObject.get(((Keybind) setting).getName());

                    if (((Keybind) setting).hasSubSettings()) {
                        for (SubSetting subSetting : ((Keybind) setting).getSubSettings()) {

                            JsonElement dataSubSettingObject = null;

                            if (subSetting instanceof SubCheckbox)
                                dataSubSettingObject = subSettingObject.get(((SubCheckbox) subSetting).getName());

                            if (subSetting instanceof SubSlider)
                                dataSubSettingObject = subSettingObject.get(((SubSlider) subSetting).getName());

                            if (subSetting instanceof SubMode)
                                dataSubSettingObject = subSettingObject.get(((SubMode) subSetting).getName());

                            if (subSetting instanceof SubKeybind)
                                dataSubSettingObject = subSettingObject.get(((SubKeybind) subSetting).getName());

                            if (dataSubSettingObject != null && dataSubSettingObject.isJsonPrimitive()) {
                                if (subSetting instanceof SubCheckbox)
                                    ((SubCheckbox) subSetting).setChecked(dataSubSettingObject.getAsBoolean());

                                if (subSetting instanceof SubSlider)
                                    ((SubSlider) subSetting).setValue(dataSubSettingObject.getAsDouble());

                                if (subSetting instanceof SubMode)
                                    ((SubMode) subSetting).setMode(dataSubSettingObject.getAsInt());

                                if (subSetting instanceof SubKeybind)
                                    ((SubKeybind) subSetting).setKey(dataSubSettingObject.getAsInt());
                            }
                        }
                    }
                }

                if (dataSettingObject != null && dataSettingObject.isJsonPrimitive()) {
                    if (setting instanceof Checkbox)
                        ((Checkbox) setting).setChecked(dataSettingObject.getAsBoolean());

                    if (setting instanceof Slider)
                        ((Slider) setting).setValue(dataSettingObject.getAsDouble());

                    if (setting instanceof Mode)
                        ((Mode) setting).setMode(dataSettingObject.getAsInt());

                    if (setting instanceof Keybind)
                        ((Keybind) setting).setKey(dataSettingObject.getAsInt());
                }
            }

            inputStream.close();
        }
    }
}
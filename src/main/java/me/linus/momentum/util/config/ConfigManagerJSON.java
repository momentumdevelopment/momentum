package me.linus.momentum.util.config;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.hud.HUDComponentManager;
import me.linus.momentum.gui.main.gui.Window;
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
import me.linus.momentum.util.social.enemy.Enemy;
import me.linus.momentum.util.social.enemy.EnemyManager;
import me.linus.momentum.util.social.friend.Friend;
import me.linus.momentum.util.social.friend.FriendManager;

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

    public static void saveConfig() {
        try {
            saveModules();
            saveGUI();
            saveHUD();
            saveFriends();
            saveEnemies();
        } catch (IOException e) {

        }
    }

    public static void loadConfig() {
        try {
            createDirectory();
            loadModules();
            loadGUI();
            loadHUD();
            loadFriends();
            loadEnemies();
        } catch (IOException e) {

        }
    }

    public static void saveModules() throws IOException {
        for (Module module : ModuleManager.getModules()) {
            registerFiles(module.getName());
            OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("momentum/" + module.getName() + ".json"), StandardCharsets.UTF_8);

            JsonObject moduleObject = new JsonObject();
            JsonObject settingObject = new JsonObject();
            JsonObject subSettingObject = new JsonObject();

            moduleObject.add("Name", new JsonPrimitive(module.getName()));
            moduleObject.add("Enabled", new JsonPrimitive(module.isEnabled()));
            moduleObject.add("Drawn", new JsonPrimitive(module.isDrawn()));
            moduleObject.add("Bind", new JsonPrimitive(module.getKeybind().getKeyCode()));

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
                continue;

            InputStream inputStream = Files.newInputStream(Paths.get("momentum/" + module.getName() + ".json"));
            JsonObject moduleObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

            if (moduleObject.get("Name") == null || moduleObject.get("Enabled") == null || moduleObject.get("Drawn") == null || moduleObject.get("Bind") == null)
                continue;

            Momentum.LOGGER.info("Loading for " + module.getName());
            module.setEnabled(moduleObject.get("Enabled").getAsBoolean());
            module.setDrawn(moduleObject.get("Drawn").getAsBoolean());
            module.getKeybind().setKeyCode(moduleObject.get("Bind").getAsInt());

            JsonObject settingObject = moduleObject.get("Settings").getAsJsonObject();
            JsonObject subSettingObject = settingObject.get("SubSettings").getAsJsonObject();

            for (Setting setting : module.getSettings()) {
                JsonElement settingValueObject = null;

                if (setting instanceof Checkbox) {
                    settingValueObject = settingObject.get(((Checkbox) setting).getName());

                    for (SubSetting subSetting : ((Checkbox) setting).getSubSettings()) {
                        JsonElement subSettingValueObject = null;

                        if (subSetting instanceof SubCheckbox)
                            subSettingValueObject = subSettingObject.get(((SubCheckbox) subSetting).getName());

                        if (subSetting instanceof SubSlider)
                            subSettingValueObject = subSettingObject.get(((SubSlider) subSetting).getName());

                        if (subSetting instanceof SubMode)
                            subSettingValueObject = subSettingObject.get(((SubMode) subSetting).getName());

                        if (subSetting instanceof SubKeybind)
                            subSettingValueObject = subSettingObject.get(((SubKeybind) subSetting).getName());

                        if (subSettingValueObject != null) {
                            if (subSetting instanceof SubCheckbox)
                                ((SubCheckbox) subSetting).setChecked(subSettingValueObject.getAsBoolean());

                            if (subSetting instanceof SubSlider)
                                ((SubSlider) subSetting).setValue(subSettingValueObject.getAsDouble());

                            if (subSetting instanceof SubMode)
                                ((SubMode) subSetting).setMode(subSettingValueObject.getAsInt());

                            if (subSetting instanceof SubKeybind)
                                ((SubKeybind) subSetting).setKey(subSettingValueObject.getAsInt());
                        }
                    }
                }

                if (setting instanceof Slider) {
                    settingValueObject = settingObject.get(((Slider) setting).getName());

                    for (SubSetting subSetting : ((Slider) setting).getSubSettings()) {
                        JsonElement subSettingValueObject = null;

                        if (subSetting instanceof SubCheckbox)
                            subSettingValueObject = subSettingObject.get(((SubCheckbox) subSetting).getName());

                        if (subSetting instanceof SubSlider)
                            subSettingValueObject = subSettingObject.get(((SubSlider) subSetting).getName());

                        if (subSetting instanceof SubMode)
                            subSettingValueObject = subSettingObject.get(((SubMode) subSetting).getName());

                        if (subSetting instanceof SubKeybind)
                            subSettingValueObject = subSettingObject.get(((SubKeybind) subSetting).getName());

                        if (subSettingValueObject != null) {
                            if (subSetting instanceof SubCheckbox)
                                ((SubCheckbox) subSetting).setChecked(subSettingValueObject.getAsBoolean());

                            if (subSetting instanceof SubSlider)
                                ((SubSlider) subSetting).setValue(subSettingValueObject.getAsDouble());

                            if (subSetting instanceof SubMode)
                                ((SubMode) subSetting).setMode(subSettingValueObject.getAsInt());

                            if (subSetting instanceof SubKeybind)
                                ((SubKeybind) subSetting).setKey(subSettingValueObject.getAsInt());
                        }
                    }
                }

                if (setting instanceof Mode) {
                    settingValueObject = settingObject.get(((Mode) setting).getName());

                    for (SubSetting subSetting : ((Mode) setting).getSubSettings()) {
                        JsonElement subSettingValueObject = null;

                        if (subSetting instanceof SubCheckbox)
                            subSettingValueObject = subSettingObject.get(((SubCheckbox) subSetting).getName());

                        if (subSetting instanceof SubSlider)
                            subSettingValueObject = subSettingObject.get(((SubSlider) subSetting).getName());

                        if (subSetting instanceof SubMode)
                            subSettingValueObject = subSettingObject.get(((SubMode) subSetting).getName());

                        if (subSetting instanceof SubKeybind)
                            subSettingValueObject = subSettingObject.get(((SubKeybind) subSetting).getName());

                        if (subSettingValueObject != null) {
                            if (subSetting instanceof SubCheckbox)
                                ((SubCheckbox) subSetting).setChecked(subSettingValueObject.getAsBoolean());

                            if (subSetting instanceof SubSlider)
                                ((SubSlider) subSetting).setValue(subSettingValueObject.getAsDouble());

                            if (subSetting instanceof SubMode)
                                ((SubMode) subSetting).setMode(subSettingValueObject.getAsInt());

                            if (subSetting instanceof SubKeybind)
                                ((SubKeybind) subSetting).setKey(subSettingValueObject.getAsInt());
                        }
                    }
                }

                if (setting instanceof Keybind) {
                    settingValueObject = settingObject.get(((Keybind) setting).getName());

                    for (SubSetting subSetting : ((Keybind) setting).getSubSettings()) {
                        JsonElement subSettingValueObject = null;

                        if (subSetting instanceof SubCheckbox)
                            subSettingValueObject = subSettingObject.get(((SubCheckbox) subSetting).getName());

                        if (subSetting instanceof SubSlider)
                            subSettingValueObject = subSettingObject.get(((SubSlider) subSetting).getName());

                        if (subSetting instanceof SubMode)
                            subSettingValueObject = subSettingObject.get(((SubMode) subSetting).getName());

                        if (subSetting instanceof SubKeybind)
                            subSettingValueObject = subSettingObject.get(((SubKeybind) subSetting).getName());

                        if (subSettingValueObject != null) {
                            if (subSetting instanceof SubCheckbox)
                                ((SubCheckbox) subSetting).setChecked(subSettingValueObject.getAsBoolean());

                            if (subSetting instanceof SubSlider)
                                ((SubSlider) subSetting).setValue(subSettingValueObject.getAsDouble());

                            if (subSetting instanceof SubMode)
                                ((SubMode) subSetting).setMode(subSettingValueObject.getAsInt());

                            if (subSetting instanceof SubKeybind)
                                ((SubKeybind) subSetting).setKey(subSettingValueObject.getAsInt());
                        }
                    }
                }

                if (settingValueObject != null) {
                    if (setting instanceof Checkbox)
                        ((Checkbox) setting).setChecked(settingValueObject.getAsBoolean());

                    if (setting instanceof Slider)
                        ((Slider) setting).setValue(settingValueObject.getAsDouble());

                    if (setting instanceof Mode)
                        ((Mode) setting).setMode(settingValueObject.getAsInt());

                    if (setting instanceof Keybind)
                        ((Keybind) setting).setKey(settingValueObject.getAsInt());
                }
            }
        }
    }

    public static void saveGUI() throws IOException {
        registerFiles("GUI");

        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("momentum/" + "GUI" + ".json"), StandardCharsets.UTF_8);
        JsonObject guiObject = new JsonObject();
        JsonObject windowObject = new JsonObject();

        for (Window window : Window.windows) {
            JsonObject positionObject = new JsonObject();

            positionObject.add("x", new JsonPrimitive(window.x));
            positionObject.add("y", new JsonPrimitive(window.y));
            positionObject.add("open", new JsonPrimitive(window.opened));

            windowObject.add(window.category.getName(), positionObject);
        }

        guiObject.add("Windows", windowObject);
        String jsonString = gson.toJson(new JsonParser().parse(guiObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    public static void loadGUI() throws IOException {
        if (!Files.exists(Paths.get("momentum/" + "GUI" + ".json")))
            return;

        InputStream inputStream = Files.newInputStream(Paths.get("momentum/" + "GUI" + ".json"));
        JsonObject guiObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (guiObject.get("Windows") == null)
            return;

        JsonObject windowObject = guiObject.get("Windows").getAsJsonObject();
        for (Window window : Window.windows) {
            if (windowObject.get(window.category.name()) == null)
                return;

            JsonObject categoryObject = windowObject.get(window.category.name()).getAsJsonObject();

            JsonElement windowXObject = categoryObject.get("x");
            if (windowXObject != null && windowXObject.isJsonPrimitive())
                window.x = windowXObject.getAsInt();

            JsonElement windowYObject = categoryObject.get("y");
            if (windowYObject != null && windowYObject.isJsonPrimitive())
                window.y = windowYObject.getAsInt();

            JsonElement windowOpenObject = categoryObject.get("open");
            if (windowOpenObject != null && windowOpenObject.isJsonPrimitive())
                window.opened = windowOpenObject.getAsBoolean();
        }

        inputStream.close();
    }

    public static void saveHUD() throws IOException {
        registerFiles("HUD");

        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("momentum/" + "HUD" + ".json"), StandardCharsets.UTF_8);
        JsonObject guiObject = new JsonObject();
        JsonObject hudObject = new JsonObject();

        for (HUDComponent component : HUDComponentManager.getComponents()) {
            JsonObject positionObject = new JsonObject();

            positionObject.add("x", new JsonPrimitive(component.x));
            positionObject.add("y", new JsonPrimitive(component.y));
            positionObject.add("enabled", new JsonPrimitive(component.isEnabled()));

            hudObject.add(component.getName(), positionObject);
        }

        guiObject.add("Components", hudObject);
        String jsonString = gson.toJson(new JsonParser().parse(guiObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    public static void loadHUD() throws IOException {
        if (!Files.exists(Paths.get("momentum/" + "HUD" + ".json")))
            return;

        InputStream inputStream = Files.newInputStream(Paths.get("momentum/" + "HUD" + ".json"));
        JsonObject guiObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (guiObject.get("Components") == null)
            return;

        JsonObject windowObject = guiObject.get("Components").getAsJsonObject();
        for (HUDComponent component : HUDComponentManager.getComponents()) {
            if (windowObject.get(component.getName()) == null)
                return;

            JsonObject categoryObject = windowObject.get(component.getName()).getAsJsonObject();

            JsonElement hudXObject = categoryObject.get("x");
            if (hudXObject != null && hudXObject.isJsonPrimitive())
                component.x = hudXObject.getAsInt();

            JsonElement hudYObject = categoryObject.get("y");
            if (hudYObject != null && hudYObject.isJsonPrimitive())
                component.y = hudYObject.getAsInt();

            JsonElement hudEnabledObject = categoryObject.get("enabled");
            if (hudEnabledObject != null && hudEnabledObject.isJsonPrimitive())
                if (hudEnabledObject.getAsBoolean())
                    component.toggle();
        }

        inputStream.close();
    }

    public static void saveFriends() throws IOException {
        registerFiles("Friends");

        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("momentum/" + "Friends" + ".json"), StandardCharsets.UTF_8);
        JsonObject mainObject = new JsonObject();
        JsonArray friendArray = new JsonArray();

        for (Friend friend : FriendManager.getFriends()) {
            friendArray.add(friend.getName());
        }

        mainObject.add("Friends", friendArray);
        String jsonString = gson.toJson(new JsonParser().parse(mainObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    public static void loadFriends() throws IOException {
        if (!Files.exists(Paths.get("momentum/" + "Friends" + ".json")))
            return;

        InputStream inputStream = Files.newInputStream(Paths.get("momentum/" + "Friends" + ".json"));
        JsonObject mainObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (mainObject.get("Friends") == null)
            return;

        JsonArray friendObject = mainObject.get("Friends").getAsJsonArray();

        for (JsonElement object : friendObject) {
            FriendManager.addFriend(object.getAsString());
        }

        inputStream.close();
    }

    public static void saveEnemies() throws IOException {
        registerFiles("Enemies");

        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("momentum/" + "Enemies" + ".json"), StandardCharsets.UTF_8);
        JsonObject mainObject = new JsonObject();
        JsonArray enemyArray = new JsonArray();

        for (Enemy enemy : EnemyManager.getEnemies()) {
            enemyArray.add(enemy.getName());
        }

        mainObject.add("Enemies", enemyArray);
        String jsonString = gson.toJson(new JsonParser().parse(mainObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    public static void loadEnemies() throws IOException {
        if (!Files.exists(Paths.get("momentum/" + "Enemies" + ".json")))
            return;

        InputStream inputStream = Files.newInputStream(Paths.get("momentum/" + "Enemies" + ".json"));
        JsonObject mainObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (mainObject.get("Enemies") == null)
            return;

        JsonArray enemyObject = mainObject.get("Enemies").getAsJsonArray();

        for (JsonElement object : enemyObject) {
            EnemyManager.addEnemy(object.getAsString());
        }

        inputStream.close();
    }
}
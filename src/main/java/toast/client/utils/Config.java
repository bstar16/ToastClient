package toast.client.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import toast.client.modules.Module;
import toast.client.modules.config.Setting;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import static toast.client.ToastClient.MODULE_MANAGER;

public class Config {
    public static final String configFile = "config.json";
    public static final String modulesFile = "modules.json";
    public static final String keybindsFile = "keybinds.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Map<String, Integer> keybinds = new HashMap<>();
    public static Map<String, Boolean> modules = new HashMap<>();
    public static Map<String, Map<String, Setting>> config = new HashMap<>();
    public static String disabledOnStart = "Panic, ClickGui";

    public static void updateRead() {
        try {
            config = gson.fromJson(new FileReader(FileManager.createFile(configFile)), new TypeToken<Map<String, Map<String, Setting>>>() {
            }.getType());
            modules = gson.fromJson(new FileReader(FileManager.createFile(modulesFile)), new TypeToken<Map<String, Boolean>>() {
            }.getType());
            keybinds = gson.fromJson(new FileReader(FileManager.createFile(keybindsFile)), new TypeToken<Map<String, Integer>>() {
            }.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void writeConfig() {
        Map<String, Map<String, Setting>> config = new HashMap<>();
        for (Module module : MODULE_MANAGER.getModules()) {
            config.put(module.getName(), module.getSettings().getSettings());
        }
        FileManager.writeFile(configFile, gson.toJson(config));
    }

    public static void loadConfigAuto() {
        updateRead();
        if (config == null) {
            writeConfig();
            updateRead();
            return;
        }
        loadConfig(config);
    }

    public static void loadConfig(Map<String, Map<String, Setting>> config) {
        if (config == null || config.isEmpty()) return;
        for (Module module : MODULE_MANAGER.getModules()) {
            if (config.containsKey(module.getName())) {
                for (Map.Entry<String, Setting> setting : config.get(module.getName()).entrySet()) {
                    if (module.getSettings().getSettings().containsKey(setting.getKey())) {
                        module.getSettings().getSettings().replace(setting.getKey(), setting.getValue());
                    }
                }
            }
        }
    }

    public static void writeKeyBinds() {
        Map<String, Integer> keybinds = new HashMap<>();
        for (Module module : MODULE_MANAGER.getModules()) {
            keybinds.put(module.getName(), module.getKey());
        }
        FileManager.writeFile(keybindsFile, gson.toJson(keybinds));
    }

    public static void loadKeyBindsAuto() {
        updateRead();
        if (keybinds == null) {
            writeKeyBinds();
            keybinds = new HashMap<>();
            return;
        }
        loadKeyBinds(keybinds);
    }

    public static void loadKeyBinds(Map<String, Integer> keybinds) {
        if (keybinds == null || keybinds.isEmpty()) return;
        for (Module module : MODULE_MANAGER.getModules()) {
            if (module != null && keybinds != null) {
                if (keybinds.containsKey(module.getName())) {
                    module.setKey(keybinds.get(module.getName()));
                }
            }
        }
    }

    public static void writeModules() {
        Map<String, Boolean> modules = new HashMap<>();
        for (Module module : MODULE_MANAGER.getModules()) {
            modules.put(module.getName(), module.isEnabled());
        }
        FileManager.writeFile(modulesFile, gson.toJson(modules));
    }

    public static void loadModulesAuto() {
        updateRead();
        if (modules == null) {
            writeModules();
            updateRead();
            return;
        }
        loadModules(modules);
    }

    public static void loadModules(Map<String, Boolean> modules) {
        if (modules == null || modules.isEmpty()) return;
        for (Module module : MODULE_MANAGER.getModules()) {
            if (module != null && modules != null) {
                if (!disabledOnStart.contains(module.getName())) {
                    if (modules.containsKey(module.getName())) {
                        module.setEnabled(modules.get(module.getName()));
                    }
                } else {
                    module.setEnabled(false);
                }
            }
        }
    }
}

package com.x404dev.astralQOL.config;

import com.x404dev.astralQOL.AstralQOL;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AstralConfig {

    public Settings(AstralQOL plugin, String fileName) {
        super(plugin, fileName, true);
    }

    public static Map<String, Boolean> DEFAULT_FEATURES_STATE;

    public void onLoad() {
        ConfigurationSection section = getConfigurationSection("default_features_state");
        if (section != null) {
            DEFAULT_FEATURES_STATE = new HashMap<>();
            for (String key : section.getKeys(false)) {
                DEFAULT_FEATURES_STATE.put(key, section.getBoolean(key));
            }
        } else {
            DEFAULT_FEATURES_STATE = new HashMap<>(); // Initialize empty map if section doesn't exist
        }
    }

    public static void init(AstralQOL plugin) {
        new Settings(plugin, "settings.yml").onLoad();
    }
}

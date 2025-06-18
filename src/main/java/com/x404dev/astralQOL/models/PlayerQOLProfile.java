package com.x404dev.astralQOL.models;

import com.x404dev.astralQOL.AstralQOL;
import com.x404dev.astralQOL.qol.QOLFeature;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerQOLProfile {
    private final UUID playerId;
    private final Map<String, Boolean> featureStates;
    private final AstralQOL plugin;

    public PlayerQOLProfile(UUID playerId, AstralQOL plugin) {
        this.playerId = playerId;
        this.plugin = plugin;
        this.featureStates = new HashMap<>();

        // Initialize with default values for all registered features
        initializeDefaultStates();
    }

    private void initializeDefaultStates() {
        for (QOLFeature feature : plugin.getQolManager().getAllFeatures()) {
            featureStates.putIfAbsent(feature.getId(), true); // default enabled
        }
    }

    public boolean isFeatureEnabled(String featureId) {
        return featureStates.getOrDefault(featureId, true);
    }

    public void setFeatureEnabled(String featureId, boolean enabled) {
        featureStates.put(featureId, enabled);
        saveToConfig();
    }

    public void toggleFeature(String featureId) {
        boolean current = isFeatureEnabled(featureId);
        setFeatureEnabled(featureId, !current);
    }

    public Map<String, Boolean> getAllFeatureStates() {
        return new HashMap<>(featureStates);
    }

    public void saveToConfig() {
        // Save to player data file or database
//        FileConfiguration config = plugin.getPlayerDataConfig(playerId);
//        for (Map.Entry<String, Boolean> entry : featureStates.entrySet()) {
//            config.set("qol-features." + entry.getKey(), entry.getValue());
//        }
//        plugin.savePlayerDataConfig(playerId);
        //TODO
    }

    public void loadFromConfig() {
//        FileConfiguration config = plugin.getPlayerDataConfig(playerId);
//        for (String featureId : plugin.getQOLManager().getAllFeatureIds()) {
//            boolean enabled = config.getBoolean("qol-features." + featureId, true);
//            featureStates.put(featureId, enabled);
//        }
        //TODO
    }
}

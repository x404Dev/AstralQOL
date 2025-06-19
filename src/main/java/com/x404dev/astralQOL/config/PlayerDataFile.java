package com.x404dev.astralQOL.config;

import com.x404dev.astralQOL.AstralQOL;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages player data storage in data/playerdata.yml
 */
public class PlayerDataFile {

    private final AstralConfig dataConfig;

    public PlayerDataFile(AstralQOL plugin) {
        // Create config without defaults since this is a data file
        this.dataConfig = new AstralConfig(plugin, "data/playerdata.yml", false);

        // Set header for the data file
        dataConfig.setHeader(
                "AstralQOL Player Data File",
                "This file stores player profiles and feature states",
                "Do not edit manually unless you know what you're doing"
        );
    }

    /**
     * Saves a player profile to the data file
     *
     * @param playerId The player's UUID
     * @param featureStates Map of feature names to their enabled states
     */
    public void savePlayerProfile(UUID playerId, Map<String, Boolean> featureStates) {
        String playerPath = "players." + playerId.toString();

        // Save each feature state
        for (Map.Entry<String, Boolean> entry : featureStates.entrySet()) {
            dataConfig.set(playerPath + ".features." + entry.getKey(), entry.getValue());
        }

        dataConfig.save();
    }

    /**
     * Loads a player profile from the data file
     *
     * @param playerId The player's UUID
     * @return Map of feature states, or empty map if player not found
     */
    public Map<String, Boolean> loadPlayerProfile(UUID playerId) {
        String playerPath = "players." + playerId.toString();
        Map<String, Boolean> featureStates = new HashMap<>();

        // Check if player exists
        if (!dataConfig.contains(playerPath)) {
            return featureStates; // Return empty map
        }

        // Load feature states
        if (dataConfig.contains(playerPath + ".features")) {
            var featuresSection = dataConfig.getConfigurationSection(playerPath + ".features");
            if (featuresSection != null) {
                for (String featureName : featuresSection.getKeys(false)) {
                    boolean state = featuresSection.getBoolean(featureName, false);
                    featureStates.put(featureName, state);
                }
            }
        }

        return featureStates;
    }

    /**
     * Sets a specific feature state for a player
     *
     * @param playerId The player's UUID
     * @param featureName The feature name
     * @param enabled Whether the feature is enabled
     */
    public void setFeatureState(UUID playerId, String featureName, boolean enabled) {
        String featurePath = "players." + playerId.toString() + ".features." + featureName;
        dataConfig.write(featurePath, enabled);
    }

    /**
     * Gets a specific feature state for a player
     *
     * @param playerId The player's UUID
     * @param featureName The feature name
     * @param defaultValue Default value if feature not found
     * @return The feature state
     */
    public boolean getFeatureState(UUID playerId, String featureName, boolean defaultValue) {
        String featurePath = "players." + playerId.toString() + ".features." + featureName;
        return (Boolean) dataConfig.get(featurePath, defaultValue);
    }

    /**
     * Checks if a player profile exists
     *
     * @param playerId The player's UUID
     * @return True if player profile exists
     */
    public boolean hasPlayerProfile(UUID playerId) {
        return dataConfig.contains("players." + playerId.toString());
    }

    /**
     * Deletes a player profile
     *
     * @param playerId The player's UUID
     */
    public void deletePlayerProfile(UUID playerId) {
        String playerPath = "players." + playerId.toString();
        dataConfig.set(playerPath, null);
        dataConfig.save();
    }

    /**
     * Reloads the data file from disk
     */
    public void reload() {
        dataConfig.reload();
    }

    /**
     * Saves any pending changes to disk
     */
    public void save() {
        dataConfig.save();
    }
}
package com.x404dev.astralQOL.managers;

import com.x404dev.astralQOL.AstralQOL;
import com.x404dev.astralQOL.qol.QOLFeature;
import com.x404dev.astralQOL.qol.antilogstripper.AntiLogStripperFeature;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;

public class QOLManager {
    private final AstralQOL plugin;
    private final Map<String, QOLFeature> features;
    private final Map<String, QOLFeature> featuresById;
    @Getter
    private boolean initialized = false;

    public QOLManager(AstralQOL plugin) {
        this.plugin = plugin;
        this.features = new LinkedHashMap<>(); // LinkedHashMap to preserve order
        this.featuresById = new HashMap<>();
    }

    /**
     * Initialize and register all QOL features
     */
    public void initialize() {
        if (initialized) {
            plugin.getLogger().warning("QOLManager is already initialized!");
            return;
        }

        plugin.getLogger().info("Initializing QOL features...");

        // Register all QOL features here
//        registerFeature(new AntiLogStripperFeature(plugin));
        registerFeature(new AntiLogStripperFeature(plugin));

        // Enable all features
        enableAllFeatures();

        initialized = true;
        plugin.getLogger().info("Initialized " + features.size() + " QOL features");
    }

    /**
     * Shutdown all QOL features
     */
    public void shutdown() {
        if (!initialized) return;

        plugin.getLogger().info("Shutting down QOL features...");

        for (QOLFeature feature : features.values()) {
            try {
                feature.onDisable();
                plugin.getLogger().info("Disabled feature: " + feature.getDisplayName());
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,
                        "Error disabling feature " + feature.getDisplayName(), e);
            }
        }

        features.clear();
        featuresById.clear();
        initialized = false;

        plugin.getLogger().info("All QOL features shut down");
    }

    /**
     * Register a new QOL feature
     */
    private void registerFeature(QOLFeature feature) {
        if (features.containsKey(feature.getDisplayName())) {
            plugin.getLogger().warning("Feature " + feature.getDisplayName() + " is already registered!");
            return;
        }

        if (featuresById.containsKey(feature.getId())) {
            plugin.getLogger().warning("Feature ID " + feature.getId() + " is already registered!");
            return;
        }

        features.put(feature.getDisplayName(), feature);
        featuresById.put(feature.getId(), feature);

        plugin.getLogger().info("Registered QOL feature: " + feature.getDisplayName());
    }

    /**
     * Enable all registered features
     */
    private void enableAllFeatures() {
        for (QOLFeature feature : features.values()) {
            try {
                feature.onEnable();
                plugin.getLogger().info("Enabled feature: " + feature.getDisplayName());
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,
                        "Error enabling feature " + feature.getDisplayName(), e);
            }
        }
    }

    /**
     * Get a feature by its display name
     */
    public QOLFeature getFeature(String displayName) {
        return features.get(displayName);
    }

    /**
     * Get a feature by its ID
     */
    public QOLFeature getFeatureById(String id) {
        return featuresById.get(id);
    }

    /**
     * Get all registered features
     */
    public Collection<QOLFeature> getAllFeatures() {
        return new ArrayList<>(features.values());
    }

    /**
     * Get all feature IDs
     */
    public Set<String> getAllFeatureIds() {
        return new HashSet<>(featuresById.keySet());
    }

    /**
     * Get all feature display names
     */
    public Set<String> getAllFeatureNames() {
        return new HashSet<>(features.keySet());
    }

    /**
     * Get features that a player has permission to use
     */
    public List<QOLFeature> getAvailableFeatures(Player player) {
        List<QOLFeature> available = new ArrayList<>();

        for (QOLFeature feature : features.values()) {
            if (player.hasPermission(feature.getPermission())) {
                available.add(feature);
            }
        }

        return available;
    }

    /**
     * Get features that a player has enabled
     */
    public List<QOLFeature> getEnabledFeatures(Player player) {
        List<QOLFeature> enabled = new ArrayList<>();

        for (QOLFeature feature : features.values()) {
            if (feature.canUse(player)) {
                enabled.add(feature);
            }
        }

        return enabled;
    }

    /**
     * Toggle a feature for a player
     */
    public boolean toggleFeature(Player player, String featureId) {
        QOLFeature feature = getFeatureById(featureId);

        if (feature == null) {
            player.sendMessage(ChatColor.RED + "Feature not found: " + featureId);
            return false;
        }

        if (!player.hasPermission(feature.getPermission())) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this feature!");
            return false;
        }

        // Toggle the feature in the player's profile
        plugin.getProfileManager().getProfile(player).toggleFeature(featureId);

        boolean isEnabled = plugin.getProfileManager().getProfile(player).isFeatureEnabled(featureId);
        String status = isEnabled ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled";

        player.sendMessage(ChatColor.YELLOW + "Feature " + ChatColor.AQUA + feature.getDisplayName() +
                ChatColor.YELLOW + " is now " + status + ChatColor.YELLOW + "!");

        return true;
    }

    /**
     * Enable a feature for a player
     */
    public boolean enableFeature(Player player, String featureId) {
        QOLFeature feature = getFeatureById(featureId);

        if (feature == null) {
            player.sendMessage(ChatColor.RED + "Feature not found: " + featureId);
            return false;
        }

        if (!player.hasPermission(feature.getPermission())) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this feature!");
            return false;
        }

        plugin.getProfileManager().getProfile(player).setFeatureEnabled(featureId, true);
        player.sendMessage(ChatColor.GREEN + "Enabled feature: " + ChatColor.AQUA + feature.getDisplayName());

        return true;
    }

    /**
     * Disable a feature for a player
     */
    public boolean disableFeature(Player player, String featureId) {
        QOLFeature feature = getFeatureById(featureId);

        if (feature == null) {
            player.sendMessage(ChatColor.RED + "Feature not found: " + featureId);
            return false;
        }

        plugin.getProfileManager().getProfile(player).setFeatureEnabled(featureId, false);
        player.sendMessage(ChatColor.RED + "Disabled feature: " + ChatColor.AQUA + feature.getDisplayName());

        return true;
    }

    /**
     * Get feature statistics for a player
     */
    public String getPlayerStats(Player player) {
        List<QOLFeature> available = getAvailableFeatures(player);
        List<QOLFeature> enabled = getEnabledFeatures(player);

        return ChatColor.AQUA + "QOL Stats for " + player.getName() + ":\n" +
                ChatColor.YELLOW + "Available features: " + ChatColor.WHITE + available.size() + "\n" +
                ChatColor.GREEN + "Enabled features: " + ChatColor.WHITE + enabled.size() + "\n" +
                ChatColor.RED + "Disabled features: " + ChatColor.WHITE + (available.size() - enabled.size());
    }

    /**
     * List all features with their status for a player
     */
    public List<String> getFeatureList(Player player) {
        List<String> featureList = new ArrayList<>();

        for (QOLFeature feature : getAvailableFeatures(player)) {
            boolean isEnabled = plugin.getProfileManager().getProfile(player).isFeatureEnabled(feature.getId());
            String status = isEnabled ? ChatColor.GREEN + "✓" : ChatColor.RED + "✗";

            featureList.add(status + ChatColor.RESET + " " + ChatColor.AQUA + feature.getDisplayName() +
                    ChatColor.GRAY + " - " + feature.getDescription());
        }

        return featureList;
    }

    /**
     * Reload a specific feature
     */
    public boolean reloadFeature(String featureId) {
        QOLFeature feature = getFeatureById(featureId);

        if (feature == null) {
            plugin.getLogger().warning("Cannot reload feature: " + featureId + " (not found)");
            return false;
        }

        try {
            feature.onDisable();
            feature.onEnable();
            plugin.getLogger().info("Reloaded feature: " + feature.getDisplayName());
            return true;
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error reloading feature " + feature.getDisplayName(), e);
            return false;
        }
    }

    /**
     * Reload all features
     */
    public void reloadAllFeatures() {
        plugin.getLogger().info("Reloading all QOL features...");

        for (QOLFeature feature : features.values()) {
            try {
                feature.onDisable();
                feature.onEnable();
                plugin.getLogger().info("Reloaded feature: " + feature.getDisplayName());
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error reloading feature " + feature.getDisplayName(), e);
            }
        }

        plugin.getLogger().info("Finished reloading all features");
    }

    /**
     * Get the total number of registered features
     */
    public int getFeatureCount() {
        return features.size();
    }
}

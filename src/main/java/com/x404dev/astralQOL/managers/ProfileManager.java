package com.x404dev.astralQOL.managers;

import com.x404dev.astralQOL.AstralQOL;
import com.x404dev.astralQOL.models.PlayerQOLProfile;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileManager {

    private final Map<UUID, PlayerQOLProfile> profiles;
    private final AstralQOL plugin;

    public ProfileManager(AstralQOL plugin) {
        this.plugin = plugin;
        this.profiles = new HashMap<>();
    }

    public PlayerQOLProfile getProfile(Player player) {
        return getProfile(player.getUniqueId());
    }

    public PlayerQOLProfile getProfile(UUID playerId) {
        return profiles.computeIfAbsent(playerId, id -> {
            PlayerQOLProfile profile = new PlayerQOLProfile(id, plugin);
            profile.loadFromConfig();
            return profile;
        });
    }

    public void saveProfile(UUID playerId) {
        PlayerQOLProfile profile = profiles.get(playerId);
        if (profile != null) {
            profile.saveToConfig();
        }
    }

    public void unloadProfile(UUID playerId) {
        PlayerQOLProfile profile = profiles.remove(playerId);
        if (profile != null) {
            profile.saveToConfig();
        }
    }

}

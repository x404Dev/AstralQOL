package com.x404dev.astralQOL.qol;

import com.x404dev.astralQOL.AstralQOL;
import com.x404dev.astralQOL.models.PlayerQOLProfile;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class QOLFeature {

    @Getter
    protected final String id;
    @Getter
    protected final String displayName;
    @Getter
    protected final String description;
    @Getter
    protected final String permission;
    protected final AstralQOL plugin;

    public QOLFeature(String id, String displayName, String description, String permission, AstralQOL plugin) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.permission = permission;
        this.plugin = plugin;
    }

    public abstract void onEnable();
    public abstract void onDisable();

    public boolean canUse(Player player) {
        PlayerQOLProfile profile = plugin.getProfileManager().getProfile(player);
        return player.hasPermission(this.permission) && profile.isFeatureEnabled(this.id);
    }
}

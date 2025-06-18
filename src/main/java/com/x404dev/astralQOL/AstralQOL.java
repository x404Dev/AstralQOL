package com.x404dev.astralQOL;

import com.x404dev.astralQOL.commands.QOLCommand;
import com.x404dev.astralQOL.managers.ProfileManager;
import com.x404dev.astralQOL.managers.QOLManager;
import com.x404dev.astralQOL.utils.AdventureHelper;
import com.x404dev.astralQOL.utils.Commons;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public final class AstralQOL extends JavaPlugin {

    @Getter
    private static AstralQOL instance;

    private BukkitAudiences adventure;
    @Getter
    private AdventureHelper adventureHelper;

    @Getter
    private ProfileManager profileManager;
    @Getter
    private QOLManager qolManager;

    @Override
    public void onEnable() {
        instance = this;
        // Initialize Adventure API
        adventure = BukkitAudiences.create(this);
        // Initialize helper classes
        adventureHelper = new AdventureHelper(adventure);
        profileManager = new ProfileManager(this);
        qolManager = new QOLManager(this);
        qolManager.initialize();

        Commons.registerCommands(new QOLCommand(this));

    }

    @Override
    public void onDisable() {
        instance = null;
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
        profileManager = null;
        if (qolManager != null) {
            qolManager.shutdown();
        }
    }

    public BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Adventure API not initialized");
        }
        return this.adventure;
    }


}

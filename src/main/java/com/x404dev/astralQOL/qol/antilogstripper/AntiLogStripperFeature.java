package com.x404dev.astralQOL.qol.antilogstripper;

import com.x404dev.astralQOL.AstralQOL;
import com.x404dev.astralQOL.qol.QOLFeature;
import com.x404dev.astralQOL.utils.Commons;
import org.bukkit.event.HandlerList;

public class AntiLogStripperFeature extends QOLFeature {

    private AntiLogStripperListener listener;

    public AntiLogStripperFeature(AstralQOL plugin) {
        super("anti-log-stripper",
                "Anti Log Stripper",
                "Prevents accidentally stripping logs with axes",
                "astralqol.antilogstripper",
                plugin);
    }

    @Override
    public void onEnable() {
        listener = new AntiLogStripperListener(this);
        Commons.registerEvents(plugin, listener);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(listener);
    }

}

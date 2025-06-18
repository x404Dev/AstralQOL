package com.x404dev.astralQOL.qol.antilogstripper;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class AntiLogStripperListener implements Listener {

    private final AntiLogStripperFeature feature;

    public AntiLogStripperListener(AntiLogStripperFeature feature) {
        this.feature = feature;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!feature.canUse(event.getPlayer())) return;

        if (shouldPreventLogStripping(event)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("Log stripping prevented!");
        }
    }

    private boolean shouldPreventLogStripping(PlayerInteractEvent event) {
        return feature.canUse(event.getPlayer()) && !event.getPlayer().isSneaking();
    }

}

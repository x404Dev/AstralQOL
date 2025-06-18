package com.x404dev.astralQOL.qol.antilogstripper;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

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
            feature.getPlugin().getAdventureHelper().sendMessage(event.getPlayer(), feature.getPlugin().getAdventureHelper().parseMessage("<green>Log stripping prevented. Hold <b>Shift</b> to strip, or disable protection: <b><gold>/qol toggle anti-log-stripper</gold></b>"));
        }
    }

    private boolean shouldPreventLogStripping(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return false;
        }

        if (!Strippable.isAxe(event.getMaterial())) {
            return false;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || !Strippable.isStrippableBlock(clickedBlock.getType())) {
            return false;
        }

        return !event.getPlayer().isSneaking();
    }

}

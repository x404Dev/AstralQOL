package com.x404dev.astralQOL.utils;

import com.x404dev.astralQOL.AstralQOL;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdventureHelper {

    private final BukkitAudiences adventure;
    private final MiniMessage miniMessage;

    public AdventureHelper(BukkitAudiences adventure) {
        this.adventure = adventure;
        this.miniMessage = MiniMessage.miniMessage();
    }

    /**
     * Sends a message to a command sender
     */
    public void sendMessage(CommandSender sender, Component message) {
        adventure.sender(sender).sendMessage(message);
    }

    /**
     * Sends a message using MiniMessage format
     */
    public void sendMessage(CommandSender sender, String miniMessageString) {
        Component message = miniMessage.deserialize(miniMessageString);
        sendMessage(sender, message);
    }

    public void sendMessage(Player player, Component message) {
        adventure.player(player).sendMessage(message);
    }

    /**
     * Sends an action bar message to a player
     */
    public void sendActionBar(Player player, Component message) {
        adventure.player(player).sendActionBar(message);
    }

    /**
     * Sends an action bar message using MiniMessage format
     */
    public void sendActionBar(Player player, String miniMessageString) {
        Component message = miniMessage.deserialize(miniMessageString);
        sendActionBar(player, message);
    }

    /**
     * Sends a title to a player
     */
    public void sendTitle(Player player, Component title, Component subtitle) {
        adventure.player(player).showTitle(
                net.kyori.adventure.title.Title.title(title, subtitle)
        );
    }

    /**
     * Sends a title using MiniMessage format
     */
    public void sendTitle(Player player, String titleString, String subtitleString) {
        Component title = miniMessage.deserialize(titleString);
        Component subtitle = miniMessage.deserialize(subtitleString);
        sendTitle(player, title, subtitle);
    }

    /**
     * Parses a MiniMessage string to Component
     */
    public Component parseMessage(String miniMessageString) {
        return miniMessage.deserialize(miniMessageString);
    }

    /**
     * Gets the Adventure API instance
     */
    public BukkitAudiences getAdventure() {
        return adventure;
    }

    /**
     * Gets the MiniMessage instance
     */
    public MiniMessage getMiniMessage() {
        return miniMessage;
    }


}

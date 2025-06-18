package com.x404dev.astralQOL.commands;

import com.x404dev.astralQOL.AstralQOL;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class QOLCommand extends BukkitCommand {

    private final AstralQOL plugin;

    public QOLCommand(AstralQOL plugin) {
        super("qol");
        setAliases(Arrays.asList("qualityoflife", "astralqol"));
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length == 0) {
            Component message = Component.text()
                    .append(Component.text("--------------------------------------------------", TextColor.color(0x292524), TextDecoration.BOLD))
                    .append(Component.newline())
                    .append(Component.newline())
                    .append(plugin.getAdventureHelper().parseMessage("   <dark_aqua>x404's</dark_aqua>   <shadow:black><gradient:#38ef7d:#11998e><i><b>AstralQOL</b></i></gradient></shadow>   <i><gray>" + plugin.getDescription().getVersion() + "</gray></i>"))
                    .append(Component.newline())
                    .append(Component.newline())
                    .append(Component.text("   AstralQOL adds a few Quality of Life changes!", NamedTextColor.GRAY))
                    .append(Component.newline())
                    .append(Component.newline())
                    .append(plugin.getAdventureHelper().parseMessage("   <yellow>Do <b><green>/qol help</green></b> to get a list of available commands.</yellow>"))
                    .append(Component.newline())
                    .append(Component.newline())
                    .append(Component.text("--------------------------------------------------", TextColor.color(0x292524), TextDecoration.BOLD))
                    .build();
            plugin.getAdventureHelper().sendMessage(sender, message);
            return true;
        }
        return true;
    }
}

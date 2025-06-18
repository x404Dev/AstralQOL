package com.x404dev.astralQOL.utils;

import com.x404dev.astralQOL.AstralQOL;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;

public class Commons {

    public static void registerCommand(Command command) {
        try {
            final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);

            final CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            commandMap.register(command.getLabel(), command);

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerCommands(Command... commands) {
        for (Command cmd : commands) {
            Commons.registerCommand(cmd);
        }
    }

    public static void registerEvents(AstralQOL instance, Listener... li) {
        for (Listener l : li) {
            Bukkit.getPluginManager().registerEvents(l, instance);
        }
    }

}

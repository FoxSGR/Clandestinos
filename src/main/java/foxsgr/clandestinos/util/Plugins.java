package foxsgr.clandestinos.util;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Plugin related useful methods.
 */
@SuppressWarnings("unused")
public final class Plugins {

    /**
     * Private constructor to hide implicit public one.
     */
    private Plugins() {
        // Should be empty.
    }

    /**
     * Registers a command executor in a plugin.
     *
     * @param plugin          the plugin to register the command executor in.
     * @param name            the name of the command executor.
     * @param commandExecutor the command executor to register.
     */
    public static void registerCommand(JavaPlugin plugin, String name, CommandExecutor commandExecutor) {
        PluginCommand command = plugin.getCommand(name);
        if (command == null) {
            throw new IllegalStateException("Could not register the command \"" + name + "\".");
        }

        command.setExecutor(commandExecutor);
    }
}

package foxsgr.clandestinos.application;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Plugin permission manager.
 */
@SuppressWarnings("WeakerAccess")
public final class PermissionsManager {

    /**
     * The root of the plugin permissions. All permissions are prefixed by this string.
     */
    private static final String PERMISSIONS_ROOT = "clandestinos.";

    /**
     * Private constructor to hide the implicit public one.
     */
    private PermissionsManager() {
        // Should be empty.
    }

    /**
     * Checks whether a player has permission to use a subcommand and warns him if he doesn't.
     *
     * @param player     the player to check and warn if necessary.
     * @param subCommand the subcommand to check if the player has permission
     * @return true if the player has permission of the subcommand, false otherwise.
     */
    public static boolean hasAndWarn(CommandSender player, String subCommand) {
        if (!has(player, subCommand)) {
            LanguageManager.send(player, LanguageManager.NO_PERMISSION);
            return false;
        }

        return true;
    }

    /**
     * Checks whether a player has permission to use a subcommand.
     *
     * @param player     the player to check.
     * @param subCommand the subcommand to check if the player has permission
     * @return true if the player has permission of the subcommand, false otherwise.
     */
    public static boolean has(CommandSender player, String subCommand) {
        return player.hasPermission(PERMISSIONS_ROOT + subCommand.toLowerCase());
    }

    /**
     * Given a set of subcommands, creates a list with only the ones that the player has permission for. (does not warn
     * him if he doesn't)
     *
     * @param player   the player to check.
     * @param commands the commands to filter.
     * @return the filtered list with only the subcommands that the player has permission for.
     */
    public static List<String> commandsWithPermission(CommandSender player, String... commands) {
        List<String> result = new ArrayList<>();

        for (String command : commands) {
            if (has(player, command)) {
                result.add(command);
            }
        }

        return result;
    }
}

package foxsgr.clandestinos.application;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public final class PermissionsManager {

    private static final String PERMISSIONS_ROOT = "clandestinos.";

    /**
     * Private constructor to hide the implicit public one.
     */
    private PermissionsManager() {
        // Should be empty.
    }

    public static boolean hasAndWarn(CommandSender player, String subCommand) {
        if (!has(player, subCommand)) {
            LanguageManager.send(player, LanguageManager.NO_PERMISSION);
            return false;
        }

        return true;
    }

    public static boolean has(CommandSender player, String subCommand) {
        return player.hasPermission(PERMISSIONS_ROOT + subCommand.toLowerCase());
    }

    public static List<String> commandsWithPermission(CommandSender sender, String... commands) {
        List<String> result = new ArrayList<>();

        for (String command : commands) {
            if (has(sender, command)) {
                result.add(command);
            }
        }

        return result;
    }
}

package foxsgr.clandestinos.application;

import org.bukkit.command.CommandSender;

public final class PermissionsManager {

    private static final String PERMISSIONS_ROOT = "clandestinos.";

    /**
     * Private constructor to hide the implicit public one.
     */
    private PermissionsManager() {
        // Should be empty.
    }

    public static boolean hasForSubCommandWarn(CommandSender player, String subCommand) {
        if (!hasForSubCommand(player, subCommand)) {
            player.sendMessage(LanguageManager.getInstance().get(LanguageManager.NO_PERMISSION));
            return false;
        }

        return true;
    }

    public static boolean hasForSubCommand(CommandSender player, String subCommand) {
        return player.hasPermission(PERMISSIONS_ROOT + subCommand.toLowerCase());
    }
}
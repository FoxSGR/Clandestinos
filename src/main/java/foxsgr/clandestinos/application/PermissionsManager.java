package foxsgr.clandestinos.application;

import org.bukkit.command.CommandSender;

public class PermissionsManager {

    private static final String PERMISSIONS_ROOT = "clandestinos.";

    public static boolean hasForSubCommandWarn(CommandSender player, String subCommand) {
        if (!hasForSubCommand(player, subCommand)) {
            player.sendMessage(LanguageManager.get(LanguageManager.NO_PERMISSION));
            return false;
        }

        return true;
    }

    public static boolean hasForSubCommand(CommandSender player, String subCommand) {
        return player.hasPermission(PERMISSIONS_ROOT + subCommand.toLowerCase());
    }
}

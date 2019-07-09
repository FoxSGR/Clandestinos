package foxsgr.clandestinos.application;

import org.bukkit.command.CommandSender;

public class PermissionsManager {

    private static final String PERMISSIONS_ROOT = "clandestinos.";

    public static boolean hasPermissionSubCommandWarn(CommandSender player, String subCommand) {
        if (!hasPermissionSubCommand(player, subCommand)) {
            player.sendMessage(LanguageManager.get(LanguageManager.NO_PERMISSION));
            return false;
        }

        return true;
    }

    public static boolean hasPermissionSubCommand(CommandSender player, String subCommand) {
        return player.hasPermission(PERMISSIONS_ROOT + subCommand.toLowerCase());
    }
}

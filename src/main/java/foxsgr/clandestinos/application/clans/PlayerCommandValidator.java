package foxsgr.clandestinos.application.clans;

import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.application.PermissionsManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Validates a command that should be executed by a player.
 */
final class PlayerCommandValidator {

    /**
     * Private constructor to hide the implicit public one.
     */
    private PlayerCommandValidator() {
        // Should be empty.
    }

    /**
     * Validates a command that should be executed by a player.
     *
     * @param sender              the sender that should be a player.
     * @param args                the command arguments.
     * @param minArgsLength       the minimum amount of arguments.
     * @param underMinArgsMessage the message to send if the number of arguments is less than the minimum.
     * @return true if the command is valid, false otherwise.
     */
    static boolean validate(CommandSender sender, String[] args, int minArgsLength, String underMinArgsMessage) {
        if (!PermissionsManager.hasForSubCommandWarn(sender, args[0])) {
            return false;
        }

        ensureIsPlayer(sender);

        LanguageManager languageManager = LanguageManager.getInstance();
        if (args.length < minArgsLength) {
            sender.sendMessage(languageManager.get(underMinArgsMessage));
            return false;
        }

        return true;
    }

    static Player ensureIsPlayer(CommandSender sender) {
        LanguageManager languageManager = LanguageManager.getInstance();

        if (!(sender instanceof Player)) {
            sender.sendMessage(languageManager.get(LanguageManager.MUST_BE_PLAYER));
            return null;
        }

        return (Player) sender;
    }
}

package foxsgr.clandestinos.application;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class PlayerCommandValidator {

    public static boolean validate(CommandSender sender, String[] args, int minArgsLength, String underMinArgsMessage) {
        if (!PermissionsManager.hasForSubCommandWarn(sender, args[0])) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(LanguageManager.get(LanguageManager.MUST_BE_PLAYER));
            return false;
        }

        if (args.length < minArgsLength) {
            sender.sendMessage(LanguageManager.get(underMinArgsMessage));
            return false;
        }

        return true;
    }
}

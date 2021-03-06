package foxsgr.clandestinos.application;

import foxsgr.clandestinos.application.config.I18n;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.util.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Validates a command that should be executed by a player.
 */
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public final class CommandValidator {

    /**
     * Private constructor to hide the implicit public one.
     */
    private CommandValidator() {
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
    public static boolean validate(CommandSender sender, String[] args, int minArgsLength, String underMinArgsMessage) {
        return validateAnySender(sender, args, minArgsLength, underMinArgsMessage) && playerFromSender(sender) != null;
    }

    public static boolean validateAnySender(CommandSender sender, String[] args, int minArgsLength, String underMinArgsMessage) {
        if (!PermissionsManager.hasAndWarn(sender, args[0])) {
            return false;
        }

        if (args.length < minArgsLength) {
            I18n.send(sender, underMinArgsMessage);
            return false;
        }

        return true;
    }

    public static Player playerFromSender(CommandSender sender) {
        if (!(sender instanceof Player)) {
            I18n.send(sender, I18n.MUST_BE_PLAYER);
            return null;
        }

        return (Player) sender;
    }

    public static Pair<Clan, ClanPlayer> validateClanLeader(CommandSender sender, String[] args, int minArgsLength, String underMinArgsMessage) {
        if (!validate(sender, args, minArgsLength, underMinArgsMessage)) {
            return null;
        }

        return Finder.findClanLeader(sender);
    }

    public static Pair<Clan, ClanPlayer> validateClanOwner(CommandSender sender, String[] args, int minArgsLength, String underMinArgsMessage) {
        if (!validate(sender, args, minArgsLength, underMinArgsMessage)) {
            return null;
        }

        return Finder.findClanOwner(sender);
    }
}

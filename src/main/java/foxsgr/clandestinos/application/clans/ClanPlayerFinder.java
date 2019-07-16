package foxsgr.clandestinos.application.clans;

import foxsgr.clandestinos.application.ConfigManager;
import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanPlayerRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Application class to find a player in the plugin's database.
 */
public final class ClanPlayerFinder {

    /**
     * Private constructor to hide the implicit public one.
     */
    private ClanPlayerFinder() {
        // Should be empty.
    }

    /**
     * Finds a player in the plugin's database, if it exists. If it doesn't exist, it's persisted in the database.
     *
     * @param player the player to look for.
     * @return the found or created player.
     */
    public static ClanPlayer get(Player player) {
        String id = idFromPlayer(player);

        ClanPlayerRepository clanPlayerRepository = PersistenceContext.repositories().players();
        ClanPlayer found = clanPlayerRepository.find(id);
        if (found == null) {
            found = new ClanPlayer(id);
            clanPlayerRepository.save(found);
        }

        return found;
    }

    /**
     * Extracts the ID from a player. If online mode is on, the ID is the player's UUID, otherwise it's the player's
     * name.
     *
     * @param player the player to extract the ID from.
     * @return the extracted ID.
     */
    public static String idFromPlayer(Player player) {
        if (ConfigManager.getInstance().getBoolean(ConfigManager.ONLINE_MODE)) {
            return player.getUniqueId().toString();
        } else {
            return player.getName();
        }
    }

    static ClanPlayer fromSenderInClan(CommandSender sender) {
        Player player = PlayerCommandValidator.ensureIsPlayer(sender);
        if (player == null) {
            return null;
        }

        ClanPlayerRepository clanPlayerRepository = PersistenceContext.repositories().players();
        LanguageManager languageManager = LanguageManager.getInstance();

        String id = ClanPlayerFinder.idFromPlayer(player);
        ClanPlayer clanPlayer = clanPlayerRepository.find(id);
        if (clanPlayer == null || !clanPlayer.inClan()) {
            sender.sendMessage(languageManager.get(LanguageManager.MUST_BE_IN_CLAN));
            return null;
        }

        return clanPlayer;
    }
}

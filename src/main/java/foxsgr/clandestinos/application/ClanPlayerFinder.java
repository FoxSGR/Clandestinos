package foxsgr.clandestinos.application;

import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanPlayerRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import org.bukkit.entity.Player;

/**
 * Application class to find a player in the plugin's database.
 */
final class ClanPlayerFinder {

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
    static ClanPlayer find(Player player) {
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
    private static String idFromPlayer(Player player) {
        if (ConfigManager.getBoolean(ConfigManager.ONLINE_MODE)) {
            return player.getUniqueId().toString();
        } else {
            return player.getName();
        }
    }
}

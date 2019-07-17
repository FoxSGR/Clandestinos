package foxsgr.clandestinos.application;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.persistence.PlayerRepository;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

/**
 * Application class with methods related to finding entities.
 */
public final class Finder {

    /**
     * Private constructor to hide the implicit public one.
     */
    private Finder() {
        // Should be empty.
    }

    /**
     * Finds a player in the plugin's database, if it exists. If it doesn't exist, it's persisted in the database.
     *
     * @param player the player to look for.
     * @return the found or created player.
     */
    public static ClanPlayer getPlayer(Player player) {
        String id = idFromPlayer(player);

        PlayerRepository playerRepository = PersistenceContext.repositories().players();
        ClanPlayer found = playerRepository.find(id);
        if (found == null) {
            found = new ClanPlayer(id);
            playerRepository.save(found);
        }

        return found;
    }

    @Nullable
    public static ClanPlayer playerByName(CommandSender sender, String name) {
        String id = idFromName(name);

        PlayerRepository playerRepository = PersistenceContext.repositories().players();
        ClanPlayer found = playerRepository.find(id);
        if (found == null) {
            LanguageManager languageManager = LanguageManager.getInstance();
            sender.sendMessage(languageManager.get(LanguageManager.UNKNOWN_PLAYER_CLAN));
        }

        return found;
    }

    @Nullable
    public static Clan clanByTag(CommandSender sender, String tag) {
        ClanRepository clanRepository = PersistenceContext.repositories().clans();
        Clan found = clanRepository.findByTag(tag);
        if (found == null) {
            LanguageManager languageManager = LanguageManager.getInstance();
            sender.sendMessage(languageManager.get(LanguageManager.CLAN_DOESNT_EXIST));
        }

        return found;
    }

    public static Set<ClanPlayer> playersInClan(Clan clan) {
        Set<ClanPlayer> result = new HashSet<>();

        PlayerRepository playerRepository = PersistenceContext.repositories().players();
        for (String id : clan.allPlayers()) {
            ClanPlayer player = playerRepository.find(id);
            if (player == null) {
                ClanLogger.getLogger().log(Level.WARNING,
                        "The player {0} was not found but the clan {1} has it as its member.",
                        new String[] {id, clan.tag().withoutColor().value()});
                continue;
            }

            result.add(player);
        }

        return result;
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

    public static String idFromName(String name) {
        if (ConfigManager.getInstance().getBoolean(ConfigManager.ONLINE_MODE)) {
            // TODO: return UUID to player name
            throw new UnsupportedOperationException("UUID to player name not implemented yet");
        } else {
            return name;
        }
    }

    public static ClanPlayer fromSenderInClan(CommandSender sender) {
        Player player = PlayerCommandValidator.playerFromSender(sender);
        if (player == null) {
            return null;
        }

        PlayerRepository playerRepository = PersistenceContext.repositories().players();
        LanguageManager languageManager = LanguageManager.getInstance();

        String id = Finder.idFromPlayer(player);
        ClanPlayer clanPlayer = playerRepository.find(id);
        if (clanPlayer == null || !clanPlayer.inClan()) {
            sender.sendMessage(languageManager.get(LanguageManager.MUST_BE_IN_CLAN));
            return null;
        }

        return clanPlayer;
    }

    public static String nameFromId(String id) {
        if (ConfigManager.getInstance().getBoolean(ConfigManager.ONLINE_MODE)) {
            // TODO: return UUID to player name
            throw new UnsupportedOperationException("UUID to player name not implemented yet");
        } else {
            return id;
        }
    }
}

package foxsgr.clandestinos.application;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.persistence.PlayerRepository;
import foxsgr.clandestinos.util.Pair;
import foxsgr.clandestinos.util.TextUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

/**
 * Application class with methods related to finding entities.
 */
@SuppressWarnings("WeakerAccess")
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
    @NotNull
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

    /**
     * Finds the ClanPlayer that corresponds to a player name. If it's not found, a warning message is sent to him.
     *
     * @param sender the command sender to inform if the player was not found.
     * @param name   the name of the player to find.
     * @return the found ClanPlayer or null if it wasn't found.
     */
    @Nullable
    public static ClanPlayer playerByName(CommandSender sender, String name) {
        String id = idFromName(name);

        PlayerRepository playerRepository = PersistenceContext.repositories().players();
        ClanPlayer found = playerRepository.find(id);
        if (found == null) {
            LanguageManager.send(sender, LanguageManager.UNKNOWN_PLAYER_CLAN);
        }

        return found;
    }

    /**
     * Finds a clan by its tag (ignoring letter case and colors) and sends a message a command sender if it's not
     * found.
     *
     * @param sender the command sender to inform if the clan doesn't exist.
     * @param tag    the tag of the clan to find.
     * @return the found clan or null if it wasn't found.
     */
    @Nullable
    public static Clan clanByTag(CommandSender sender, String tag) {
        tag = TextUtil.stripColorAndFormatting(tag);
        ClanRepository clanRepository = PersistenceContext.repositories().clans();
        Clan found = clanRepository.findByTag(tag);
        if (found == null) {
            LanguageManager.send(sender, LanguageManager.CLAN_DOESNT_EXIST);
        }

        return found;
    }

    /**
     * Finds a player's clan, ensuring that it exists. If it doesn't, throws an unchecked exception. Only call if you're
     * sure that the clan exists.
     *
     * @param player the player whose clan should be found.
     * @return the found clan.
     * @throws IllegalStateException thrown if the player's clan is not found.
     */
    @NotNull
    public static Clan findClanEnsureExists(ClanPlayer player) {
        ClanRepository clanRepository = PersistenceContext.repositories().clans();
        Clan clan = player.clan().map(tag -> {
            Clan foundClan = clanRepository.findByTag(tag.withoutColor().value());
            if (foundClan == null) {
                throw new IllegalStateException(
                        "The clan with tag " + tag.withoutColor().value() + " couldn't be found.");
            }

            return foundClan;
        }).orElse(null);

        if (clan == null) {
            throw new IllegalStateException(player.id() + " is not in a clan.");
        }

        return clan;
    }

    /**
     * Finds all of the players in a clan.
     *
     * @param clan the clan to find the players that belong to it.
     * @return the players in the clan.
     */
    public static Set<ClanPlayer> playersInClan(Clan clan) {
        Set<ClanPlayer> result = new HashSet<>();

        PlayerRepository playerRepository = PersistenceContext.repositories().players();
        for (String id : clan.allPlayers()) {
            ClanPlayer player = playerRepository.find(id);
            if (player == null) {
                ClanLogger.getLogger().log(Level.WARNING,
                        "The player {0} was not found but the clan {1} has it as its member.",
                        new String[] {id, clan.simpleTag()});
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
    public static String idFromPlayer(OfflinePlayer player) {
        if (ConfigManager.getInstance().getBoolean(ConfigManager.USE_UUIDS)) {
            return player.getUniqueId().toString();
        } else {
            return player.getName();
        }
    }

    /**
     * Finds a ClanPlayer and his clan from a command sender and warns him if he is not in a clan.
     *
     * @param sender the command sender to find the corresponding clan and ClanPlayer.
     * @return the found clan and ClanPlayer or null if they are not found or applicable.
     */
    @Nullable
    public static Pair<Clan, ClanPlayer> fromSenderInClan(CommandSender sender) {
        Player player = CommandValidator.playerFromSender(sender);
        if (player == null) {
            return null;
        }

        PlayerRepository playerRepository = PersistenceContext.repositories().players();
        String id = Finder.idFromPlayer(player);
        ClanPlayer clanPlayer = playerRepository.find(id);
        if (clanPlayer == null) {
            LanguageManager.send(sender, LanguageManager.MUST_BE_IN_CLAN);
            return null;
        }

        ClanRepository clanRepository = PersistenceContext.repositories().clans();
        Clan clan = clanPlayer.clan().map(tag -> clanRepository.findByTag(tag.withoutColor().value().toLowerCase()))
                .orElse(null);
        if (clan == null) {
            LanguageManager.send(sender, LanguageManager.MUST_BE_IN_CLAN);
            return null;
        }

        return new Pair<>(clan, clanPlayer);
    }

    @Nullable
    public static Clan clanFromPlayer(CommandSender sender, ClanPlayer clanPlayer) {
        ClanRepository clanRepository = PersistenceContext.repositories().clans();

        Optional<ClanTag> clanTag = clanPlayer.clan();
        if (!clanTag.isPresent()) {
            LanguageManager.send(sender, LanguageManager.MUST_BE_IN_CLAN);
            return null;
        }

        Clan clan = clanRepository.findByTag(clanTag.get().withoutColor().value());
        if (clan == null) {
            LanguageManager.send(sender, LanguageManager.MUST_BE_IN_CLAN);
            return null;
        }

        return clan;
    }

    /**
     * Finds the clan and ClanPlayer that correspond to a command sender who is the leader of a clan. If he is not, he
     * is warned that he must be one.
     *
     * @param sender the command sender to find the corresponding clan and ClanPlayer.
     * @return the found clan and ClanPlayer or null if they are not found or applicable.
     */
    @Nullable
    public static Pair<Clan, ClanPlayer> findClanLeader(CommandSender sender) {
        Pair<Clan, ClanPlayer> clanLeader = fromSenderInClan(sender);
        if (clanLeader == null) {
            return null;
        }

        if (!clanLeader.first.isLeader(clanLeader.second)) {
            LanguageManager.send(sender, LanguageManager.MUST_BE_LEADER);
            return null;
        }

        return clanLeader;
    }

    /**
     * Finds the clan and ClanPlayer that correspond to a command sender who is the owner of a clan. If he is not, he is
     * warned that he must be one.
     *
     * @param sender the command sender to find the corresponding clan and ClanPlayer.
     * @return the found clan and ClanPlayer or null if they are not found or applicable.
     */
    @Nullable
    public static Pair<Clan, ClanPlayer> findClanOwner(CommandSender sender) {
        Pair<Clan, ClanPlayer> clanOwner = fromSenderInClan(sender);
        if (clanOwner == null) {
            return null;
        }

        if (!clanOwner.first.isOwner(clanOwner.second)) {
            LanguageManager.send(sender, LanguageManager.MUST_BE_OWNER);
            return null;
        }

        return clanOwner;
    }

    /**
     * Finds the ClanPlayer that corresponds to an OfflinePlayer.
     *
     * @param player the player to find the corresponding ClanPlayer.
     * @return the found ClanPlayer or null if it doesn't exist.
     */
    @Nullable
    public static ClanPlayer findPlayer(OfflinePlayer player) {
        String id = idFromPlayer(player);
        PlayerRepository playerRepository = PersistenceContext.repositories().players();
        return playerRepository.find(id);
    }

    /**
     * Finds the ID of a player by its name. If the config states that UUIDs should be used, the name is translated to
     * the corresponding UUID. Otherwise, the name is returned.
     *
     * @param name the name to extract the ID from.
     * @return the corresponding UUID if the config states that UUIDs should be used, otherwise the name.
     */
    public static String idFromName(String name) {
        if (ConfigManager.getInstance().getBoolean(ConfigManager.USE_UUIDS)) {
            // TODO: return UUID to player name
            throw new UnsupportedOperationException("UUID to player name not implemented yet");
        } else {
            return name;
        }
    }

    /**
     * Finds the name of a player from his ID. If the config states that UUIDs should be used, the ID is translated to
     * the corresponding player name. Otherwise, the ID is returned, because it is the name.
     *
     * @param id the ID to extract the player name from.
     * @return the extracted player name.
     */
    public static String nameFromId(String id) {
        if (ConfigManager.getInstance().getBoolean(ConfigManager.USE_UUIDS)) {
            // TODO: return UUID to player name
            throw new UnsupportedOperationException("UUID to player name not implemented yet");
        } else {
            return id;
        }
    }
}

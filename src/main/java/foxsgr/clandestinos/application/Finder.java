package foxsgr.clandestinos.application;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.persistence.PlayerRepository;
import foxsgr.clandestinos.util.Pair;
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

    @Nullable
    public static Clan clanByTag(CommandSender sender, String tag) {
        ClanRepository clanRepository = PersistenceContext.repositories().clans();
        Clan found = clanRepository.findByTag(tag);
        if (found == null) {
            LanguageManager.send(sender, LanguageManager.CLAN_DOESNT_EXIST);
        }

        return found;
    }

    public static Clan findClanEnsureExists(ClanPlayer player) {
        ClanRepository clanRepository = PersistenceContext.repositories().clans();
        Clan clan = player.clan().map(tag -> {
            Clan foundClan = clanRepository.findByTag(tag.withoutColor().value());
            if (foundClan == null) {
                throw new IllegalStateException("The clan with tag " + tag.withoutColor().value() + " couldn't be found.");
            }

            return foundClan;
        }).orElse(null);

        if (clan == null) {
            throw new IllegalStateException(player.id() + " is not in a clan.");
        }

        return clan;
    }

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

    public static String idFromName(String name) {
        if (ConfigManager.getInstance().getBoolean(ConfigManager.USE_UUIDS)) {
            // TODO: return UUID to player name
            throw new UnsupportedOperationException("UUID to player name not implemented yet");
        } else {
            return name;
        }
    }

    @Nullable
    public static ClanPlayer fromSenderInClan(CommandSender sender) {
        Player player = CommandValidator.playerFromSender(sender);
        if (player == null) {
            return null;
        }

        PlayerRepository playerRepository = PersistenceContext.repositories().players();
        String id = Finder.idFromPlayer(player);
        ClanPlayer clanPlayer = playerRepository.find(id);
        if (clanPlayer == null || !clanPlayer.inClan()) {
            LanguageManager.send(sender, LanguageManager.MUST_BE_IN_CLAN);
            return null;
        }

        return clanPlayer;
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

    @Nullable
    public static Pair<Clan, ClanPlayer> findClanLeader(CommandSender sender) {
        ClanPlayer leader = fromSenderInClan(sender);
        if (leader == null) {
            return null;
        }

        Clan clan = findClanEnsureExists(leader);
        if (!clan.isLeader(leader)) {
            LanguageManager.send(sender, LanguageManager.MUST_BE_LEADER);
            return null;
        }

        return new Pair<>(clan, leader);
    }

    @Nullable
    public static Pair<Clan, ClanPlayer> findClanOwner(CommandSender sender) {
        ClanPlayer owner = fromSenderInClan(sender);
        if (owner == null) {
            return null;
        }

        Clan clan = findClanEnsureExists(owner);
        if (!clan.isOwner(owner)) {
            LanguageManager.send(sender, LanguageManager.MUST_BE_OWNER);
            return null;
        }

        return new Pair<>(clan, owner);
    }

    @Nullable
    public static ClanPlayer findPlayer(OfflinePlayer player) {
        String id = idFromPlayer(player);
        PlayerRepository playerRepository = PersistenceContext.repositories().players();
        return playerRepository.find(id);
    }

    public static String nameFromId(String id) {
        if (ConfigManager.getInstance().getBoolean(ConfigManager.USE_UUIDS)) {
            // TODO: return UUID to player name
            throw new UnsupportedOperationException("UUID to player name not implemented yet");
        } else {
            return id;
        }
    }
}

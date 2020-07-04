package foxsgr.clandestinos.persistence.yaml;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.PlayerRepository;
import foxsgr.clandestinos.util.TaskUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PlayerRepositoryYAML extends YAMLRepository implements PlayerRepository {

    private static final Map<String, ClanPlayer> cache = new ConcurrentHashMap<>();

    private static final String CLAN_TAG = "clan-tag";
    private static final Lock MUTEX = new ReentrantLock();

    private static final String KILL_COUNT_FIELD = "kill-count";
    private static final String DEATH_COUNT_FIELD = "death-count";
    private static final String FRIENDLY_FIRE_FIELD = "friendly-fire-enabled";

    PlayerRepositoryYAML(JavaPlugin plugin) {
        super(plugin, "players");
    }

    @Override
    public ClanPlayer find(String id) {
        ClanPlayer clanPlayer = cache.get(id);
        if (clanPlayer != null) {
            return clanPlayer;
        }

        MUTEX.lock();
        ConfigurationSection playerSection = file(id);
        MUTEX.unlock();

        if (playerSection == null) {
            return null;
        }

        return constructPlayer(playerSection, id);
    }

    @Override
    public void save(ClanPlayer clanPlayer) {
        String id = clanPlayer.id();
        cache.put(id, clanPlayer);

        FileConfiguration fileConfiguration = loadFile(id.toLowerCase());
        fileConfiguration.set(KILL_COUNT_FIELD, clanPlayer.killCount().value());
        fileConfiguration.set(DEATH_COUNT_FIELD, clanPlayer.deathCount().value());
        fileConfiguration.set(FRIENDLY_FIRE_FIELD, clanPlayer.isFriendlyFireEnabled());

        Optional<ClanTag> clanTag = clanPlayer.clan();
        if (clanTag.isPresent()) {
            fileConfiguration.set(CLAN_TAG, clanTag.get().withoutColor().value().toLowerCase());
        } else {
            fileConfiguration.set(CLAN_TAG, null);
        }

        MUTEX.lock();
        saveFile(fileConfiguration, id.toLowerCase());
        MUTEX.unlock();
    }

    @Override
    public void load(String id) {
        MUTEX.lock();
        ClanPlayer clanPlayer = find(id);
        MUTEX.unlock();

        if (clanPlayer != null) {
            cache.put(clanPlayer.id(), clanPlayer);
        }
    }

    @Override
    public void unload(String id) {
        cache.remove(id);
    }

    @Override
    public void leaveFromClan(Clan clan) {
        List<String> ids = clan.allPlayers();

        TaskUtil.runAsync(MUTEX, plugin, () -> {
            for (String id : ids) {
                FileConfiguration fileConfiguration = loadFile(id.toLowerCase());
                fileConfiguration.set(CLAN_TAG, null);
                saveFile(fileConfiguration, id.toLowerCase());

                if (cache.containsKey(id)) {
                    cache.put(id, constructPlayer(fileConfiguration, id));
                }
            }
        });
    }

    public static Map<String, ClanPlayer> loadedPlayers() {
        return new HashMap<>(cache);
    }

    private static ClanPlayer constructPlayer(ConfigurationSection playerSection, String id) {
        playerSection.addDefault(FRIENDLY_FIRE_FIELD, ClanPlayer.DEFAULT_FRIENDLY_FIRE_ENABLED);

        int killCount = playerSection.getInt(KILL_COUNT_FIELD);
        int deathCount = playerSection.getInt(DEATH_COUNT_FIELD);
        String clanTag = playerSection.getString(CLAN_TAG);
        boolean friendlyFireEnabled = playerSection.getBoolean(FRIENDLY_FIRE_FIELD);
        return new ClanPlayer(id, killCount, deathCount, clanTag, friendlyFireEnabled);
    }
}

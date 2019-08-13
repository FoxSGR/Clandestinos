package foxsgr.clandestinos.persistence.yaml;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.util.TaskUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

class ClanRepositoryYAML extends YAMLRepository implements ClanRepository {

    private Map<String, Clan> cache;

    private static final String NAME_FIELD = "name";
    private static final String OWNER_FIELD = "owner";
    private static final String TAG_FIELD = "tag";
    private static final String LEADERS_FIELD = "leaders";
    private static final String MEMBERS_FIELD = "members";
    private static final String ENEMY_CLANS_FIELD = "enemy-clans";
    private static final String FRIENDLY_FIRE_FIELD = "friendly-fire-enabled";

    private static final Lock MUTEX = new ReentrantLock();

    ClanRepositoryYAML(JavaPlugin plugin) {
        super(plugin, "clans");
        cache = new ConcurrentHashMap<>();
    }

    @Override
    public void load() {
        File[] files = listFiles();
        if (files.length == cache.size()) {
            return;
        }

        for (File file : files) {
            FileConfiguration fileConfiguration = new YamlConfiguration();
            try {
                fileConfiguration.load(file);
                constructClan(fileConfiguration);
            } catch (InvalidConfigurationException | IOException e) {
                logger().log(Level.WARNING, "Could not load the clan in " + file.getName(), e);
            }
        }
    }

    @Override
    public Clan find(String tag) {
        String lowerCaseTag = tag.toLowerCase();

        // Mutex lock in case files are being removed
        MUTEX.lock();
        if (cache.containsKey(lowerCaseTag)) {
            MUTEX.unlock();
            return cache.get(lowerCaseTag);
        }

        FileConfiguration fileConfiguration = file(lowerCaseTag);
        MUTEX.unlock();
        if (fileConfiguration == null) {
            return null;
        }

        return constructClan(fileConfiguration);
    }

    @Override
    public List<Clan> findAll() {
        return new ArrayList<>(cache.values());
    }

    @Override
    public boolean add(Clan clan) {
        // Mutex lock in case files are being removed
        MUTEX.lock();
        File[] clanFiles = repositoryFolder.listFiles();
        MUTEX.unlock();

        if (clanFiles != null) {
            String tagWithoutColor = clan.simpleTag();
            for (File clanFile : clanFiles) {
                String fileName = clanFile.getName().replace(".yml", "");
                if (tagWithoutColor.equalsIgnoreCase(fileName)) {
                    return false;
                }
            }
        }

        fillConfiguration(new YamlConfiguration(), clan);
        return true;
    }

    @Override
    public void update(Clan clan) {
        TaskUtil.runAsync(MUTEX, plugin, () -> {
            FileConfiguration fileConfiguration = file(clan.simpleTag());
            if (fileConfiguration == null) {
                throw new IllegalStateException(
                        "Could not update the clan " + clan.simpleTag() + " because it was not found.");
            }

            fillConfiguration(fileConfiguration, clan);
        });
    }

    @Override
    public void remove(Clan clan) {
        TaskUtil.runAsync(MUTEX, plugin, () -> {
            String properTag = clan.simpleTag();
            if (makeFile(properTag).delete()) {
                cache.remove(properTag);

                File[] clanFiles = listFiles();
                for (File clanFile : clanFiles) {
                    String clanFileName = clanFile.getName();
                    FileConfiguration fileConfiguration = file(clanFileName);
                    if (fileConfiguration == null) {
                        // Should never happen
                        continue;
                    }

                    List<String> enemyClans = fileConfiguration.getStringList(ENEMY_CLANS_FIELD);
                    if (enemyClans.contains(properTag)) {
                        enemyClans.remove(properTag);
                        fileConfiguration.set(ENEMY_CLANS_FIELD, enemyClans);
                        saveFile(fileConfiguration, clanFileName);
                        constructClan(fileConfiguration);
                    }
                }
            } else {
                logger().log(Level.WARNING, "Could not delete the clan file {0}", properTag);
            }
        });
    }

    private Clan constructClan(FileConfiguration fileConfiguration) {
        fileConfiguration.addDefault(FRIENDLY_FIRE_FIELD, Clan.DEFAULT_FRIENDLY_FIRE_SETTING);
        fileConfiguration.options().copyDefaults(true);

        String name = fileConfiguration.getString(NAME_FIELD);
        String owner = fileConfiguration.getString(OWNER_FIELD);
        String coloredTag = fileConfiguration.getString(TAG_FIELD);
        List<String> leaders = fileConfiguration.getStringList(LEADERS_FIELD);
        List<String> members = fileConfiguration.getStringList(MEMBERS_FIELD);
        List<String> enemyClans = fileConfiguration.getStringList(ENEMY_CLANS_FIELD);
        boolean friendlyFireEnabled = fileConfiguration.getBoolean(FRIENDLY_FIRE_FIELD);
        Clan clan = new Clan(coloredTag, name, owner, leaders, members, enemyClans, friendlyFireEnabled);
        cache.put(clan.simpleTag(), clan);
        return clan;
    }

    private void fillConfiguration(FileConfiguration fileConfiguration, Clan clan) {
        cache.put(clan.simpleTag(), clan);
        fileConfiguration.set(TAG_FIELD, clan.tag().value());
        fileConfiguration.set(NAME_FIELD, clan.name().value());
        fileConfiguration.set(OWNER_FIELD, clan.owner());
        fileConfiguration.set(LEADERS_FIELD, clan.leaders());
        fileConfiguration.set(MEMBERS_FIELD, clan.members());
        fileConfiguration.set(ENEMY_CLANS_FIELD, clan.enemyClans());
        MUTEX.lock();
        saveFile(fileConfiguration, clan.simpleTag());
        MUTEX.unlock();
    }
}

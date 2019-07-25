package foxsgr.clandestinos.persistence.yaml;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.persistence.ClanRepository;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

class ClanRepositoryYAML extends YAMLRepository implements ClanRepository {

    private Map<String, Clan> cache;

    private static final String NAME_FIELD = "name";
    private static final String OWNER_FIELD = "owner";
    private static final String TAG_FIELD = "tag";
    private static final String LEADERS_FIELD = "leaders";
    private static final String MEMBERS_FIELD = "members";
    private static final String ENEMY_CLANS_FIELD = "enemy-clans";

    ClanRepositoryYAML(JavaPlugin plugin) {
        super(plugin, "clans");
        cache = new ConcurrentHashMap<>();
    }

    @Override
    public Clan findByTag(String tag) {
        String lowerCaseTag = tag.toLowerCase();
        if (cache.containsKey(lowerCaseTag)) {
            return cache.get(lowerCaseTag);
        }

        FileConfiguration fileConfiguration = file(lowerCaseTag);
        if (fileConfiguration == null) {
            return null;
        }

        return constructClan(fileConfiguration);
    }

    @Override
    public boolean add(Clan clan) {
        File[] clanFiles = repositoryFolder.listFiles();

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
        FileConfiguration fileConfiguration = file(clan.simpleTag());
        if (fileConfiguration == null) {
            throw new IllegalStateException(
                    "Could not update the clan " + clan.simpleTag() + " because it was not found.");
        }

        fillConfiguration(fileConfiguration, clan);
    }

    @Override
    public synchronized void remove(Clan clan) {
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
                    cache.put(clanFile.getName(), constructClan(fileConfiguration));
                }
            }
        } else {
            logger().log(Level.WARNING, "Could not delete the clan file {0}", properTag);
        }
    }

    private Clan constructClan(FileConfiguration fileConfiguration) {
        String name = fileConfiguration.getString(NAME_FIELD);
        String owner = fileConfiguration.getString(OWNER_FIELD);
        String coloredTag = fileConfiguration.getString(TAG_FIELD);
        List<String> leaders = fileConfiguration.getStringList(LEADERS_FIELD);
        List<String> members = fileConfiguration.getStringList(MEMBERS_FIELD);
        List<String> enemyClans = fileConfiguration.getStringList(ENEMY_CLANS_FIELD);
        return new Clan(coloredTag, name, owner, leaders, members, enemyClans);
    }

    private synchronized void fillConfiguration(FileConfiguration fileConfiguration, Clan clan) {
        cache.put(clan.simpleTag(), clan);
        fileConfiguration.set(TAG_FIELD, clan.tag().value());
        fileConfiguration.set(NAME_FIELD, clan.name().value());
        fileConfiguration.set(OWNER_FIELD, clan.owner());
        fileConfiguration.set(LEADERS_FIELD, clan.leaders());
        fileConfiguration.set(MEMBERS_FIELD, clan.members());
        fileConfiguration.set(ENEMY_CLANS_FIELD, clan.enemyClans());
        saveFile(fileConfiguration, clan.simpleTag());
    }
}

package foxsgr.clandestinos.persistence.yaml;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.persistence.ClanRepository;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

class ClanRepositoryYAML extends YAMLRepository implements ClanRepository {

    private Map<String, Clan> cache;

    ClanRepositoryYAML(JavaPlugin plugin) {
        super(plugin, "clans");
        cache = new HashMap<>();
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

        String name = fileConfiguration.getString("name");
        String owner = fileConfiguration.getString("owner");
        String coloredTag = fileConfiguration.getString("tag");
        List<String> leaders = fileConfiguration.getStringList("leaders");
        List<String> members = fileConfiguration.getStringList("members");
        return new Clan(coloredTag, name, owner, leaders, members);
    }

    @Override
    public boolean add(Clan clan) {
        File[] clanFiles = repositoryFolder.listFiles();

        if (clanFiles != null) {
            String tagWithoutColor = properTag(clan);
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
        FileConfiguration fileConfiguration = file(properTag(clan));
        fillConfiguration(fileConfiguration, clan);
    }

    @Override
    public void remove(Clan clan) {
        String properTag = properTag(clan);
        if (!makeFile(properTag).delete()) {
            logger().log(Level.WARNING, "Could not delete the clan file {0}", properTag);
        } else {
            cache.remove(properTag);
        }
    }

    private void fillConfiguration(FileConfiguration fileConfiguration, Clan clan) {
        cache.put(properTag(clan), clan);
        fileConfiguration.set("tag", clan.tag().value());
        fileConfiguration.set("name", clan.name().value());
        fileConfiguration.set("owner", clan.owner());
        fileConfiguration.set("leaders", clan.leaders());
        fileConfiguration.set("members", clan.members());
        saveFile(fileConfiguration, properTag(clan));
    }

    private static String properTag(Clan clan) {
        return clan.tag().withoutColor().value().toLowerCase();
    }
}

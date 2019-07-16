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
        if (cache.containsKey(tag)) {
            return cache.get(tag);
        }

        FileConfiguration fileConfiguration = file(tag);
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
            String tagWithoutColor = clan.tag().withoutColor().value();
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
        FileConfiguration fileConfiguration = file(clan.tag().withoutColor().value());
        fillConfiguration(fileConfiguration, clan);

        cache.put(clan.tag().withoutColor().value(), clan);
    }

    @Override
    public void remove(Clan clan) {
        if (!makeFile(clan.tag().value()).delete()) {
            logger().log(Level.WARNING, "Could not delete the clan file {0}", clan.tag().withoutColor().value());
        } else {
            cache.remove(clan.tag().withoutColor().value());
        }
    }

    private void fillConfiguration(FileConfiguration fileConfiguration, Clan clan) {
        fileConfiguration.set("tag", clan.tag().value());
        fileConfiguration.set("name", clan.name().value());
        fileConfiguration.set("owner", clan.owner());
        fileConfiguration.set("leaders", clan.leaders());
        fileConfiguration.set("members", clan.members());
        update(fileConfiguration, clan.tag().withoutColor().value());
    }
}

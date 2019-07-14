package foxsgr.clandestinos.persistence.yaml;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.persistence.ClanRepository;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

class ClanRepositoryYAML extends YAMLRepository implements ClanRepository {

    private FileConfiguration fileConfiguration;

    ClanRepositoryYAML(JavaPlugin plugin) {
        super(plugin, "clans.yml");
        fileConfiguration = load();
    }

    @Override
    public Clan findByTag(String tag) {
        ConfigurationSection clanSection = fileConfiguration.getConfigurationSection(tag);
        if (clanSection == null) {
            return null;
        }

        String name = clanSection.getString("name");
        String owner = clanSection.getString("owner");
        String coloredTag = clanSection.getString("tag");
        List<String> leaders = clanSection.getStringList("leaders");
        List<String> members = clanSection.getStringList("members");
        return new Clan(coloredTag, name, owner, leaders, members);
    }

    @Override
    public boolean add(Clan clan) {
        String tag = clan.tag().value();
        String tagWithoutColor = clan.tag().withoutColor().value();
        for (String otherClanTag : fileConfiguration.getKeys(false)) {
            if (tagWithoutColor.equalsIgnoreCase(otherClanTag)) {
                return false;
            }
        }

        ConfigurationSection clanSection = fileConfiguration.createSection(tagWithoutColor);
        clanSection.set("tag", tag);
        clanSection.set("name", clan.name().value());
        clanSection.set("owner", clan.owner());
        clanSection.set("leaders", clan.members());
        clanSection.set("members", clan.members());
        update(fileConfiguration);
        return true;
    }
}

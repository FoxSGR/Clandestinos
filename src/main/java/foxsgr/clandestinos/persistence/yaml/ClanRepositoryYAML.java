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
        String tagWithoutColor = clan.tag().withoutColor().value();
        for (String otherClanTag : fileConfiguration.getKeys(false)) {
            if (tagWithoutColor.equalsIgnoreCase(otherClanTag)) {
                return false;
            }
        }

        ConfigurationSection clanSection = fileConfiguration.createSection(tagWithoutColor);
        fillSection(clanSection, clan);
        return true;
    }

    @Override
    public void update(Clan clan) {
        ConfigurationSection clanSection = fileConfiguration.getConfigurationSection(clan.tag().withoutColor().value());
        if (clanSection == null) {
            throw new IllegalStateException("Could not update a clan because it does not exist.");
        }

        fillSection(clanSection, clan);
    }

    @Override
    public void remove(Clan clan) {
        fileConfiguration.set(clan.tag().withoutColor().value(), null);
        update(fileConfiguration);
    }

    private void fillSection(ConfigurationSection clanSection, Clan clan) {
        clanSection.set("tag", clan.tag().value());
        clanSection.set("name", clan.name().value());
        clanSection.set("owner", clan.owner());
        clanSection.set("leaders", clan.leaders());
        clanSection.set("members", clan.members());
        update(fileConfiguration);
    }
}

package foxsgr.clandestinos.persistence.yaml;

import foxsgr.clandestinos.domain.model.Clan;
import foxsgr.clandestinos.persistence.ClanRepository;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

class ClanRepositoryYAML extends YAMLRepository implements ClanRepository {

    ClanRepositoryYAML(@NotNull File configurationFile, @NotNull FileConfiguration fileConfiguration) {
        super(configurationFile, fileConfiguration, "clans");
    }

    @Override
    public Clan find(String tag) {
        ConfigurationSection clanSection = subSection(tag);
        if (clanSection == null) {
            return null;
        }

        String name = clanSection.getString("name");
        String ownerId = clanSection.getString("ownerId");
        String coloredTag = clanSection.getString("colored-tag");
        List<String> members = clanSection.getStringList("members");
        return new Clan(coloredTag, name, ownerId, members);
    }

    @Override
    public boolean save(Clan clan) {
        ConfigurationSection section = section();

        String tag = clan.tag().value();
        for (String otherClanTag : section.getKeys(false)) {
            if (tag.equalsIgnoreCase(otherClanTag)) {
                return false;
            }
        }

        ConfigurationSection clanSection = section.getConfigurationSection(clan.tag().value());
        if (clanSection == null) {
            clanSection = section.createSection(clan.tag().value());
        }

        clanSection.set("name", clan.name().value());
        clanSection.set("owner-id", clan.ownerId());
        clanSection.set("colored-tag", clan.coloredTag().value());
        clanSection.set("members", clan.members());
        return true;
    }

    @Override
    public Clan changeTag(Clan clan, String newTag) {
        return null;
    }
}

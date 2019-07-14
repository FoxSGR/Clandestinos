package foxsgr.clandestinos.persistence.yaml;

import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanPlayerRepository;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

class ClanPlayerRepositoryYAML extends YAMLRepository implements ClanPlayerRepository {

    private List<ClanPlayer> cache;

    ClanPlayerRepositoryYAML(JavaPlugin plugin) {
        super(plugin, "players.yml");
        cache = new ArrayList<>();
    }

    @Override
    public ClanPlayer find(String id) {
        ConfigurationSection playerSection = section(id);
        if (playerSection == null) {
            return null;
        }

        int killCount = playerSection.getInt("kill-count");
        int deathCount = playerSection.getInt("death-count");
        String clanTag = playerSection.getString("clan-tag");
        return new ClanPlayer(id, killCount, deathCount, clanTag);
    }

    @Override
    public void save(ClanPlayer clanPlayer) {
        FileConfiguration fileConfiguration = load();
        ConfigurationSection section = fileConfiguration.getConfigurationSection(clanPlayer.id());
        if (section == null) {
            section = fileConfiguration.createSection(clanPlayer.id());
        }

        section.set("kill-count", clanPlayer.killCount().value());
        section.set("death-count", clanPlayer.deathCount().value());

        ClanTag clanTag = clanPlayer.clan();
        if (clanTag != null) {
            section.set("clan-tag", clanTag.value());
        }

        update(fileConfiguration);
    }
}

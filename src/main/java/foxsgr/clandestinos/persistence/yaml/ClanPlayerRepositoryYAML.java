package foxsgr.clandestinos.persistence.yaml;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanPlayerRepository;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ClanPlayerRepositoryYAML extends YAMLRepository implements ClanPlayerRepository {

    private Map<String, ClanPlayer> cache;

    private static final String CLAN_TAG = "clan-tag";

    ClanPlayerRepositoryYAML(JavaPlugin plugin) {
        super(plugin, "players");
        cache = new HashMap<>();
    }

    @Override
    public ClanPlayer find(String id) {
        ClanPlayer clanPlayer = cache.get(id);
        if (clanPlayer != null) {
            return clanPlayer;
        }

        ConfigurationSection playerSection = file(id);
        if (playerSection == null) {
            return null;
        }

        return constructPlayer(playerSection, id);
    }

    @Override
    public void save(ClanPlayer clanPlayer) {
        String id = clanPlayer.id();
        cache.put(id, clanPlayer);

        FileConfiguration fileConfiguration = loadFile(id);
        fileConfiguration.set("kill-count", clanPlayer.killCount().value());
        fileConfiguration.set("death-count", clanPlayer.deathCount().value());

        ClanTag clanTag = clanPlayer.clan();
        if (clanTag != null) {
            fileConfiguration.set(CLAN_TAG, clanTag.value());
        } else {
            fileConfiguration.set(CLAN_TAG, null);
        }

        update(fileConfiguration, id);
    }

    @Override
    public void load(String id) {
        ClanPlayer clanPlayer = find(id);
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
        List<String> ids = clan.members();
        ids.addAll(clan.leaders());

        for (String id : ids) {
            FileConfiguration fileConfiguration = loadFile(id);
            fileConfiguration.set(CLAN_TAG, null);

            if (cache.containsKey(id)) {
                cache.put(id, constructPlayer(fileConfiguration, id));
            }

            update(fileConfiguration, id);
        }
    }

    private static ClanPlayer constructPlayer(ConfigurationSection playerSection, String id) {
        int killCount = playerSection.getInt("kill-count");
        int deathCount = playerSection.getInt("death-count");
        String clanTag = playerSection.getString(CLAN_TAG);
        return new ClanPlayer(id, killCount, deathCount, clanTag);
    }
}

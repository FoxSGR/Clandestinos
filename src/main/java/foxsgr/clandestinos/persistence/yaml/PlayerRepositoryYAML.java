package foxsgr.clandestinos.persistence.yaml;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.PlayerRepository;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class PlayerRepositoryYAML extends YAMLRepository implements PlayerRepository {

    private Map<String, ClanPlayer> cache;

    private static final String CLAN_TAG = "clan-tag";

    PlayerRepositoryYAML(JavaPlugin plugin) {
        super(plugin, "players");
        cache = new HashMap<>();
    }

    @Override
    public ClanPlayer find(String id) {
        ClanPlayer clanPlayer = cache.get(id.toLowerCase());
        if (clanPlayer != null) {
            return clanPlayer;
        }

        ConfigurationSection playerSection = file(id.toLowerCase());
        if (playerSection == null) {
            return null;
        }

        return constructPlayer(playerSection, id);
    }

    @Override
    public void save(ClanPlayer clanPlayer) {
        String id = clanPlayer.id();
        cache.put(id.toLowerCase(), clanPlayer);

        FileConfiguration fileConfiguration = loadFile(id.toLowerCase());
        fileConfiguration.set("kill-count", clanPlayer.killCount().value());
        fileConfiguration.set("death-count", clanPlayer.deathCount().value());

        Optional<ClanTag> clanTag = clanPlayer.clan();
        if (clanTag.isPresent()) {
            fileConfiguration.set(CLAN_TAG, clanTag.get().withoutColor().value().toLowerCase());
        } else {
            fileConfiguration.set(CLAN_TAG, null);
        }

        saveFile(fileConfiguration, id.toLowerCase());
    }

    @Override
    public void load(String id) {
        ClanPlayer clanPlayer = find(id);
        if (clanPlayer != null) {
            cache.put(clanPlayer.id().toLowerCase(), clanPlayer);
        }
    }

    @Override
    public void unload(String id) {
        cache.remove(id.toLowerCase());
    }

    @Override
    public void leaveFromClan(Clan clan) {
        List<String> ids = clan.allPlayers();
        for (String id : ids) {
            FileConfiguration fileConfiguration = loadFile(id.toLowerCase());
            fileConfiguration.set(CLAN_TAG, null);
            saveFile(fileConfiguration, id.toLowerCase());

            if (cache.containsKey(id.toLowerCase())) {
                cache.put(id.toLowerCase(), constructPlayer(fileConfiguration, id));
            }
        }
    }

    private static ClanPlayer constructPlayer(ConfigurationSection playerSection, String id) {
        int killCount = playerSection.getInt("kill-count");
        int deathCount = playerSection.getInt("death-count");
        String clanTag = playerSection.getString(CLAN_TAG);
        return new ClanPlayer(id, killCount, deathCount, clanTag);
    }
}

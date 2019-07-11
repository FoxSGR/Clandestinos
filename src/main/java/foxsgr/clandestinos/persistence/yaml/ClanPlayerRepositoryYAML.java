package foxsgr.clandestinos.persistence.yaml;

import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanPlayerRepository;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

class ClanPlayerRepositoryYAML extends YAMLRepository implements ClanPlayerRepository {

    ClanPlayerRepositoryYAML(@NotNull File configurationFile, @NotNull FileConfiguration fileConfiguration) {
        super(configurationFile, fileConfiguration, "players");
    }

    @Override
    public ClanPlayer find(String id) {
/*        ConfigurationSection playerSection = subSection(id);
        if (playerSection == null) {
            return null;
        }

        int killCount = playerSection.getInt("kill-count");
        String clanTag = playerSection.getString("clan-tag");
        if (clanTag == null) {
            return new ClanPlayer(id, killCount, null);
        } else {
            return new ClanPlayer(id, killCount, new ClanTag(clanTag));
        }*/

        throw new UnsupportedOperationException();
    }

    @Override
    public ClanPlayer save(ClanPlayer clanPlayer) {
/*        ConfigurationSection section = section();
        ConfigurationSection playerSection = section.getConfigurationSection(clanPlayer.id());
        if (playerSection == null) {
            playerSection = section.createSection(clanPlayer.id());
        }

        playerSection.set("kill-count", clanPlayer.killCount());

        ClanTag clanTag = clanPlayer.clanTag();
        if (clanTag != null) {
            playerSection.set("clan-tag", clanTag.value());
        }

        update();*/

        throw new UnsupportedOperationException();
    }
}

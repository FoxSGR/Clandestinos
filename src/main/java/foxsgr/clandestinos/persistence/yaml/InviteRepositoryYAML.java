package foxsgr.clandestinos.persistence.yaml;

import foxsgr.clandestinos.domain.model.Invite;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.persistence.InviteRepository;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class InviteRepositoryYAML extends YAMLRepository implements InviteRepository {

    InviteRepositoryYAML(JavaPlugin plugin) {
        super(plugin, "invites.yml");
    }

    @Override
    public void add(Invite invite) {
        FileConfiguration fileConfiguration = load();
        ConfigurationSection section = fileConfiguration.createSection(invite.id());
        section.set("invited-to", invite.invitedTo().withoutColor().value());
        section.set("invited-player", invite.invitedPlayer());
        update(fileConfiguration);
    }

    @Override
    public Invite find(String invitedPlayer, String clanInvitedTo) {
        FileConfiguration fileConfiguration = load();
        String id = clanInvitedTo + invitedPlayer;
        ConfigurationSection section = fileConfiguration.getConfigurationSection(id);
        if (section == null) {
            return null;
        }

        ClanTag clanTag = new ClanTag(clanInvitedTo);
        return new Invite(id, clanTag, invitedPlayer);
    }

    @Override
    public void remove(Invite invite) {
        FileConfiguration fileConfiguration = load();
        fileConfiguration.set(invite.id(), null);
        update(fileConfiguration);
    }

    @Override
    public void removeAllFrom(Clan clan) {
        FileConfiguration fileConfiguration = load();

        for (String key : fileConfiguration.getKeys(false)) {
            if (key.contains(clan.tag().withoutColor().value())) {
                fileConfiguration.set(key, null);
            }
        }

        update(fileConfiguration);
    }
}

package foxsgr.clandestinos.persistence.yaml;

import foxsgr.clandestinos.domain.model.Invite;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.persistence.InviteRepository;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public class InviteRepositoryYAML extends YAMLRepository implements InviteRepository {

    InviteRepositoryYAML(JavaPlugin plugin) {
        super(plugin, "invites");
    }

    @Override
    public void add(Invite invite) {
        FileConfiguration fileConfiguration = new YamlConfiguration();

        fileConfiguration.set("invited-to", invite.invitedTo().withoutColor().value());
        fileConfiguration.set("invited-player", invite.invitedPlayer());

        update(fileConfiguration, invite.id());
    }

    @Override
    public Invite find(String invitedPlayer, String clanInvitedTo) {
        String id = clanInvitedTo + invitedPlayer;
        File file = makeFile(id);
        if (!file.exists()) {
            return null;
        }

        ClanTag clanTag = new ClanTag(clanInvitedTo);
        return new Invite(id, clanTag, invitedPlayer);
    }

    @Override
    public void remove(Invite invite) {
        if (!makeFile(invite.id()).delete()) {
            logger().log(Level.WARNING, "Could not delete the invite file {0}.yml", invite.id());
        }
    }

    @Override
    public void removeAllFrom(Clan clan) {
        File[] inviteFiles = repositoryFolder.listFiles();
        if (inviteFiles == null) {
            return;
        }

        for (File inviteFile : inviteFiles) {
            if (!inviteFile.delete()) {
                logger().log(Level.WARNING, "Could not delete the invite file {0}.yml", inviteFile.getName());
            }
        }
    }
}

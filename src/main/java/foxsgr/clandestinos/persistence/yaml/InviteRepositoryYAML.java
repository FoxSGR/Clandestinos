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

    private static final String INVITED_TO_FIELD = "invited-to";
    private static final String INVITED_PLAYER_FIELD = "invited-player";

    InviteRepositoryYAML(JavaPlugin plugin) {
        super(plugin, "invites");
    }

    @Override
    public void add(Invite invite) {
        FileConfiguration fileConfiguration = new YamlConfiguration();
        fileConfiguration.set(INVITED_TO_FIELD, invite.invitedTo().withoutColor().value().toLowerCase());
        fileConfiguration.set(INVITED_PLAYER_FIELD, invite.invitedPlayer());
        saveFile(fileConfiguration, invite.id().toLowerCase());
    }

    @Override
    public Invite find(String invitedPlayer, String clanInvitedTo) {
        String id = clanInvitedTo + invitedPlayer;
        FileConfiguration fileConfiguration = file(id.toLowerCase());
        if (fileConfiguration == null) {
            return null;
        }

        ClanTag clanTag = new ClanTag(fileConfiguration.getString(INVITED_TO_FIELD));
        return new Invite(id, clanTag, fileConfiguration.getString(INVITED_PLAYER_FIELD));
    }

    @Override
    public void remove(Invite invite) {
        if (!makeFile(invite.id().toLowerCase()).delete()) {
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
            if (inviteFile.getName().contains(clan.simpleTag()) && !inviteFile.delete()) {
                logger().log(Level.WARNING, "Could not delete the invite file {0}.yml", inviteFile.getName());
            }
        }
    }
}

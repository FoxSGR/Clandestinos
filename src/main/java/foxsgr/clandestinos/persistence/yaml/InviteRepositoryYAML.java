package foxsgr.clandestinos.persistence.yaml;

import foxsgr.clandestinos.domain.model.Invite;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.persistence.InviteRepository;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

import static foxsgr.clandestinos.util.TaskUtil.runAsync;

public class InviteRepositoryYAML extends YAMLRepository implements InviteRepository {

    private static final String INVITED_TO_FIELD = "invited-to";
    private static final String INVITED_PLAYER_FIELD = "invited-player";

    private static final Lock MUTEX = new ReentrantLock();

    InviteRepositoryYAML(JavaPlugin plugin) {
        super(plugin, "invites");
    }

    @Override
    public void add(Invite invite) {
        FileConfiguration fileConfiguration = new YamlConfiguration();
        fileConfiguration.set(INVITED_TO_FIELD, invite.invitedTo().withoutColor().value().toLowerCase());
        fileConfiguration.set(INVITED_PLAYER_FIELD, invite.invitedPlayer());

        MUTEX.lock();
        saveFile(fileConfiguration, invite.id().toLowerCase());
        MUTEX.unlock();
    }

    @Override
    public Invite find(String invitedPlayer, String clanInvitedTo) {
        String id = clanInvitedTo + Invite.ID_SEPARATOR + invitedPlayer;

        // Mutex lock in case invites are being removed
        MUTEX.lock();
        FileConfiguration fileConfiguration = file(id.toLowerCase());
        MUTEX.unlock();
        if (fileConfiguration == null) {
            return null;
        }

        ClanTag clanTag = new ClanTag(fileConfiguration.getString(INVITED_TO_FIELD));
        return new Invite(id, clanTag, fileConfiguration.getString(INVITED_PLAYER_FIELD));
    }

    @Override
    public void remove(Invite invite) {
        MUTEX.lock();
        if (!makeFile(invite.id().toLowerCase()).delete()) {
            logger().log(Level.WARNING, "Could not delete the invite file {0}.yml", invite.id());
        }
        MUTEX.unlock();
    }

    @Override
    public void removeAllFrom(Clan clan) {
        runAsync(MUTEX, plugin, () -> removeFilesStartingWith(clan.simpleTag()));
    }
}

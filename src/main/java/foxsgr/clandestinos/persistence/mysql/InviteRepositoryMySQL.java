package foxsgr.clandestinos.persistence.mysql;

import foxsgr.clandestinos.domain.model.Invite;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.persistence.InviteRepository;
import org.bukkit.plugin.java.JavaPlugin;

import static foxsgr.clandestinos.persistence.mysql.DBConnectionManager.execute;
import static foxsgr.clandestinos.util.TaskUtil.runAsync;
import static java.util.Arrays.asList;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class InviteRepositoryMySQL extends MySQLRepository implements InviteRepository {

    public InviteRepositoryMySQL(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void add(Invite invite) {
        execute("INSERT INTO invite VALUES (:1, :2, :3)", asList(
                invite.id(),
                invite.invitedTo().withoutColor().toString().toLowerCase(),
                invite.invitedPlayer()
        ));
    }

    @Override
    public Invite find(String invited, String clanTag) {
        String id = clanTag + Invite.ID_SEPARATOR + invited;

        return execute("SELECT * FROM invite WHERE id = :1", asList(id), results -> {
            if (!results.next()) {
                return null;
            }

            return new Invite(
                    id,
                    new ClanTag(results.getString("invited_to_tag")),
                    results.getString("invited_player")
            );
        });
    }

    @Override
    public void remove(Invite invite) {
        execute("DELETE FROM invite WHERE id = :1", asList(invite.id()));
    }

    @Override
    public void removeAllFrom(Clan clan) {
        runAsync(plugin, () -> execute("DELETE FROM invite WHERE invited_to_tag = :1", asList(clan.simpleTag())));
    }
}

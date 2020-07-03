package foxsgr.clandestinos.persistence.mysql;

import foxsgr.clandestinos.domain.model.Invite;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.persistence.InviteRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static foxsgr.clandestinos.persistence.mysql.SQLConnectionManager.execute;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class InviteRepositoryMySQL implements InviteRepository {

    @Override
    public void add(Invite invite) {
        execute("INSERT INTO invite VALUES (:1, :2, :3)", Arrays.asList(
                invite.id(),
                invite.invitedTo().withoutColor().toString().toLowerCase(),
                invite.invitedPlayer()
        ));
    }

    @Override
    public Invite find(String invited, String clanTag) {
        String id = clanTag + Invite.ID_SEPARATOR + invited;

        return execute("SELECT * FROM invite WHERE id = :1", Arrays.asList(id), statement -> {
            try (ResultSet results = statement.getResultSet()) {
                if (!results.next()) {
                    return null;
                }

                return new Invite(
                        id,
                        new ClanTag(results.getString("invited_to_tag")),
                        results.getString("invited_player")
                );
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    @Override
    public void remove(Invite invite) {
        execute("DELETE FROM invite WHERE id = :1", Arrays.asList(invite.id()));
    }

    @Override
    public void removeAllFrom(Clan clan) {
        execute("DELETE FROM invite WHERE invited_to_tag = :1", Arrays.asList(clan.simpleTag()));
    }
}

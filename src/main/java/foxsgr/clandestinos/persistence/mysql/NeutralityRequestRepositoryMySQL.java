package foxsgr.clandestinos.persistence.mysql;

import foxsgr.clandestinos.domain.model.NeutralityRequest;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.persistence.NeutralityRequestRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static foxsgr.clandestinos.persistence.mysql.SQLConnectionManager.execute;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class NeutralityRequestRepositoryMySQL implements NeutralityRequestRepository {

    @Override
    public void save(NeutralityRequest request) {
        execute("SELECT id FROM neutrality_request WHERE id = :1", Arrays.asList(request.id()),
                statement -> {
                    try (ResultSet resultSet = statement.getResultSet()) {
                        if (resultSet.next()) {
                            execute("UPDATE neutrality_request SET requester_tag = :1, requestee_tag = :2 WHERE id = :3",
                                    Arrays.asList(request.requester().withoutColor().value().toLowerCase(),
                                            request.requestee().withoutColor().value().toLowerCase(),
                                            request.id()));
                        } else {
                            execute("INSERT INTO neutrality_request VALUES (:1, :2, :3)", Arrays.asList(
                                    request.id(),
                                    request.requester().withoutColor().value().toLowerCase(),
                                    request.requestee().withoutColor().value().toLowerCase())
                            );
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    return null;
                }
        );
    }

    @Override
    public NeutralityRequest find(String requesterTag, String requesteeTag) {
        String id = requesterTag + NeutralityRequest.ID_SEPARATOR + requesteeTag;

        return execute("SELECT * FROM neutrality_request WHERE id = :1", Arrays.asList(id), statement -> {
            try (ResultSet results = statement.getResultSet()) {
                if (!results.next()) {
                    return null;
                }

                return new NeutralityRequest(
                        new ClanTag(results.getString("requester_tag")),
                        new ClanTag(results.getString("requestee_tag"))
                );
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    @Override
    public void remove(NeutralityRequest request) {
        execute("DELETE FROM neutrality_request WHERE id = :1", Arrays.asList(request.id()));
    }

    @Override
    public void removeAllFrom(Clan clan) {
        execute("DELETE FROM neutrality_request WHERE requestee_tag = :1", Arrays.asList(clan.simpleTag()));
    }
}

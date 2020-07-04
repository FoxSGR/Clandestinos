package foxsgr.clandestinos.persistence.mysql;

import foxsgr.clandestinos.domain.model.NeutralityRequest;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.persistence.NeutralityRequestRepository;
import org.bukkit.plugin.java.JavaPlugin;

import static foxsgr.clandestinos.persistence.mysql.DBConnectionManager.execute;
import static foxsgr.clandestinos.util.TaskUtil.runAsync;
import static java.util.Arrays.asList;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class NeutralityRequestRepositoryMySQL extends MySQLRepository implements NeutralityRequestRepository {

    public NeutralityRequestRepositoryMySQL(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void save(NeutralityRequest request) {
        execute("SELECT id FROM neutrality_request WHERE id = :1", asList(request.id()), results -> {
                    if (results.next()) {
                        execute("UPDATE neutrality_request SET requester_tag = :1, requestee_tag = :2 WHERE id = :3",
                                asList(request.requester().withoutColor().value().toLowerCase(),
                                        request.requestee().withoutColor().value().toLowerCase(),
                                        request.id()));
                    } else {
                        execute("INSERT INTO neutrality_request VALUES (:1, :2, :3)", asList(
                                request.id(),
                                request.requester().withoutColor().value().toLowerCase(),
                                request.requestee().withoutColor().value().toLowerCase())
                        );
                    }

                    return null;
                }
        );
    }

    @Override
    public NeutralityRequest find(String requesterTag, String requesteeTag) {
        String id = requesterTag + NeutralityRequest.ID_SEPARATOR + requesteeTag;

        return execute("SELECT * FROM neutrality_request WHERE id = :1", asList(id), results -> {
            if (!results.next()) {
                return null;
            }

            return new NeutralityRequest(
                    new ClanTag(results.getString("requester_tag")),
                    new ClanTag(results.getString("requestee_tag"))
            );
        });
    }

    @Override
    public void remove(NeutralityRequest request) {
        execute("DELETE FROM neutrality_request WHERE id = :1", asList(request.id()));
    }

    @Override
    public void removeAllFrom(Clan clan) {
        runAsync(plugin, () ->
                execute("DELETE FROM neutrality_request WHERE requestee_tag = :1", asList(clan.simpleTag())));
    }
}

package foxsgr.clandestinos.persistence.mysql;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.persistence.ClanRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static foxsgr.clandestinos.persistence.mysql.SQLConnectionManager.execute;

public class ClanRepositoryMySQL implements ClanRepository {

    @Override
    public Clan find(String tag) {
        return execute("SELECT * FROM clan c WHERE LOWER(c.tag) = LOWER(:1)", Arrays.asList(tag),
                statement -> {
                    try (ResultSet results = statement.getResultSet()) {
                        String foundTag = null;
                        String name = null;
                        String owner = null;
                        boolean friendlyFire = false;

                        while (results.next()) {
                            foundTag = results.getString("tag");
                            name = results.getString("clan_name");
                            owner = results.getString("clan_owner");
                            friendlyFire = results.getBoolean("clan_friendly_fire");
                        }

                        if (foundTag == null) {
                            return null;
                        }

                        return new Clan(
                                foundTag,
                                name,
                                owner,
                                friendlyFire
                        );
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        return null;
                    }
                });
    }

    @Override
    public List<Clan> findAll() {
        return null;
    }

    @Override
    public boolean add(Clan clan) {
        return false;
    }

    @Override
    public void update(Clan clan) {

    }

    @Override
    public void remove(Clan clan) {

    }
}

package foxsgr.clandestinos.persistence.mysql;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.PlayerRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import static foxsgr.clandestinos.persistence.mysql.SQLConnectionManager.createStatement;
import static foxsgr.clandestinos.persistence.mysql.SQLConnectionManager.execute;

public class PlayerRepositoryMySQL implements PlayerRepository {

    @Override
    public ClanPlayer find(String id) {
        return null;
    }

    @Override
    public void save(ClanPlayer clanPlayer) {
        execute("SELECT id FROM player cp WHERE cp.id = :1", Arrays.asList(clanPlayer.id()),
                statement -> {
                    try (ResultSet resultSet = statement.getResultSet()) {
                        System.out.println("has result, should update");
                    } catch (SQLException throwables) {
                        System.out.println("no result, should create");
                        throwables.printStackTrace();
                    }

                    return null;
                });

        try (Statement statement = createStatement()) {
            statement.executeUpdate(String.format(
                    "INSERT INTO clan_player (id, friendly_fire_enabled, kill_count, death_count, clan_tag) VALUES ('%s', '%s', '%d', '%d', '%s')"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void load(String id) {

    }

    @Override
    public void unload(String id) {

    }

    @Override
    public void leaveFromClan(Clan clan) {

    }
}

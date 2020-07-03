package foxsgr.clandestinos.persistence.mysql;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.PlayerRepository;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static foxsgr.clandestinos.persistence.mysql.SQLConnectionManager.execute;
import static foxsgr.clandestinos.util.TaskUtil.runAsync;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class PlayerRepositoryMySQL extends MySQLRepository implements PlayerRepository {

    public PlayerRepositoryMySQL(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public ClanPlayer find(String id) {
        return execute("SELECT * FROM player WHERE id = :1", Arrays.asList(id), statement -> {
            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {
                    return new ClanPlayer(
                            resultSet.getString("id"),
                            resultSet.getInt("kill_count"),
                            resultSet.getInt("death_count"),
                            resultSet.getString("tag"),
                            resultSet.getBoolean("player_friendly_fire")
                    );
                } else {
                    return null;
                }
            } catch (SQLException throwables) {
                return null;
            }
        });
    }

    @Override
    public void save(ClanPlayer clanPlayer) {
        execute("SELECT id FROM player cp WHERE cp.id = :1", Arrays.asList(clanPlayer.id()),
                statement -> {
                    try (ResultSet resultSet = statement.getResultSet()) {
                        if (resultSet.next()) {
                            execute("CALL update_player(:1, :2, :3, :4, :5)", createPlayerParams(clanPlayer));
                        } else {
                            execute("INSERT INTO player VALUES (:1, :2, :3, :4, :5)",
                                    createPlayerParams(clanPlayer));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    return null;
                }
        );
    }

    @Override
    public void load(String id) {

    }

    @Override
    public void unload(String id) {

    }

    @Override
    public void leaveFromClan(Clan clan) {
        runAsync(plugin, () -> execute("CALL leave_from_clan(:1)", Arrays.asList(clan.simpleTag())));
    }

    private static List<Object> createPlayerParams(ClanPlayer player) {
        return Arrays.asList(
                player.id(),
                player.isFriendlyFireEnabled(),
                player.killCount().value(),
                player.deathCount().value(),
                player.clan().map(tag -> tag.withoutColor().toString().toLowerCase()).orElse(null)
        );
    }
}

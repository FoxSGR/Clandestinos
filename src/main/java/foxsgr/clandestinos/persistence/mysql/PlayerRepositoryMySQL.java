package foxsgr.clandestinos.persistence.mysql;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.PlayerRepository;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static foxsgr.clandestinos.persistence.mysql.DBConnectionManager.execute;
import static foxsgr.clandestinos.util.TaskUtil.runAsync;
import static java.util.Arrays.asList;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class PlayerRepositoryMySQL extends MySQLRepository implements PlayerRepository {

    public PlayerRepositoryMySQL(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public ClanPlayer find(String id) {
        return execute("SELECT * FROM player WHERE id = :1", asList(id), results -> {
            if (results.next()) {
                return new ClanPlayer(
                        results.getString("id"),
                        results.getInt("kill_count"),
                        results.getInt("death_count"),
                        results.getString("tag"),
                        results.getBoolean("player_friendly_fire")
                );
            } else {
                return null;
            }
        });
    }

    @Override
    public void save(ClanPlayer clanPlayer) {
        execute("SELECT id FROM player cp WHERE cp.id = :1", asList(clanPlayer.id()), results -> {
                    if (results.next()) {
                        execute("CALL update_player(:1, :2, :3, :4, :5)", createPlayerParams(clanPlayer));
                    } else {
                        execute("INSERT INTO player VALUES (:1, :2, :3, :4, :5)", createPlayerParams(clanPlayer));
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
        runAsync(plugin, () -> execute("CALL leave_from_clan(:1)", asList(clan.simpleTag())));
    }

    private static List<Object> createPlayerParams(ClanPlayer player) {
        return asList(
                player.id(),
                player.isFriendlyFireEnabled(),
                player.killCount().value(),
                player.deathCount().value(),
                player.clan().map(tag -> tag.withoutColor().toString().toLowerCase()).orElse(null)
        );
    }
}

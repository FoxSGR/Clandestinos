package foxsgr.clandestinos.persistence.mysql;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.persistence.ClanRepository;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static foxsgr.clandestinos.persistence.mysql.SQLConnectionManager.execute;
import static foxsgr.clandestinos.util.TaskUtil.runAsync;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class ClanRepositoryMySQL extends MySQLRepository implements ClanRepository {

    public ClanRepositoryMySQL(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public Clan find(String tag) {
        return execute("SELECT * FROM clan c WHERE LOWER(c.tag) = LOWER(:1)", Arrays.asList(tag),
                statement -> {
                    try (ResultSet results = statement.getResultSet()) {
                        String foundTag = null;
                        String name = null;
                        String owner = null;
                        boolean friendlyFire = false;
                        Set<String> leaders = new HashSet<>();
                        Set<String> members = new HashSet<>();
                        Set<String> enemies = new HashSet<>();

                        while (results.next()) {
                            foundTag = results.getString("styled_tag");
                            name = results.getString("clan_name");
                            owner = results.getString("clan_owner");
                            friendlyFire = results.getBoolean("clan_friendly_fire");
                            leaders.add(results.getString("leader_id"));
                            members.add(results.getString("member_id"));
                            enemies.add(results.getString("enemy_tag"));
                        }

                        if (foundTag == null) {
                            return null;
                        }

                        return new Clan(
                                foundTag,
                                name,
                                owner,
                                leaders,
                                members,
                                enemies,
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
        return execute("CALL save_clan(:1, :2, :3, :4, :5, :6, :7, :8, FALSE)", clanParams(clan)) != null;
    }

    @Override
    public void update(Clan clan) {
        runAsync(plugin, () -> execute("CALL save_clan(:1, :2, :3, :4, :5, :6, :7, :8, TRUE)", clanParams(clan)));
    }

    @Override
    public void remove(Clan clan) {
        runAsync(plugin, () -> execute("CALL remove_clan(:1)", Arrays.asList(clan.simpleTag())));
    }

    private static List<Object> clanParams(Clan clan) {
        return Arrays.asList(
                clan.simpleTag(),
                clan.tag().toString(),
                clan.name().toString(),
                clan.isFriendlyFireEnabled(),
                clan.owner(),
                clan.leaders(),
                clan.members(),
                clan.enemyClans()
        );
    }
}

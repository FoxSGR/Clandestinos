package foxsgr.clandestinos.persistence.mysql;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.persistence.ClanRepository;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static foxsgr.clandestinos.persistence.mysql.SQLConnectionManager.execute;

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
                            foundTag = results.getString("tag");
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
        return false;
    }

    @Override
    public void update(Clan clan) {

    }

    @Override
    public void remove(Clan clan) {

    }
}

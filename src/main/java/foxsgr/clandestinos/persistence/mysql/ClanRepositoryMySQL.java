package foxsgr.clandestinos.persistence.mysql;

import foxsgr.clandestinos.domain.builder.ClanBuilder;
import foxsgr.clandestinos.domain.model.KDR;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.persistence.ClanRepository;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static foxsgr.clandestinos.persistence.mysql.DBConnectionManager.doIfPossible;
import static foxsgr.clandestinos.persistence.mysql.DBConnectionManager.execute;
import static foxsgr.clandestinos.util.TaskUtil.runAsync;
import static foxsgr.clandestinos.util.UtilUtil.applyIfNotNull;
import static java.util.Arrays.asList;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class ClanRepositoryMySQL extends MySQLRepository implements ClanRepository {

    private static final Map<String, Clan> cache = new ConcurrentHashMap<>();

    public ClanRepositoryMySQL(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void load() {
        execute("SELECT COUNT(*) AS count FROM clan", results -> {
            if (results.next() && results.getInt("count") == cache.size()) {
                return null;
            }

            List<Clan> clans = findAll();

            cache.clear();
            for (Clan clan : clans) {
                clan.updateKDR(null);
                cache.put(clan.simpleTag(), clan);
            }

            return null;
        });
    }

    @Override
    public Clan find(String tag) {
        Clan clan = cache.get(tag.toLowerCase());
        if (clan != null) {
            return clan;
        }

        return execute("CALL find_clan(:1)", asList(tag), results -> {
            ClanBuilder builder = new ClanBuilder();

            while (results.next()) {
                parseResults(builder, results);
            }

            if (builder.getTag() == null) {
                return null;
            }

            Clan newClan = builder.build();
            cache.put(newClan.simpleTag(), newClan);
            return newClan;
        });
    }

    @Override
    public List<Clan> findAll() {
        return findAll(100, 0);
    }

    @Override
    public List<Clan> findAll(int limit, int offset) {
        return execute("CALL find_all_clans(:1, :2)", asList(limit, offset), results -> {
            Map<String, ClanBuilder> builders = new HashMap<>();

            while (results.next()) {
                String tag = results.getString("tag");

                ClanBuilder builder = builders.get(tag);
                if (builder == null) {
                    builder = new ClanBuilder();
                    builders.put(tag, builder);
                }

                parseResults(builder, results);
            }

            List<Clan> clans = new ArrayList<>();

            for (String tag : builders.keySet()) {
                ClanBuilder builder = builders.get(tag);
                clans.add(builder.build());
            }

            return clans;
        });
    }

    @Override
    public boolean add(Clan clan) {
        return execute(
                "CALL save_clan(:1, :2, :3, :4, :5, :6, :7, :8, FALSE)",
                clanParams(clan),
                (statement) -> {
                    cache.put(clan.simpleTag(), clan);
                    return null;
                }
        ) != null;
    }

    @Override
    public void update(Clan clan) {
        runAsync(plugin, () -> execute("CALL save_clan(:1, :2, :3, :4, :5, :6, :7, :8, TRUE)", clanParams(clan)));
    }

    @Override
    public void remove(Clan clan) {
        runAsync(plugin, () -> {
            execute("CALL remove_clan(:1)", asList(clan.simpleTag()));
            cache.remove(clan.simpleTag());
        });
    }

    private static List<Object> clanParams(Clan clan) {
        return asList(
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

    private static void parseResults(ClanBuilder builder, ResultSet results) throws SQLException {
        builder
                .withTag(results.getString("styled_tag"))
                .withName(results.getString("clan_name"))
                .withOwner(results.getString("clan_owner"))
                .withFriendlyFireEnabled(results.getBoolean("clan_friendly_fire"));

        doIfPossible(() -> builder.withKDR(new KDR(results.getInt("kill_count"), results.getInt("death_count"))));

        applyIfNotNull(results.getString("leader_id"), builder::addLeader);
        applyIfNotNull(results.getString("member_id"), builder::addMember);
        applyIfNotNull(results.getString("enemy_tag"), builder::addEnemy);
    }
}

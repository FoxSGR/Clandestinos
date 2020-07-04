package foxsgr.clandestinos.persistence.mysql;

import foxsgr.clandestinos.domain.builder.ClanBuilder;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.persistence.ClanRepository;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static foxsgr.clandestinos.persistence.mysql.DBConnectionManager.execute;
import static foxsgr.clandestinos.util.TaskUtil.runAsync;
import static foxsgr.clandestinos.util.UtilUtil.applyIfNotNull;
import static java.util.Arrays.asList;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class ClanRepositoryMySQL extends MySQLRepository implements ClanRepository {

    public ClanRepositoryMySQL(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public Clan find(String tag) {
        return execute("CALL find_clan(:1)", asList(tag), results -> {
            ClanBuilder builder = new ClanBuilder();

            while (results.next()) {
                parseResults(builder, results);
            }

            if (builder.getTag() == null) {
                return null;
            }

            return builder.build();
        });
    }

    @Override
    public List<Clan> findAll() {
        return execute("CALL find_all_clans()", results -> {
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
                (statement) -> true
        ) != null;
    }

    @Override
    public void update(Clan clan) {
        runAsync(plugin, () -> execute("CALL save_clan(:1, :2, :3, :4, :5, :6, :7, :8, TRUE)", clanParams(clan)));
    }

    @Override
    public void remove(Clan clan) {
        runAsync(plugin, () -> execute("CALL remove_clan(:1)", asList(clan.simpleTag())));
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

        applyIfNotNull(results.getString("leader_id"), builder::addLeader);
        applyIfNotNull(results.getString("member_id"), builder::addMember);
        applyIfNotNull(results.getString("enemy_tag"), builder::addEnemy);
    }
}

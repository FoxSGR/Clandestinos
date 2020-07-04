package foxsgr.clandestinos.persistence.mysql;

import foxsgr.clandestinos.persistence.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import static foxsgr.clandestinos.persistence.mysql.DBConnectionManager.getConnection;

public class MySQLRepositoryFactory implements RepositoryFactory {

    private final JavaPlugin plugin;

    public MySQLRepositoryFactory(JavaPlugin plugin) {
        this.plugin = plugin;

        DBConnectionManager.init();
        runDDL();
    }

    @Override
    public @NotNull PlayerRepository players() {
        return new PlayerRepositoryMySQL(plugin);
    }

    @Override
    public @NotNull ClanRepository clans() {
        return new ClanRepositoryMySQL(plugin);
    }

    @Override
    public @NotNull InviteRepository invites() {
        return new InviteRepositoryMySQL(plugin);
    }

    @Override
    public @NotNull NeutralityRequestRepository neutralityRequests() {
        return new NeutralityRequestRepositoryMySQL(plugin);
    }

    private static void runDDL() {
        ScriptRunner runner = new ScriptRunner(getConnection(), false, false);
        try {
            runner.runScript(new InputStreamReader(MySQLRepositoryFactory.class.getResourceAsStream("/mysql/ddl.sql")));
            runner.runScript(new InputStreamReader(MySQLRepositoryFactory.class.getResourceAsStream("/mysql/clan.sql")));
            runner.runScript(new InputStreamReader(MySQLRepositoryFactory.class.getResourceAsStream("/mysql/player.sql")));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}

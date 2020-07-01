package foxsgr.clandestinos.persistence.mysql;

import foxsgr.clandestinos.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import static foxsgr.clandestinos.persistence.mysql.SQLConnectionManager.getConnection;

public class MySQLRepositoryFactory implements RepositoryFactory {

    public MySQLRepositoryFactory() {
        SQLConnectionManager.init();
        runDDL();
    }

    @Override
    public @NotNull PlayerRepository players() {
        return new PlayerRepositoryMySQL();
    }

    @Override
    public @NotNull ClanRepository clans() {
        return new ClanRepositoryMySQL();
    }

    @Override
    public @NotNull InviteRepository invites() {
        return null;
    }

    @Override
    public @NotNull NeutralityRequestRepository neutralityRequests() {
        return null;
    }

    private static void runDDL() {
        ScriptRunner runner = new ScriptRunner(getConnection(), false, false);
        try {
            runner.runScript(new InputStreamReader(MySQLRepositoryFactory.class.getResourceAsStream("/mysql/ddl.sql")));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}

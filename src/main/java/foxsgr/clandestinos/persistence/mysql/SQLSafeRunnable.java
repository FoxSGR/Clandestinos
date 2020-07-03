package foxsgr.clandestinos.persistence.mysql;

import java.sql.SQLException;

public interface SQLSafeRunnable {

    void run() throws SQLException;
}

package foxsgr.clandestinos.persistence.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SQLSafeFunction<T> {

    T apply(ResultSet results) throws SQLException;
}

package skymine.redenergy.core.database;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface DatabaseConnectionHandler {

    void handle(ResultSet set) throws SQLException;
}
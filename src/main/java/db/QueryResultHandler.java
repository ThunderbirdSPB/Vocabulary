package db;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface QueryResultHandler<T> {
    T handle(ResultSet resultSet) throws SQLException;
}

package db;

import java.sql.*;


public class UserDAO {
    private QueryExecutor executor;

    public UserDAO(QueryExecutor executor) {
        this.executor = executor;
    }

    public boolean exists(String login) {
        return executor.execQuery(String.format("SELECT id FROM users WHERE login='%s'", login), (ResultSet::next));
    }

    /**
     * If return false - this login isn't unique, true otherwise
     */
    public boolean addNewUser(String login, String pass) {
        if (!exists(login)) {
            executor.execUpdate(String.format("INSERT INTO users(login, password) VALUES('%s','%s')", login, pass));

            return createTableWords(login, executor);
        }
        return false;
    }

    private boolean createTableWords(String login, QueryExecutor executor) {
        try {
            executor.execUpdate(String.format("CREATE TABLE public.%s(\n" +
                    "word text NOT NULL,\n" +
                    "translation text NOT NULL,\n" +
                    "\"completedTrainings\" text[] NOT NULL\n" +
                    ")\n" +
                    "WITH (\n" +
                    "    OIDS = FALSE\n" +
                    ");", login));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

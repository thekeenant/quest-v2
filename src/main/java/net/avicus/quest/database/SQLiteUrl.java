package net.avicus.quest.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteUrl implements DatabaseUrl {
    private final String dbPath;

    private SQLiteUrl(String dbPath) {
        this.dbPath = dbPath;
    }

    public Connection establishConnection() throws DatabaseException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("SQLite library not found", e);
        }

        try {
            return DriverManager.getConnection(toString());
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public String toString() {
        return "jdbc:sqlite:" + dbPath;
    }

    public static SQLiteUrl of(String dbPath) {
        return new SQLiteUrl(dbPath);
    }
}

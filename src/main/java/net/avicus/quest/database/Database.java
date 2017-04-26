package net.avicus.quest.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class Database extends DatabaseConnection {
    private final DatabaseUrl url;
    private Connection connection;

    public Database(DatabaseUrl url) {
        this.url = url;
    }

    public void open() throws DatabaseException {
        this.connection = this.url.establishConnection();
    }

    public void close() throws DatabaseException {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public Optional<Connection> getConnection() {
        return Optional.ofNullable(this.connection);
    }
}

package net.avicus.quest.database.url;

import net.avicus.quest.database.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class MySQLUrl implements DatabaseUrl {
    private final String host;
    private final String database;
    private final String username;
    private final String password;
    private final boolean reconnectEnabled;
    private final boolean unicode;

    public MySQLUrl(String host, String database, Optional<String> username, Optional<String> password, boolean reconnectEnabled, boolean unicode) {
        this.host = host;
        this.database = database;
        this.username = username.orElse(null);
        this.password = password.orElse(null);
        this.reconnectEnabled = reconnectEnabled;
        this.unicode = unicode;
    }

    public Connection establishConnection() throws DatabaseException {
        try {
            return DriverManager.getConnection(toString(), this.username, this.password);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public String toString() {
        StringBuilder url = new StringBuilder();
        url.append(String.format("jdbc:mysql://%s/%s", this.host, this.database));
        url.append("?");

        if (this.reconnectEnabled) {
            url.append("autoReconnect=true&");
        }

        if (this.unicode) {
            url.append("useUnicode=yes&characterEncoding=UTF-8&");
        }

        return url.toString();
    }
}

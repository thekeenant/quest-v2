package net.avicus.quest.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLUrl implements DatabaseUrl {
    private final String host;
    private final String database;
    private final String username;
    private final String password;
    private final boolean reconnectEnabled;
    private final boolean unicode;

    private MySQLUrl(String host, String database, String username, String password, boolean reconnectEnabled, boolean unicode) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
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

    public static Builder builder(String host, String database) {
        return new Builder(host, database);
    }

    private static class Builder {
        private final String host;
        private final String database;
        private String username;
        private String password;
        private boolean reconnectEnabled;
        private boolean unicode;

        private Builder(String host, String database) {
            this.host = host;
            this.database = database;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder reconnect(boolean reconnect) {
            this.reconnectEnabled = reconnect;
            return this;
        }

        public Builder unicode(boolean unicode) {
            this.unicode = unicode;
            return this;
        }

        public MySQLUrl build() {
            return new MySQLUrl(host, database, username, password, reconnectEnabled, unicode);
        }
    }
}

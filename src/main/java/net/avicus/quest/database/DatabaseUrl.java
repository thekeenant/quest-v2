package net.avicus.quest.database;

import java.sql.Connection;

public interface DatabaseUrl {
    Connection establishConnection() throws DatabaseException;
}

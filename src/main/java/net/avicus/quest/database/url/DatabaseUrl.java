package net.avicus.quest.database.url;

import net.avicus.quest.database.DatabaseException;

import java.sql.Connection;

public interface DatabaseUrl {
    Connection establishConnection() throws DatabaseException;
}

package net.avicus.quest.query.insert;

import net.avicus.quest.query.QueryConfig;
import net.avicus.quest.database.Database;

import java.sql.PreparedStatement;

public class InsertConfig implements QueryConfig {
    public static InsertConfig DEFAULT = new InsertConfig();

    public PreparedStatement createStatement(Database database, String sql) {
        return database.createUpdateStatement(sql);
    }
}

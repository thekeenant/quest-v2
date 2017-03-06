package net.avicus.quest.query.delete;

import net.avicus.quest.query.QueryConfig;
import net.avicus.quest.database.Database;

import java.sql.PreparedStatement;

public class DeleteConfig implements QueryConfig {
    public static DeleteConfig DEFAULT = new DeleteConfig();

    public PreparedStatement createStatement(Database database, String sql) {
        return database.createUpdateStatement(sql);
    }
}

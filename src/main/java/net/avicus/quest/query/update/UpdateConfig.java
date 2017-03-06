package net.avicus.quest.query.update;

import net.avicus.quest.query.QueryConfig;
import net.avicus.quest.database.Database;

import java.sql.PreparedStatement;

public class UpdateConfig implements QueryConfig {
    public static UpdateConfig DEFAULT = new UpdateConfig();

    public PreparedStatement createStatement(Database database, String sql) {
        return database.createUpdateStatement(sql);
    }
}

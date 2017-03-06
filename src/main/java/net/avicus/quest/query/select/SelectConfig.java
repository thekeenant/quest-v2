package net.avicus.quest.query.select;

import net.avicus.quest.query.QueryConfig;
import net.avicus.quest.database.Database;
import net.avicus.quest.database.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SelectConfig implements QueryConfig {
    public static SelectConfig DEFAULT = new SelectConfig(false, -1, false);

    private final boolean iterate;
    private final int timeout;
    private final boolean poolable;

    /**
     * Select configuration.
     * @param iterate True to tell database to return results one-by-one, as opposed
     *                to all at once, which may use up too many resources.
     * @param timeout The select timeout.
     * @param poolable Poolable.
     */
    public SelectConfig(boolean iterate, int timeout, boolean poolable) {
        this.iterate = iterate;
        this.timeout = timeout;
        this.poolable = poolable;
    }

    public PreparedStatement createStatement(Database database, String sql) throws DatabaseException {
        try {
            PreparedStatement statement = database.createQueryStatement(sql, this.iterate);
            statement.setQueryTimeout(this.timeout);
            statement.setPoolable(this.poolable);
            return statement;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}

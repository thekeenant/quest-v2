package net.avicus.quest.database;

import net.avicus.quest.parameter.FieldParam;
import net.avicus.quest.query.delete.Delete;
import net.avicus.quest.query.delete.DeleteResult;
import net.avicus.quest.query.insert.Insert;
import net.avicus.quest.query.insert.InsertResult;
import net.avicus.quest.query.select.Select;
import net.avicus.quest.query.select.SelectResult;
import net.avicus.quest.query.update.Update;
import net.avicus.quest.query.update.UpdateResult;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class DatabaseConnection {
    private Integer defaultQueryTimeout;

    public DatabaseConnection(Integer defaultQueryTimeout) {
        this.defaultQueryTimeout = defaultQueryTimeout;
    }

    public DatabaseConnection() {
        this(null);
    }

    public void setDefaultQueryTimeout(Integer timeout) {
        this.defaultQueryTimeout = timeout;
    }

    public abstract Optional<Connection> getConnection();

    public Delete delete(FieldParam table) {
        return new Delete(this, table);
    }

    public Delete delete(String table) {
        return delete(new FieldParam(table));
    }

    public DeleteResult rawDelete(String sql, List<Object> data) {
        PreparedStatement statement = createUpdateStatement(sql);
        for (int i = 0; i < data.size(); i++) {
            try {
                statement.setObject(i + 1, data.get(i));
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }
        return DeleteResult.execute(statement);
    }

    public Insert insert(FieldParam table) {
        return new Insert(this, table);
    }

    public Insert insert(String table) {
        return insert(new FieldParam(table));
    }

    public InsertResult rawInsert(String sql, Object... data) {
        return rawInsert(sql, Arrays.asList(data));
    }

    public InsertResult rawInsert(String sql, List<Object> data) {
        PreparedStatement statement = createUpdateStatement(sql);
        for (int i = 0; i < data.size(); i++) {
            try {
                statement.setObject(i + 1, data.get(i));
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }
        return InsertResult.execute(statement);
    }

    public Update update(FieldParam table) {
        return new Update(this, table);
    }

    public Update update(String table) {
        return new Update(this, new FieldParam(table));
    }

    public UpdateResult rawUpdate(String sql, Object... data) {
        return rawUpdate(sql, Arrays.asList(data));
    }

    public UpdateResult rawUpdate(String sql, List<Object> data) {
        PreparedStatement statement = createUpdateStatement(sql);
        for (int i = 0; i < data.size(); i++) {
            try {
                statement.setObject(i + 1, data.get(i));
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }
        return UpdateResult.execute(statement);
    }

    public Select select(FieldParam table) {
        return new Select(this, table);
    }

    public Select select(String table) {
        return select(new FieldParam(table));
    }

    public SelectResult rawSelect(String sql, boolean iterate, int timeout, Object... data) {
        return rawSelect(sql, Arrays.asList(data), iterate, timeout);
    }

    public SelectResult rawSelect(String sql, List<Object> data, boolean iterate, int timeout) {
        PreparedStatement statement = createQueryStatement(sql, iterate, timeout);
        for (int i = 0; i < data.size(); i++) {
            try {
                statement.setObject(i + 1, data.get(i));
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }
        return SelectResult.execute(statement);
    }

    public PreparedStatement createUpdateStatement(String sql) {
        Connection conn = getConnection().orElseThrow(() -> new DatabaseException("Not connected"));

        try {
            return conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public PreparedStatement createQueryStatement(String sql, boolean iterate, Integer timeout) throws DatabaseException {
        Connection conn = getConnection().orElseThrow(() -> new DatabaseException("Not connected"));

        try {
            PreparedStatement statement;

            if (iterate) {
                statement = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                statement.setFetchSize(Integer.MIN_VALUE);
            }
            else {
                statement = conn.prepareStatement(sql);
            }

            // If timeout not provided, try default timeout
            timeout = timeout == null ? this.defaultQueryTimeout : timeout;
            if (timeout != null) {
                statement.setQueryTimeout(timeout);
            }

            return statement;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}

package net.avicus.quest.database;

import net.avicus.quest.Param;
import net.avicus.quest.database.url.DatabaseUrl;
import net.avicus.quest.parameter.FieldParam;
import net.avicus.quest.query.delete.Delete;
import net.avicus.quest.query.delete.DeleteConfig;
import net.avicus.quest.query.delete.DeleteResult;
import net.avicus.quest.query.insert.Insert;
import net.avicus.quest.query.insert.InsertConfig;
import net.avicus.quest.query.insert.InsertResult;
import net.avicus.quest.query.select.Select;
import net.avicus.quest.query.select.SelectConfig;
import net.avicus.quest.query.select.SelectResult;
import net.avicus.quest.query.update.Update;
import net.avicus.quest.query.update.UpdateConfig;
import net.avicus.quest.query.update.UpdateResult;

import java.sql.*;
import java.util.Optional;

public class Database {
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

    public Delete delete(FieldParam table) {
        return new Delete(this, table);
    }

    public Delete delete(String table) {
        return delete(new FieldParam(table));
    }

    public DeleteResult rawDelete(String sql, Object... data) {
        return rawDelete(sql, DeleteConfig.DEFAULT, data);
    }

    public DeleteResult rawDelete(String sql, DeleteConfig config, Object... data) {
        PreparedStatement statement = config.createStatement(this, sql);
        for (int i = 0; i < data.length; i++) {
            try {
                statement.setObject(i + 1, data[i]);
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
        return rawInsert(sql, InsertConfig.DEFAULT, data);
    }

    public InsertResult rawInsert(String sql, InsertConfig config, Object... data) {
        PreparedStatement statement = config.createStatement(this, sql);
        for (int i = 0; i < data.length; i++) {
            try {
                statement.setObject(i + 1, data[i]);
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
        return rawUpdate(sql, UpdateConfig.DEFAULT, data);
    }

    public UpdateResult rawUpdate(String sql, UpdateConfig config, Object... data) {
        PreparedStatement statement = config.createStatement(this, sql);
        for (int i = 0; i < data.length; i++) {
            try {
                statement.setObject(i + 1, data[i]);
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }
        return UpdateResult.execute(statement);
    }

    public Select select(FieldParam table, String... columns) {
        return new Select(this, table).select(columns);
    }

    public Select select(String table, String... columns) {
        return select(new FieldParam(table)).select(columns);
    }

    public Select select(FieldParam table, Param... params) {
        return new Select(this, table).select(params);
    }

    public Select select(String table, Param... params) {
        return select(new FieldParam(table)).select(params);
    }

    public Select select(FieldParam table) {
        return new Select(this, table);
    }

    public Select select(String table) {
        return select(new FieldParam(table));
    }

    public SelectResult rawSelect(String sql, SelectConfig config, Object... data) {
        PreparedStatement statement = config.createStatement(this, sql);
        for (int i = 0; i < data.length; i++) {
            try {
                statement.setObject(i + 1, data[i]);
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }
        return SelectResult.execute(statement);
    }

    public SelectResult rawSelect(String sql, Object... data) {
        return rawSelect(sql, SelectConfig.DEFAULT, data);
    }

    public PreparedStatement createUpdateStatement(String sql) {
        try {
            return this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public PreparedStatement createQueryStatement(String sql, boolean iterate) throws DatabaseException {
        try {
            PreparedStatement statement;

            if (iterate) {
                statement = this.connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                statement.setFetchSize(Integer.MIN_VALUE);
            }
            else {
                statement = this.connection.prepareStatement(sql);
            }

            return statement;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}

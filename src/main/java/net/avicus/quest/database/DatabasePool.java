package net.avicus.quest.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DatabasePool extends DatabaseConnection {
    private final DatabaseUrl url;
    private final int count;

    private final List<Database> pool;

    public DatabasePool(DatabaseUrl url, int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Count must be at least 1");
        }
        this.url = url;
        this.count = count;
        this.pool = new ArrayList<>();
    }

    public void open() {
        for (int i = 0; i < count; i++) {
            pool.add(new Database(url));
        }
        pool.forEach(Database::open);
    }

    public void close() {
        pool.forEach(Database::close);
    }

    @Override
    public Optional<Connection> getConnection() {
        // quite primitive
        Collections.shuffle(pool);
        return pool.get(0).getConnection();
    }
}

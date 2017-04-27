package net.avicus.quest;

import net.avicus.quest.database.Database;
import net.avicus.quest.database.SQLiteUrl;
import net.avicus.quest.query.select.Cursor;
import net.avicus.quest.query.select.Select;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static net.avicus.quest.Functions.sum;

public class SQLiteExample {
    @Test
    public void db() {
        Database db = new Database(SQLiteUrl.of("example.db"));
        db.open();
        db.rawUpdate("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name string, age int, quality string)");

//        System.out.println(db.select("users").select(count()).execute().fetchOne(1).asInteger());

//        time(() -> {
//            return db.select("users").execute().stream()
//                    .mapToInt(Users.AGE::mapNonNull)
//                    .sum();
//        });


        Select select = db.select("users").select(sum("age"));

        time(() -> {
            try (Stream<Cursor> stream = select.fetchLazy().stream()) {
                return stream.mapToInt(rec -> rec.getNonNull(1, int.class)).sum();
            }
        });
    }

    private void time(Supplier<Object> func) {
        long start = System.nanoTime();
        Object result = func.get();
        long end = System.nanoTime();

        long diff = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);
        System.out.println(result);
        System.out.println("it took " + diff + " ms");
    }
}

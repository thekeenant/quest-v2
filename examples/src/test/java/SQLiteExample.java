import net.avicus.quest.Column;
import net.avicus.quest.database.Database;
import net.avicus.quest.database.SQLiteUrl;
import net.avicus.quest.query.insert.Insert;
import net.avicus.quest.query.insert.InsertRecords;
import net.avicus.quest.query.insert.InsertResult;
import net.avicus.quest.query.select.Cursor;
import net.avicus.quest.query.select.Select;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SQLiteExample {
    @Test
    public void db() {
        Database db = new Database(SQLiteUrl.of("example.db"));
        db.open();
        db.rawUpdate("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name string, age int, quality string)");

        Select select = db.select("users").select("age");

        Column<Integer> column = Column.of("age");

        InsertRecords insert = db.insert("users").newRecord()
                .with("name", "Keenan")
                .with(column, 10)
                .with("quality", "WISE")
                .newRecord()
                .with("name", "Adam")
                .with(column, 10)
                .with("quality", "WISE");


        try (InsertResult result = insert.execute()) {
            Cursor cursor = result.getGenerated();
            while (cursor.moveNext()) {
                System.out.println(cursor.get(1));
            }
        }

        time(() -> {
            int sum;
            try (Stream<Cursor> stream = select.fetch().stream()) {
                sum = stream.mapToInt(curr -> curr.getNonNull(column)).sum();
            }
            return sum;
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

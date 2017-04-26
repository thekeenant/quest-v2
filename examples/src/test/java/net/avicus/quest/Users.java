package net.avicus.quest;

import net.avicus.quest.database.Database;
import net.avicus.quest.query.insert.Insert;
import net.avicus.quest.query.select.Select;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Users {
    public static final Column<Integer> id = Column.of("id");
    public static final Column<String> name = Column.of("name");
    public static final Column<Integer> age = Column.of("age");
    public static final MappedColumn<String, Quality> quality = MappedColumn.of("quality", Quality::valueOf, Quality::name);

    private final Database database;

    enum Quality {
        WISE,
        CLEVER,
        ATHLETIC,
        FUNNY;

        public static Quality valueOf(Row row, String quality) {
            return valueOf(quality);
        }
    }

    public Users(Database database) {
        this.database = database;
    }

    public Insert insert(Row... rows) {
        return database.insert("users").insert(rows);
    }

    public Select all() {
        return database.select("users");
    }

    public Select elderly() {
        return all().where(age.greaterThan(70));
    }

    public Select evenAges() {
        return all().where(age.mod(2).eq(0));
    }

    public IntStream ages() {
        return all().execute().stream().map(age::getRequired).mapToInt(Integer::intValue);
    }

    public Stream<Quality> qualities() {
        return all().execute().stream().map(quality::getRequired);
    }
}

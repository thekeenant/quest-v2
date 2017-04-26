package net.avicus.quest;

import net.avicus.quest.database.Database;
import net.avicus.quest.query.select.Select;

import java.util.Optional;
import java.util.stream.Stream;

import static net.avicus.quest.Functions.count;

public class Users {
    public static final Column<Integer> ID = Column.of("id");
    public static final Column<String> NAME = Column.of("name");
    public static final Column<Integer> AGE = Column.of("age");
    public static final MappedColumn<String, Quality> QUALITY = MappedColumn.of(
            "quality",
            String.class, Quality.class,
            Quality::valueOf, Quality::name);

    private final Database database;

    enum Quality {
        WISE,
        CLEVER,
        ATHLETIC,
        FUNNY
    }

    public Users(Database database) {
        this.database = database;
    }

    public void insert(String name, int age, Quality quality) {
        Record record = Record.builder()
                .with(Users.NAME, name)
                .with(Users.AGE, age)
                .with(Users.QUALITY, quality)
                .build();
        database.insert("users").with(record).execute();
    }

    public Select all() {
        return database.select("users");
    }

    public Select elderly() {
        return all().where(AGE.gt(70));
    }

    public int sumAges() {
        return all().select(count()).execute().fetchOne(1).asNonNullInteger();
    }

    public Stream<Optional<Quality>> qualities() {
        return all().execute().stream().map(QUALITY::map);
    }
}

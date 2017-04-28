package net.avicus.quest.query.insert;

import net.avicus.quest.database.DatabaseConnection;
import net.avicus.quest.parameter.FieldParam;
import net.avicus.quest.query.select.Select;

public class Insert {
    private final DatabaseConnection database;
    private final FieldParam table;

    public Insert(DatabaseConnection database, FieldParam table) {
        this.database = database;
        this.table = table;
    }

    public InsertRecords newRecord() {
        return new InsertRecords(database, table);
    }

    public InsertSelection select(Select select) {
        return new InsertSelection(database, table, select);
    }

    @Override
    public String toString() {
        return "Insert(table=" + table + ")";
    }
}

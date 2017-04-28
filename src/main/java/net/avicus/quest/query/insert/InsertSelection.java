package net.avicus.quest.query.insert;

import net.avicus.quest.database.DatabaseConnection;
import net.avicus.quest.parameter.FieldParam;
import net.avicus.quest.query.select.Select;

public class InsertSelection extends Insert {
    private final Select select;

    public InsertSelection(DatabaseConnection database, FieldParam table, Select select) {
        super(database, table);
        this.select = select;
    }
}

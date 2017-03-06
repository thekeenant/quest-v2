package net.avicus.quest.table;

import net.avicus.quest.Row;

public interface RowMapper<M> {
    M map(Row row);
}

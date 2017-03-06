package net.avicus.quest.table;

import net.avicus.quest.Row;
import net.avicus.quest.parameter.WildcardParam;
import net.avicus.quest.query.Filter;
import net.avicus.quest.query.insert.Insertion;

public interface Model<T> {
    WildcardParam WILDCARD = WildcardParam.INSTANCE;

    default Filter toFilter() {
        return new Filter(getIdColumn(), getId());
    }

    default String getIdColumn() {
        return "id";
    }

    T getId();

    Insertion toInsertion();

    Row toRow();
}

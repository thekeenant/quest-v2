package net.avicus.quest.model;

import net.avicus.quest.Column;
import net.avicus.quest.Param;
import net.avicus.quest.parameter.FieldParam;

import java.util.List;

public interface TableSchema {
    FieldParam getName();

    List<ColumnDefinition> getColumns();

    class ColumnDefinition implements Param {
        private final Column column;
        private final String datatype;

        public ColumnDefinition(Column column, String datatype) {
            this.column = column;
            this.datatype = datatype;
        }

        @Override
        public String getParamString() {
            return column.getParamString() + " " + datatype;
        }

        @Override
        public List<Object> getValues() {
            return column.getValues();
        }
    }
}

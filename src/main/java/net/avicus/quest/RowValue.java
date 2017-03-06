package net.avicus.quest;

import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.table.MappedColumn;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class RowValue {
    private final Object data;

    public RowValue(Object data) {
        this.data = data;
    }

    public <T> T asRequired(Class<T> type) {
        T data = as(type).orElse(null);
        if (data == null) {
            throw new DatabaseException("Unexpected null value.");
        }
        return data;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> as(Class<T> type) {
        if (this.data == null)
            return Optional.empty();

        T data = (T) this.data;
        return Optional.of(data);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> as(MappedColumn<?, T> type) {
        return (Optional<T>) as(Object.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T asRequired(MappedColumn<?, T> type) {
        return (T) asRequired(Object.class);
    }

    public Optional<Object> asObject() {
        return as(Object.class);
    }

    public Object asRequiredObject() {
        return asRequired(Object.class);
    }

    public Optional<String> asString() {
        return as(String.class);
    }

    public String asRequiredString() {
        return asRequired(String.class);
    }

    public Optional<Date> asDate() {
        return as(Date.class);
    }

    public Date asRequiredDate() {
        return asRequired(Date.class);
    }

    public Optional<Time> asTime() {
        return as(Time.class);
    }

    public Time asRequiredTime() {
        return asRequired(Time.class);
    }

    public Optional<Timestamp> asTimestamp() {
        return as(Timestamp.class);
    }

    public Timestamp asRequiredTimestamp() {
        return asRequired(Timestamp.class);
    }

    public Optional<Boolean> asBoolean() {
        return as(Object.class).map(RowValue::booleanValue);
    }

    public boolean asRequiredBoolean() {
        return booleanValue(asRequired(Object.class));
    }

    public Optional<Integer> asInteger() {
        return as(Integer.class);
    }

    public int asRequiredInteger() {
        return asRequired(Integer.class);
    }

    public Optional<Long> asLong() {
        return as(Long.class);
    }

    public long asRequiredLong() {
        return asRequired(Long.class);
    }

    public Optional<Float> asFloat() {
        return as(Float.class);
    }

    public float asRequiredFloat() {
        return asRequired(Float.class);
    }

    public Optional<Double> asDouble() {
        return as(Double.class);
    }

    public double asRequiredDouble() {
        return asRequired(Double.class);
    }

    public Optional<Short> asShort() {
        return as(Short.class);
    }

    public short asRequiredShort() {
        return asRequired(Short.class);
    }

    public Optional<Byte> asByte() {
        return as(Byte.class);
    }

    public byte asRequiredByte() {
        return asRequired(Byte.class);
    }

    public Optional<BigDecimal> asBigDecimal() {
        return as(BigDecimal.class);
    }

    public BigDecimal asRequiredBigDecimal() {
        return asRequired(BigDecimal.class);
    }

    public Optional<Number> asNumber() {
        return as(Number.class);
    }

    public Number asRequiredNumber() {
        return asRequired(Number.class);
    }

    @Override
    public String toString() {
        return "RowValue(" + this.data + ")";
    }

    private static boolean booleanValue(Object data) {
        return Objects.equals(data, true) || Objects.equals(data, (byte) 1);
    }
}

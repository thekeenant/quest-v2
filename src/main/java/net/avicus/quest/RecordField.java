package net.avicus.quest;

import net.avicus.quest.database.DatabaseException;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class RecordField {
    private final Object data;

    public RecordField(Object data) {
        this.data = data;
    }

    public <T> T asNonNull(Class<T> type) {
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
    public <T> Optional<T> as(Column<T> type) {
        return type.map(this);
    }

    @SuppressWarnings("unchecked")
    public <T> T asNonNull(Column<T> type) {
        return type.mapNonNull(this);
    }

    public Optional<Object> asObject() {
        return as(Object.class);
    }

    public Object asNonNullObject() {
        return asNonNull(Object.class);
    }

    public Optional<String> asString() {
        return as(String.class);
    }

    public String asNonNullString() {
        return asNonNull(String.class);
    }

    public Optional<Date> asDate() {
        return as(Date.class);
    }

    public Date asNonNullDate() {
        return asNonNull(Date.class);
    }

    public Optional<Time> asTime() {
        return as(Time.class);
    }

    public Time asNonNullTime() {
        return asNonNull(Time.class);
    }

    public Optional<Timestamp> asTimestamp() {
        return as(Timestamp.class);
    }

    public Timestamp asNonNullTimestamp() {
        return asNonNull(Timestamp.class);
    }

    public Optional<Boolean> asBoolean() {
        return as(Object.class).map(RecordField::booleanValue);
    }

    public boolean asNonNullBoolean() {
        return booleanValue(asNonNull(Object.class));
    }

    public Optional<Integer> asInteger() {
        return as(Integer.class);
    }

    public int asNonNullInteger() {
        return asNonNull(Integer.class);
    }

    public Optional<Long> asLong() {
        return as(Long.class);
    }

    public long asNonNullLong() {
        return asNonNull(Long.class);
    }

    public Optional<Float> asFloat() {
        return as(Float.class);
    }

    public float asNonNullFloat() {
        return asNonNull(Float.class);
    }

    public Optional<Double> asDouble() {
        return as(Double.class);
    }

    public double asNonNullDouble() {
        return asNonNull(Double.class);
    }

    public Optional<Short> asShort() {
        return as(Short.class);
    }

    public short asNonNullShort() {
        return asNonNull(Short.class);
    }

    public Optional<Byte> asByte() {
        return as(Byte.class);
    }

    public byte asNonNullByte() {
        return asNonNull(Byte.class);
    }

    public Optional<BigDecimal> asBigDecimal() {
        return as(BigDecimal.class);
    }

    public BigDecimal asNonNullBigDecimal() {
        return asNonNull(BigDecimal.class);
    }

    public Optional<Number> asNumber() {
        return as(Number.class);
    }

    public Number asNonNullNumber() {
        return asNonNull(Number.class);
    }

    @Override
    public String toString() {
        return "RowValue(" + this.data + ")";
    }

    private static boolean booleanValue(Object data) {
        return Objects.equals(data, true) || Objects.equals(data, (byte) 1);
    }
}

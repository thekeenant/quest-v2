package net.avicus.quest;

import net.avicus.quest.database.DatabaseException;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public interface Record {
    boolean hasField(int field);

    Object getField(int field);

    int getFieldIndex(String label);

    @SuppressWarnings("unchecked")
    default <T> Optional<T> get(Class<T> type, int field) {
        Object data = getField(field);

        if (data == null)
            return Optional.empty();
        
        if (type.isInstance(data)) {
            T result = (T) data;
            return Optional.of(result);
        }
        else {
            throw new DatabaseException("Couldn't force record field type from " + data.getClass() + " to " + type);
        }
    }

    @SuppressWarnings("unchecked")
    default <T> T getNonNull(Class<T> type, int field) {
        Object data = getField(field);
        if (data == null) {
            throw new DatabaseException("Unexpected null value");
        }

        if (type.isInstance(data)) {
            return (T) data;
        }
        else {
            throw new DatabaseException("Couldn't force record field type from " + data.getClass() + " to " + type);
        }
    }

    default Optional<Object> getObject(int field) {
        return get(Object.class, field);
    }

    default Object getNonNullObject(int field) {
        return getNonNull(Object.class, field);
    }

    default Optional<Object> getObject(String label) {
        return get(Object.class, getFieldIndex(label));
    }

    default Object getNonNullObject(String label) {
        return getNonNull(Object.class, getFieldIndex(label));
    }

    default Optional<String> getString(int field) {
        return get(String.class, field);
    }

    default String getNonNullString(int field) {
        return getNonNull(String.class, field);
    }

    default Optional<Date> getDate(int field) {
        return get(Date.class, field);
    }

    default Date getNonNullDate(int field) {
        return getNonNull(Date.class, field);
    }

    default Optional<Time> getTime(int field) {
        return get(Time.class, field);
    }

    default Time getNonNullTime(int field) {
        return getNonNull(Time.class, field);
    }

    default Optional<Timestamp> getTimestamp(int field) {
        return get(Timestamp.class, field);
    }

    default Timestamp getNonNullTimestamp(int field) {
        return getNonNull(Timestamp.class, field);
    }

    default Optional<Boolean> getBoolean(int field) {
        // Todo: Accept 1 get true get well?
        return getObject(field).map(data -> Objects.equals(data, true));
    }

    default boolean getNonNullBoolean(int field) {
        // Todo: Accept 1 get true get well?
        return Objects.equals((getNonNullObject(field)), true);
    }

    default Optional<Integer> getInteger(int field) {
        return get(Integer.class, field);
    }

    default int getNonNullInteger(int field) {
        return getNonNull(Integer.class, field);
    }

    default Optional<Long> getLong(int field) {
        return get(Long.class, field);
    }

    default long getNonNullLong(int field) {
        return getNonNull(Long.class, field);
    }

    default Optional<Float> getFloat(int field) {
        return get(Float.class, field);
    }

    default float getNonNullFloat(int field) {
        return getNonNull(Float.class, field);
    }

    default Optional<Double> getDouble(int field) {
        return get(Double.class, field);
    }

    default double getNonNullDouble(int field) {
        return getNonNull(Double.class, field);
    }

    default Optional<Short> getShort(int field) {
        return get(Short.class, field);
    }

    default short getNonNullShort(int field) {
        return getNonNull(Short.class, field);
    }

    default Optional<Byte> getByte(int field) {
        return get(Byte.class, field);
    }

    default byte getNonNullByte(int field) {
        return getNonNull(Byte.class, field);
    }

    default Optional<BigDecimal> getBigDecimal(int field) {
        return get(BigDecimal.class, field);
    }

    default BigDecimal getNonNullBigDecimal(int field) {
        return getNonNull(BigDecimal.class, field);
    }

    default Optional<Number> getNumber(int field) {
        return get(Number.class, field);
    }

    default Number getNonNullNumber(int field) {
        return getNonNull(Number.class, field);
    }

}

package net.avicus.quest.query;

public interface Query<R extends QueryResult> {
    R execute();
}

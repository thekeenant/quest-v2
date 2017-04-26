package net.avicus.quest.query;

public interface Query<R extends QueryResult, C extends QueryConfig> {
    C getDefaultConfig();

    default R execute() {
        return execute(getDefaultConfig());
    }

    R execute(C config);
}

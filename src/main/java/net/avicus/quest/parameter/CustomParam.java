package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomParam implements Param {
    private final String key;
    private final List<Param> params;

    public CustomParam(String key, List<Param> params) {
        this.key = key;
        this.params = params;
    }

    public CustomParam(String key, Param... params) {
        this(key, Arrays.asList(params));
    }

    public CustomParam(String key, Object... objects) {
        this(key, Arrays.asList(objects).stream().map(ObjectParam::new).collect(Collectors.toList()));
    }

    @Override
    public String getKey() {
        return this.key;
    }

    public List<Object> getObjects() {
        List<Object> objects = new ArrayList<>();
        for (Param param : this.params) {
            objects.addAll(param.getObjects());
        }
        return objects;
    }

    @Override
    public String toString() {
        return "CustomParam(sql='" + this.key + "', params=" + this.params + ")";
    }
}

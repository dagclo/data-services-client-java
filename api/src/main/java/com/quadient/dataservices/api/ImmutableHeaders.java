package com.quadient.dataservices.api;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class ImmutableHeaders implements Headers {

    private final Map<String, String> map;

    public ImmutableHeaders() {
        this(Collections.emptyMap());
    }

    public ImmutableHeaders(Map<String, String> map) {
        Objects.requireNonNull(map);
        this.map = map;
    }

    @Override
    public String getValue(String key) {
        return map.get(key);
    }

    @Override
    public Collection<String> getKeys() {
        return map.keySet();
    }

}

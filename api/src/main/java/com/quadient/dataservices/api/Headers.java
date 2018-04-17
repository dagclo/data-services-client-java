package com.quadient.dataservices.api;

import java.util.Collection;

public interface Headers {

    public static Headers EMPTY = new ImmutableHeaders();

    public String getValue(String key);

    public Collection<String> getKeys();
}

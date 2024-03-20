package com.amoalla.redis.core;

import java.time.Duration;

public interface RedisCache extends AutoCloseable {
    Object get(String key);

    void put(String key, Object value);

    void putAndKeepTTL(String key, Object value);

    void put(String key, Object value, Duration ttl);

    boolean containsKey(String key);
}

package com.amoalla.redis.core;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.ExpiryPolicy;

import java.time.Duration;
import java.util.function.Supplier;

public class RedisCache implements AutoCloseable {

    private final CacheManager cacheManager;
    private final Cache<String, Object> cache;

    public RedisCache() {
        cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .build();
        cacheManager.init();
        var config = CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Object.class, ResourcePoolsBuilder.heap(100))
                .withExpiry(new PerEntryExpiryPolicy())
                .build();
        cache = cacheManager.createCache("cache", config);
    }

    public Object get(String key) {
        return cache.get(key);
    }

    public void put(String key, Object value) {
        cache.put(key, value);
    }

    public void putAndKeepTTL(String key, Object value) {
        cache.put(key, new ValueWithExpiration(value, PerEntryExpiryPolicy.INFINITE, true));
    }

    public void put(String key, Object value, Duration ttl) {
        cache.put(key, new ValueWithExpiration(value, ttl));
    }

    @Override
    public void close() {
        cacheManager.close();
    }

    private record ValueWithExpiration(Object value, Duration expiration, boolean keepTTL) {
        ValueWithExpiration(Object value, Duration expiration) {
            this(value, expiration, false);
        }
    }

    private static class PerEntryExpiryPolicy implements ExpiryPolicy<String, Object> {

        @Override
        public Duration getExpiryForCreation(String key, Object value) {
            if (value instanceof ValueWithExpiration valueWithExpiration) {
                return valueWithExpiration.expiration();
            }
            return INFINITE;
        }

        @Override
        public Duration getExpiryForAccess(String key, Supplier<?> value) {
            if (value.get() instanceof ValueWithExpiration valueWithExpiration) {
                return valueWithExpiration.expiration();
            }
            return INFINITE;
        }

        @Override
        public Duration getExpiryForUpdate(String key, Supplier<?> oldValue, Object newValue) {
            if (newValue instanceof ValueWithExpiration valueWithExpiration) {
                if (valueWithExpiration.keepTTL() && oldValue.get() instanceof ValueWithExpiration old) {
                    return old.expiration();
                }
                return valueWithExpiration.expiration();
            }
            return INFINITE;
        }
    }

}

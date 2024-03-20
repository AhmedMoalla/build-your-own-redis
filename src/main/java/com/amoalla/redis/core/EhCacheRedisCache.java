package com.amoalla.redis.core;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.ExpiryPolicy;

import java.time.Duration;
import java.util.function.Supplier;

public class EhCacheRedisCache implements RedisCache {

    private final CacheManager cacheManager;
    private final Cache<String, Object> cache;

    public EhCacheRedisCache() {
        cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .build();
        cacheManager.init();
        var config = CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Object.class, ResourcePoolsBuilder.heap(100))
                .withExpiry(new PerEntryExpiryPolicy())
                .build();
        cache = cacheManager.createCache("cache", config);
    }

    @Override
    public Object get(String key) {
        Object value = cache.get(key);
        if (value instanceof ValueWithExpiration valueWithExpiration) {
            return valueWithExpiration.value();
        }
        return value;
    }

    @Override
    public void put(String key, Object value) {
        cache.put(key, value);
    }

    @Override
    public void putAndKeepTTL(String key, Object value) {
        cache.put(key, new ValueWithExpiration(value, PerEntryExpiryPolicy.INFINITE, true));
    }

    @Override
    public void put(String key, Object value, Duration ttl) {
        cache.put(key, new ValueWithExpiration(value, ttl));
    }

    @Override
    public boolean containsKey(String key) {
        return cache.containsKey(key);
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

package com.amoalla.redis.command;

import com.amoalla.redis.core.RedisCache;
import com.amoalla.redis.exception.CommandParsingException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Sets;

import java.time.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.amoalla.redis.command.util.IteratorUtils.nextPositiveInt;
import static com.amoalla.redis.command.util.IteratorUtils.nextString;

@Builder
public record SetCommand(String key, String value, SetMode mode, boolean setAndGet, Duration expiration,
                         boolean keepTTL) implements RedisCommand {

    public static final Duration INFINITE = Duration.ofNanos(Long.MAX_VALUE);
    private static final Set<Object> EXPIRATION_ARGS = Set.of("EX", "PX", "EXAT", "PXAT", "KEEPTTL");

    @RequiredArgsConstructor
    public enum SetMode implements BiPredicate<RedisCache, String> {
        DEFAULT((cache, key) -> true),
        SET_IF_NOT_EXISTS((cache, key) -> !cache.containsKey(key)),
        SET_IF_EXISTS(RedisCache::containsKey);
        private final BiPredicate<RedisCache, String> predicate;

        @Override
        public boolean test(RedisCache cache, String key) {
            return predicate.test(cache, key);
        }
    }

    public static SetCommand parse(List<Object> args) {
        validateArgs(args);

        Iterator<Object> iterator = args.iterator();
        String key = nextString(iterator);
        String value = nextString(iterator);

        SetMode mode = SetMode.DEFAULT;
        boolean setAndGet = false;
        Duration expiration = INFINITE;
        boolean keepTTL = false;

        while (iterator.hasNext()) {
            String next = nextString(iterator).toUpperCase();
            switch (next) {
                case "NX" -> mode = SetMode.SET_IF_NOT_EXISTS;
                case "XX" -> mode = SetMode.SET_IF_EXISTS;
                case "GET" -> setAndGet = true;
                case "EX" -> expiration = Duration.ofSeconds(nextPositiveInt(iterator));
                case "PX" -> expiration = Duration.ofMillis(nextPositiveInt(iterator));
                case "EXAT" -> {
                    var expirationDateTime = LocalDateTime.ofEpochSecond(nextPositiveInt(iterator), 0, ZoneOffset.UTC);
                    expiration = Duration.between(LocalDateTime.now(), expirationDateTime);
                }
                case "PXAT" -> {
                    var expirationDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(nextPositiveInt(iterator)), ZoneId.of("UTC"));
                    expiration = Duration.between(LocalDateTime.now(), expirationDateTime);
                }
                case "KEEPTTL" -> keepTTL = true;
                default -> throw new CommandParsingException(STR."Unknown argument: \{next}");
            }
        }

        return new SetCommand(key, value, mode, setAndGet, expiration, keepTTL);
    }

    private static final int MIN_ARGS = 2;
    private static final int MAX_ARGS = 6;

    private static void validateArgs(List<Object> args) {
        if (args.size() < MIN_ARGS || args.size() > MAX_ARGS) {
            throw new CommandParsingException(STR."Expected between \{MIN_ARGS} and \{MAX_ARGS} args. Got \{args.size()} instead.");
        }

        if (args.contains("NX") && args.contains("XX")) {
            throw new CommandParsingException("Syntax Error: Can't have NX and XX at the same time.");
        }

        MutableSet<Object> expirationArgs = Sets.intersect(EXPIRATION_ARGS, new HashSet<>(args));
        if (expirationArgs.size() > 1) {
            throw new CommandParsingException(STR."Syntax Error: Can't have multiple expiration args at the same time. Found: \{expirationArgs}");
        }

    }
}

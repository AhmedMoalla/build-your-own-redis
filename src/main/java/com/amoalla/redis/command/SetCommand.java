package com.amoalla.redis.command;

import com.amoalla.redis.exception.CommandParsingException;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Sets;

import java.time.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.amoalla.redis.command.util.IteratorUtils.*;

public record SetCommand(String key, String value, SetMode mode, boolean setAndGet, Duration expiration, boolean keepTTL) implements RedisCommand {

    public static final Duration INFINITE = Duration.ofNanos(Long.MAX_VALUE);
    private static final Set<Object> EXPIRATION_ARGS = Set.of("EX", "PX", "EXAT", "PXAT", "KEEPTTL");

    public SetCommand(String key, String value) {
        this(key, value, SetMode.DEFAULT, false, INFINITE, false);
    }

    public enum SetMode {
        DEFAULT, SET_IF_NOT_EXISTS, SET_IF_EXISTS
    }

    public static SetCommand parse(List<Object> args) {
        validateArgs(args);

        Iterator<Object> iterator = args.iterator();
        String key = nextString(iterator);
        String value = nextString(iterator);

        SetMode mode = SetMode.DEFAULT;
        boolean getAndSet = false;
        Duration expiration = INFINITE;
        boolean keepTTL = false;

        while (iterator.hasNext()) {
            String next = nextString(iterator);
            switch (next) {
                case "NX" -> mode = SetMode.SET_IF_NOT_EXISTS;
                case "XX" -> mode = SetMode.SET_IF_EXISTS;
                case "GET" -> getAndSet = true;
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
                default -> throw new CommandParsingException("Unknown argument: " + next);
            }
        }

        return new SetCommand(key, value, mode, getAndSet, expiration, keepTTL);
    }

    private static void validateArgs(List<Object> args) {
        if (args.size() < 2 || args.size() > 5) {
            throw new CommandParsingException("Expected between 2 and 5 args. Got " + args.size() + " instead.");
        }

        if (args.contains("NX") && args.contains("XX")) {
            throw new CommandParsingException("Syntax Error: Can't have NX and XX at the same time.");
        }

        MutableSet<Object> expirationArgs = Sets.intersect(EXPIRATION_ARGS, new HashSet<>(args));
        if (expirationArgs.size() > 1) {
            throw new CommandParsingException("Syntax Error: Can't have multiple expiration args at the same time. Found: " + expirationArgs);
        }

    }
}

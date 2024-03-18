package com.amoalla.redis;

import java.util.function.Function;

import static com.amoalla.redis.Main.CACHE;

public enum RedisCommand {
    echo(args -> args[0]),
    ping(args -> "PONG"),
    set(args -> {
        CACHE.put((String) args[0], args[1]);
        return "OK";
    }),
    get(args -> CACHE.get((String) args[0]).toString());

    private final Function<Object[], Object> fn;

    RedisCommand(Function<Object[], Object> fn) {
        this.fn = fn;
    }

    public Object execute(Object[] args) {
        return fn.apply(args);
    }

    public static RedisCommand parseCommand(String commandName) {
        return RedisCommand.valueOf(commandName);
    }
}
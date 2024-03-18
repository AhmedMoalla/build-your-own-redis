package com.amoalla.redis;

import java.util.function.Function;

public enum RedisCommand {
    echo(args -> args[0]),
    ping(args -> "PONG"),;

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
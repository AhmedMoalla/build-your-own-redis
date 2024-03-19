package com.amoalla.redis.command;

import java.util.List;

public record EchoCommand(String input) implements RedisCommand {
    public static EchoCommand parse(List<Object> args) {
        return new EchoCommand((String) args.getFirst());
    }
}

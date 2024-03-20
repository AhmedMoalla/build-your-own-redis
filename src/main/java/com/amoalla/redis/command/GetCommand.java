package com.amoalla.redis.command;

import java.util.List;

public record GetCommand(String key) implements RedisCommand {

    public static GetCommand parse(List<Object> args) {
        return new GetCommand((String) args.getFirst());
    }
}

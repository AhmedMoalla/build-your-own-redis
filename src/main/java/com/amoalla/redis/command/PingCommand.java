package com.amoalla.redis.command;

import java.util.List;

public final class PingCommand implements RedisCommand {
    private static final PingCommand INSTANCE = new PingCommand();

    public static PingCommand parse(List<Object> args) {
        return INSTANCE;
    }
}

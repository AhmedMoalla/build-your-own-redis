package com.amoalla.redis.command;

import java.util.List;
import java.util.function.Function;

public enum RedisCommandType {
    echo(EchoCommand::parse),
    ping(PingCommand::parse),
    set(SetCommand::parse),
    get(args -> null);

    private final Function<List<Object>, RedisCommand> argsParser;

    RedisCommandType(Function<List<Object>, RedisCommand> argsParser) {
        this.argsParser = argsParser;
    }

    public RedisCommand parse(List<Object> args) {
        return argsParser.apply(args);
    }
}
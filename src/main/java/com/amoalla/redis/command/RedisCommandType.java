package com.amoalla.redis.command;

import java.util.List;
import java.util.function.Function;

public enum RedisCommandType {
    ECHO(EchoCommand::parse),
    PING(PingCommand::parse),
    SET(SetCommand::parse),
    GET(GetCommand::parse),
    INFO(InfoCommand::parse),
    REPLCONF(ReplConfCommand::parse);

    private final Function<List<Object>, RedisCommand> argsParser;

    RedisCommandType(Function<List<Object>, RedisCommand> argsParser) {
        this.argsParser = argsParser;
    }

    public RedisCommand parse(List<Object> args) {
        return argsParser.apply(args);
    }
}